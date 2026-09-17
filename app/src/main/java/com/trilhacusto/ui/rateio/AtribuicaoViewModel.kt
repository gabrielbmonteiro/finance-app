package com.trilhacusto.ui.rateio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trilhacusto.data.local.dao.CategoriaDao
import com.trilhacusto.data.local.dao.PessoaDao
import com.trilhacusto.data.local.dao.TransacaoDao
import com.trilhacusto.data.local.entity.CategoriaEntity
import com.trilhacusto.data.local.entity.PessoaEntity
import com.trilhacusto.domain.exception.AtribuicaoDivergenteException
import com.trilhacusto.domain.usecase.AtribuirValoresUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
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
        val filtrado = valor.filterIndexed { index, c -> c.isDigit() || c == '.' || c == ',' || (index == 0 && c == '-') }
        _uiState.update { it.copy(valorTotalInput = filtrado) }
    }
    
    fun atualizarDataHora(dataHora: Long) {
        _uiState.update { it.copy(dataHora = dataHora) }
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

    fun atualizarDropdownPessoa(pessoaId: Long?) {
        _uiState.update { it.copy(dropdownPessoaId = pessoaId) }
    }

    fun atualizarDropdownValor(valor: String) {
        val filtrado = valor.filterIndexed { index, c -> c.isDigit() || c == '.' || c == ',' || (index == 0 && c == '-') }
        _uiState.update { it.copy(dropdownValor = filtrado) }
    }

    fun adicionarResponsavelDropdown(pessoaId: Long) {
        val state = _uiState.value
        val pessoa = state.todasAsPessoas.find { it.id == pessoaId } ?: return

        if (!state.pessoasNaDivida.any { it.id == pessoaId }) {
            _uiState.update { currentState ->
                val pessoasAtuais = currentState.pessoasNaDivida.toMutableList().apply { add(pessoa) }
                val novosValores = currentState.valoresInput.toMutableMap().apply { put(pessoaId, "") }
                currentState.copy(
                    pessoasNaDivida = pessoasAtuais,
                    valoresInput = novosValores,
                    dropdownPessoaId = null,
                    dropdownValor = ""
                )
            }
        }
    }

    fun removerResponsavelDropdown(pessoaId: Long) {
        _uiState.update { currentState ->
            val pessoasAtuais = currentState.pessoasNaDivida.toMutableList().apply { removeAll { it.id == pessoaId } }
            val novosValores = currentState.valoresInput.toMutableMap().apply { remove(pessoaId) }
            currentState.copy(
                pessoasNaDivida = pessoasAtuais,
                valoresInput = novosValores
            )
        }
    }

    fun atualizarParte(pessoaId: Long, valor: String) {
        val filtrado = valor.filterIndexed { index, c -> c.isDigit() || c == '.' || c == ',' || (index == 0 && c == '-') }
        _uiState.update { currentState ->
            val novosValores = currentState.valoresInput.toMutableMap()
            novosValores[pessoaId] = filtrado
            
            val totalValue = currentState.valorTotalInput.replace(",", ".").toDoubleOrNull() ?: 0.0
            val sumParts = novosValores.values.sumOf { it.replace(",", ".").toDoubleOrNull() ?: 0.0 }
            val remainder = totalValue - sumParts
            
            var suggestedPessoaId: Long? = null
            var suggestedValue: String? = null
            
            if (remainder > 0.009) {
                val emptyPerson = currentState.pessoasNaDivida.firstOrNull { 
                    val v = novosValores[it.id]
                    v.isNullOrBlank() || (v.replace(",", ".").toDoubleOrNull() ?: 0.0) == 0.0
                }
                if (emptyPerson != null) {
                    suggestedPessoaId = emptyPerson.id
                    suggestedValue = String.format(java.util.Locale.US, "%.2f", remainder)
                }
            }
            
            currentState.copy(
                valoresInput = novosValores,
                pessoaIdSugestao = suggestedPessoaId,
                valorSugestao = suggestedValue
            )
        }
    }
    
    fun aceitarSugestao() {
        _uiState.update { currentState ->
            val pessoaId = currentState.pessoaIdSugestao
            val valor = currentState.valorSugestao
            if (pessoaId != null && valor != null) {
                val novosValores = currentState.valoresInput.toMutableMap()
                novosValores[pessoaId] = valor
                currentState.copy(
                    valoresInput = novosValores,
                    pessoaIdSugestao = null,
                    valorSugestao = null
                )
            } else {
                currentState
            }
        }
    }
    
    fun dividirIgualmente() {
        _uiState.update { currentState ->
            val totalValue = currentState.valorTotalInput.replace(",", ".").toDoubleOrNull() ?: 0.0
            val count = currentState.pessoasNaDivida.size
            if (count > 0 && totalValue != 0.0) {
                val splitValue = totalValue / count
                val splitStr = String.format(java.util.Locale.US, "%.2f", splitValue)
                val novosValores = currentState.valoresInput.toMutableMap()
                currentState.pessoasNaDivida.forEach { pessoa ->
                    novosValores[pessoa.id] = splitStr
                }
                
                val sumParts = count * (String.format(java.util.Locale.US, "%.2f", splitValue).toDoubleOrNull() ?: 0.0)
                val diff = totalValue - sumParts
                if (Math.abs(diff) > 0.001) {
                    val firstId = currentState.pessoasNaDivida.first().id
                    val adjustedFirst = (String.format(java.util.Locale.US, "%.2f", splitValue).toDoubleOrNull() ?: 0.0) + diff
                    novosValores[firstId] = String.format(java.util.Locale.US, "%.2f", adjustedFirst)
                }
                
                currentState.copy(
                    valoresInput = novosValores,
                    pessoaIdSugestao = null,
                    valorSugestao = null
                )
            } else {
                currentState
            }
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
            if (input.isNotBlank()) {
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
                            categoriaId = currentState.categoriaIdSelecionada,
                            dataHora = currentState.dataHora
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
