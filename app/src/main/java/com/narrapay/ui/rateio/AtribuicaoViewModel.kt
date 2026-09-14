package com.narrapay.ui.rateio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.narrapay.data.local.dao.PessoaDao
import com.narrapay.data.local.dao.TransacaoDao
import com.narrapay.data.local.dao.CategoriaDao
import com.narrapay.data.local.entity.CategoriaEntity
import com.narrapay.data.local.entity.PessoaEntity
import com.narrapay.domain.exception.AtribuicaoDivergenteException
import com.narrapay.domain.usecase.AtribuirValoresUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AtribuicaoViewModel(
    private val transacaoDao: TransacaoDao,
    private val pessoaDao: PessoaDao,
    private val categoriaDao: CategoriaDao,
    private val atribuirValoresUseCase: AtribuirValoresUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RateioUiState(isLoading = true))
    val uiState: StateFlow<RateioUiState> = _uiState.asStateFlow()
    
    private val _eventos = Channel<AtribuicaoEvent>()
    val eventos = _eventos.receiveAsFlow()

    init {
        viewModelScope.launch {
            categoriaDao.getAll().collect { categorias ->
                _uiState.update { it.copy(categorias = categorias) }
            }
        }
        
        viewModelScope.launch {
            pessoaDao.getAll().collect { pessoas ->
                _uiState.update { it.copy(todasAsPessoas = pessoas) }
            }
        }
    }

    fun carregarTransacao(transacaoId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val transacaoCompleta = transacaoDao.getTransacaoCompletaById(transacaoId)
                
                if (transacaoCompleta != null) {
                    val transacao = transacaoCompleta.transacao
                    val titulo = transacao.descricaoCustomizada ?: transacao.descricaoOriginal
                    val valorStr = String.format(java.util.Locale.US, "%.2f", transacao.valorTotal)
                    
                    val mapValores = mutableMapOf<Long, String>()
                    val pessoasNaDivida = mutableListOf<PessoaEntity>()
                    
                    if (transacaoCompleta.atribuicoes.isNotEmpty()) {
                        transacaoCompleta.atribuicoes.forEach { atrib ->
                            mapValores[atrib.pessoa.id] = String.format(java.util.Locale.US, "%.2f", atrib.atribuicao.valorAtribuido)
                            pessoasNaDivida.add(atrib.pessoa)
                        }
                    } else {
                        val todas = pessoaDao.getAll().firstOrNull() ?: emptyList()
                        todas.forEach { p ->
                            pessoasNaDivida.add(p)
                        }
                    }

                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            transacaoId = transacaoId,
                            tituloInput = titulo,
                            valorTotalInput = valorStr,
                            dataHora = transacao.dataHora,
                            categoriaIdSelecionada = transacao.categoriaId,
                            pessoasNaDivida = pessoasNaDivida,
                            valoresInput = mapValores
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Transação não encontrada") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun atualizarTitulo(titulo: String) {
        _uiState.update { it.copy(tituloInput = titulo) }
    }

    fun atualizarValorTotal(valor: String) {
        val filtrado = valor.filter { it.isDigit() || it == '.' || it == ',' }
        _uiState.update { it.copy(valorTotalInput = filtrado) }
    }
    
    fun atualizarCategoria(categoriaId: Long?) {
        _uiState.update { it.copy(categoriaIdSelecionada = categoriaId) }
    }
    
    fun adicionarCategoria(nome: String, icone: String, corHex: String) {
        viewModelScope.launch {
            val novaCategoria = CategoriaEntity(nome = nome, icone = icone, corHex = corHex)
            val idCriado = categoriaDao.insert(novaCategoria)
            atualizarCategoria(idCriado)
        }
    }
    
    fun deletarCategoria(categoria: CategoriaEntity) {
        viewModelScope.launch {
            categoriaDao.delete(categoria)
            if (_uiState.value.categoriaIdSelecionada == categoria.id) {
                atualizarCategoria(null)
            }
        }
    }
    
    fun editarCategoria(categoria: CategoriaEntity, novoNome: String) {
        viewModelScope.launch {
            categoriaDao.update(categoria.copy(nome = novoNome))
        }
    }

    fun togglePessoaNaDivida(pessoa: PessoaEntity) {
        _uiState.update { currentState ->
            val pessoasAtuais = currentState.pessoasNaDivida.toMutableList()
            val novosValores = currentState.valoresInput.toMutableMap()
            if (pessoasAtuais.any { it.id == pessoa.id }) {
                pessoasAtuais.removeAll { it.id == pessoa.id }
                novosValores.remove(pessoa.id)
            } else {
                pessoasAtuais.add(pessoa)
            }
            currentState.copy(pessoasNaDivida = pessoasAtuais, valoresInput = novosValores)
        }
    }

    fun atualizarParte(pessoaId: Long, valor: String) {
        _uiState.update { currentState ->
            val novosValores = currentState.valoresInput.toMutableMap()
            val filtrado = valor.filter { it.isDigit() || it == '.' || it == ',' }
            novosValores[pessoaId] = filtrado
            currentState.copy(valoresInput = novosValores)
        }
    }
    
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun confirmarRateio() {
        val currentState = _uiState.value
        val mapaAtribuicoes = mutableMapOf<Long, Double>()
        
        currentState.pessoasNaDivida.forEach { pessoa ->
            val input = currentState.valoresInput[pessoa.id] ?: ""
            val valorFormatado = input.replace(",", ".").toDoubleOrNull() ?: 0.0
            if (valorFormatado >= 0) {
                mapaAtribuicoes[pessoa.id] = valorFormatado
            }
        }

        viewModelScope.launch {
            try {
                val transacaoCompleta = transacaoDao.getTransacaoCompletaById(currentState.transacaoId)
                if (transacaoCompleta != null) {
                    val valorNovo = currentState.valorTotalInput.replace(",", ".").toDoubleOrNull() ?: transacaoCompleta.transacao.valorTotal
                    
                    val transacaoEditada = transacaoCompleta.copy(
                        transacao = transacaoCompleta.transacao.copy(
                            descricaoCustomizada = currentState.tituloInput,
                            valorTotal = valorNovo,
                            categoriaId = currentState.categoriaIdSelecionada
                        )
                    )
                    
                    atribuirValoresUseCase(transacaoEditada, mapaAtribuicoes)
                    _eventos.send(AtribuicaoEvent.Sucesso)
                }
            } catch (e: AtribuicaoDivergenteException) {
                _uiState.update { it.copy(error = e.message) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Erro inesperado: ${e.message}") }
            }
        }
    }
    fun ignorarTransacao() {
        val currentState = _uiState.value
        viewModelScope.launch {
            try {
                val transacaoCompleta = transacaoDao.getTransacaoCompletaById(currentState.transacaoId)
                if (transacaoCompleta != null) {
                    val transacaoEditada = transacaoCompleta.transacao.copy(
                        statusAtribuicao = "IGNORADO"
                    )
                    atribuirValoresUseCase(
                        transacaoCompleta.copy(transacao = transacaoEditada), 
                        emptyMap()
                    )
                    _eventos.send(AtribuicaoEvent.Sucesso)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Erro ao ignorar: ${e.message}") }
            }
        }
    }
}

sealed class AtribuicaoEvent {
    object Sucesso : AtribuicaoEvent()
}
