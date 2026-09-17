package com.trilhacusto.ui.transacao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trilhacusto.data.local.entity.AtribuicaoTransacaoEntity
import com.trilhacusto.data.local.entity.CategoriaEntity
import com.trilhacusto.data.local.entity.TransacaoEntity
import com.trilhacusto.domain.repository.TransacaoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class TransacaoFormViewModel(
    private val repository: TransacaoRepository,
    private val pessoaDao: com.trilhacusto.data.local.dao.PessoaDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransacaoFormUiState())
    val uiState: StateFlow<TransacaoFormUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.buscarCategorias().collect { categorias ->
                _uiState.update { it.copy(categorias = categorias) }
            }
        }
        viewModelScope.launch {
            pessoaDao.getAll().collect { pessoas ->
                _uiState.update { it.copy(todasAsPessoas = pessoas) }
            }
        }
    }

    fun carregarTransacao(id: String?) {
        if (id == null) {
            _uiState.update { it.copy(isEditing = false, transacaoId = null) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val transacaoCompleta = repository.getTransacaoCompletaById(id)
                if (transacaoCompleta != null) {
                    val transacao = transacaoCompleta.transacao
                    
                    val mapValores = mutableMapOf<Long, String>()
                    val pessoasNaDivida = mutableListOf<com.trilhacusto.data.local.entity.PessoaEntity>()
                    transacaoCompleta.atribuicoes.forEach { atrib ->
                        mapValores[atrib.pessoa.id] = String.format(java.util.Locale.US, "%.2f", atrib.atribuicao.valorAtribuido)
                        pessoasNaDivida.add(atrib.pessoa)
                    }

                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            isEditing = true,
                            transacaoId = transacao.id,
                            descricaoInput = transacao.descricaoCustomizada ?: transacao.descricaoOriginal,
                            valorInput = transacao.valorTotal.toString(),
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
                _uiState.update { it.copy(isLoading = false, error = "Erro ao carregar: ${e.message}") }
            }
        }
    }

    fun atualizarDescricao(descricao: String) {
        _uiState.update { it.copy(descricaoInput = descricao) }
    }

    fun atualizarValor(valor: String) {
        _uiState.update { it.copy(valorInput = valor) }
    }

    fun atualizarDataHora(dataHora: Long) {
        _uiState.update { it.copy(dataHora = dataHora) }
    }

    fun atualizarCategoria(categoriaId: Long?) {
        _uiState.update { it.copy(categoriaIdSelecionada = categoriaId) }
    }

    fun atualizarParcelas(parcelas: String) {
        _uiState.update { it.copy(parcelasInput = parcelas) }
    }

    fun atualizarPessoaSugestao(pessoaId: Long?) {
        _uiState.update { it.copy(pessoaIdSugestao = pessoaId) }
    }

    fun atualizarValorSugestao(valor: String) {
        val filtrado = valor.filterIndexed { index, c -> c.isDigit() || c == '.' || c == ',' || (index == 0 && c == '-') }
        _uiState.update { it.copy(valorSugestao = filtrado) }
    }

    fun adicionarResponsavel(pessoaId: Long) {
        val state = _uiState.value
        val pessoa = state.todasAsPessoas.find { it.id == pessoaId } ?: return

        if (!state.pessoasNaDivida.any { it.id == pessoaId }) {
            _uiState.update { currentState ->
                val pessoasAtuais = currentState.pessoasNaDivida.toMutableList().apply { add(pessoa) }
                val novosValores = currentState.valoresInput.toMutableMap().apply { put(pessoaId, "") }
                currentState.copy(
                    pessoasNaDivida = pessoasAtuais,
                    valoresInput = novosValores
                )
            }
        }
    }

    fun removerResponsavel(pessoaId: Long) {
        val state = _uiState.value
        val novosValores = state.valoresInput.toMutableMap()
        novosValores.remove(pessoaId)
        
        val novasPessoas = state.pessoasNaDivida.filter { it.id != pessoaId }

        _uiState.update {
            it.copy(
                pessoasNaDivida = novasPessoas,
                valoresInput = novosValores
            )
        }
    }

    fun atualizarParteResponsavel(pessoaId: Long, valor: String) {
        val filtrado = valor.filterIndexed { index, c -> c.isDigit() || c == '.' || c == ',' || (index == 0 && c == '-') }
        _uiState.update { currentState ->
            val novosValores = currentState.valoresInput.toMutableMap()
            novosValores[pessoaId] = filtrado
            
            val totalValue = currentState.valorInput.replace(",", ".").toDoubleOrNull() ?: 0.0
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
                valorSugestao = suggestedValue ?: ""
            )
        }
    }

    fun aceitarSugestao() {
        _uiState.update { currentState ->
            val pessoaId = currentState.pessoaIdSugestao
            val valor = currentState.valorSugestao
            if (pessoaId != null && valor.isNotBlank()) {
                val novosValores = currentState.valoresInput.toMutableMap()
                novosValores[pessoaId] = valor
                currentState.copy(
                    valoresInput = novosValores,
                    pessoaIdSugestao = null,
                    valorSugestao = ""
                )
            } else {
                currentState
            }
        }
    }

    fun dividirIgualmente() {
        val state = _uiState.value
        val valorNumerico = state.valorInput.replace(",", ".").toDoubleOrNull() ?: 0.0
        val numPessoas = state.pessoasNaDivida.size
        
        if (numPessoas > 0 && valorNumerico > 0) {
            val valorPorPessoa = valorNumerico / numPessoas
            val valorFormatado = String.format(java.util.Locale.US, "%.2f", valorPorPessoa)
            
            val novosValores = state.valoresInput.toMutableMap()
            state.pessoasNaDivida.forEach { pessoa ->
                novosValores[pessoa.id] = valorFormatado
            }
            
            _uiState.update { it.copy(valoresInput = novosValores) }
        }
    }

    fun salvarTransacao() {
        val state = _uiState.value
        val valorNumerico = state.valorInput.replace(",", ".").toDoubleOrNull()
        val numParcelas = state.parcelasInput.toIntOrNull() ?: 1
        
        if (state.descricaoInput.isBlank() || valorNumerico == null || valorNumerico == 0.0 || numParcelas < 1) {
            _uiState.update { it.copy(error = "Preencha a descrição, valor (diferente de zero) e parcelas válidos.") }
            return
        }

        val somaAtribuicoes = state.valoresInput.values
            .mapNotNull { it.replace(",", ".").toDoubleOrNull() }
            .sum()

        if (somaAtribuicoes > valorNumerico + 0.01) {
            _uiState.update { it.copy(error = "A soma dos responsáveis (R$ $somaAtribuicoes) não pode ser maior que o valor da despesa (R$ $valorNumerico).") }
            return
        }

        val isRateado = kotlin.math.abs(somaAtribuicoes - valorNumerico) < 0.01
        val status = if (isRateado) "RATEADO" else "PENDENTE"

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                if (state.isEditing && state.transacaoId != null) {
                    val existente = repository.getTransacaoById(state.transacaoId)
                    if (existente != null) {
                        val transacaoAtualizada = existente.copy(
                            descricaoCustomizada = state.descricaoInput,
                            valorTotal = valorNumerico,
                            categoriaId = state.categoriaIdSelecionada,
                            dataHora = state.dataHora,
                            statusAtribuicao = status
                        )
                        
                        val novasAtribuicoes = state.pessoasNaDivida.mapNotNull { pessoa ->
                            val valorAtribuidoTotal = state.valoresInput[pessoa.id]?.replace(",", ".")?.toDoubleOrNull() ?: 0.0
                            if (valorAtribuidoTotal > 0) {
                                com.trilhacusto.data.local.entity.AtribuicaoTransacaoEntity(
                                    transacaoId = transacaoAtualizada.id,
                                    pessoaId = pessoa.id,
                                    valorAtribuido = valorAtribuidoTotal
                                )
                            } else null
                        }
                        
                        repository.atribuirRateio(state.transacaoId, transacaoAtualizada, novasAtribuicoes)
                    }
                } else {
                    val valorPorParcela = valorNumerico / numParcelas
                    
                    for (i in 1..numParcelas) {
                        val c = java.util.Calendar.getInstance()
                        c.timeInMillis = state.dataHora
                        c.add(java.util.Calendar.MONTH, i - 1)
                        
                        val novaTransacao = TransacaoEntity(
                            id = UUID.randomUUID().toString(),
                            descricaoOriginal = if (numParcelas > 1) "${state.descricaoInput} $i/$numParcelas" else state.descricaoInput,
                            valorTotal = valorPorParcela,
                            dataHora = c.timeInMillis,
                            categoriaId = state.categoriaIdSelecionada,
                            statusAtribuicao = status,
                            numeroParcela = if (numParcelas > 1) i else null,
                            totalParcelas = if (numParcelas > 1) numParcelas else null
                        )
                        
                        val novasAtribuicoes = state.pessoasNaDivida.mapNotNull { pessoa ->
                            val valorAtribuidoTotal = state.valoresInput[pessoa.id]?.replace(",", ".")?.toDoubleOrNull() ?: 0.0
                            if (valorAtribuidoTotal > 0) {
                                AtribuicaoTransacaoEntity(
                                    transacaoId = novaTransacao.id,
                                    pessoaId = pessoa.id,
                                    valorAtribuido = valorAtribuidoTotal / numParcelas
                                )
                            } else null
                        }

                        if (novasAtribuicoes.isNotEmpty()) {
                            repository.salvarTransacaoComAtribuicoes(novaTransacao, novasAtribuicoes)
                        } else {
                            repository.salvarTransacao(novaTransacao)
                        }
                    }
                }
                
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Erro ao salvar: ${e.message}") }
            }
        }
    }
    
    fun excluirTransacao() {
        val state = _uiState.value
        if (!state.isEditing || state.transacaoId == null) return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                repository.deletarTransacaoPorId(state.transacaoId)
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Erro ao excluir: ${e.message}") }
            }
        }
    }
    
    fun limparErro() {
        _uiState.update { it.copy(error = null) }
    }

    fun salvarCategoria(nome: String, corHex: String, id: Long? = null) {
        viewModelScope.launch {
            try {
                if (id == null) {
                    val novaCat = CategoriaEntity(nome = nome, corHex = corHex, icone = "")
                    repository.salvarCategoria(novaCat)
                } else {
                    val categoriaEditada = CategoriaEntity(id = id, nome = nome, corHex = corHex, icone = "")
                    repository.salvarCategoria(categoriaEditada)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Erro ao salvar categoria: ${e.message}") }
            }
        }
    }

    fun deletarCategoria(categoria: CategoriaEntity) {
        viewModelScope.launch {
            try {
                repository.deletarCategoria(categoria)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Erro ao excluir categoria: ${e.message}") }
            }
        }
    }
}
