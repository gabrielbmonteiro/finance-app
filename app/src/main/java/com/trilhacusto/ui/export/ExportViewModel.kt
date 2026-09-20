package com.trilhacusto.ui.export

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trilhacusto.data.local.dao.ConfiguracoesDao
import com.trilhacusto.data.local.dao.PessoaDao
import com.trilhacusto.data.local.dao.TransacaoDao
import com.trilhacusto.data.local.entity.PessoaEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.Calendar

enum class ExportFormat { PDF, JPG }

data class ExportUiState(
    val pessoas: List<PessoaEntity> = emptyList(),
    val pessoasSelecionadas: Set<Long> = emptySet(),
    val formato: ExportFormat = ExportFormat.PDF,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val exportSuccessUri: Uri? = null,
    val isShare: Boolean = false
)

class ExportViewModel(
    private val pessoaDao: PessoaDao,
    private val transacaoDao: TransacaoDao,
    private val configuracoesDao: ConfiguracoesDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExportUiState(isLoading = true))
    val uiState: StateFlow<ExportUiState> = _uiState.asStateFlow()

    init {
        carregarDados()
    }

    private fun carregarDados() {
        viewModelScope.launch {
            pessoaDao.getAll().collect { pessoas ->
                _uiState.value = _uiState.value.copy(
                    pessoas = pessoas,
                    isLoading = false
                )
            }
        }
    }

    fun togglePessoa(pessoaId: Long) {
        val current = _uiState.value.pessoasSelecionadas.toMutableSet()
        if (current.contains(pessoaId)) {
            current.remove(pessoaId)
        } else {
            current.add(pessoaId)
        }
        _uiState.value = _uiState.value.copy(pessoasSelecionadas = current)
    }
    
    fun selecionarTodasPessoas() {
        _uiState.value = _uiState.value.copy(pessoasSelecionadas = emptySet())
    }

    fun setFormato(formato: ExportFormat) {
        _uiState.value = _uiState.value.copy(formato = formato)
    }

    fun limparMensagem() {
        _uiState.value = _uiState.value.copy(errorMessage = null, exportSuccessUri = null)
    }

    fun exportar(context: Context, isShare: Boolean, targetUri: Uri? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null, exportSuccessUri = null, isShare = isShare)
            
            try {
                val config = configuracoesDao.getConfiguracoes().firstOrNull()
                val fechamento = config?.diaFechamento ?: 25
                val calendar = Calendar.getInstance()
                val mesSelecionado = calendar.get(Calendar.MONTH)
                val anoSelecionado = calendar.get(Calendar.YEAR)
                
                val c = Calendar.getInstance().apply {
                    set(Calendar.MONTH, mesSelecionado)
                    set(Calendar.YEAR, anoSelecionado)
                    set(Calendar.DAY_OF_MONTH, fechamento)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                
                val fimMes = c.timeInMillis - 1
                c.add(Calendar.MONTH, -1)
                val inicioMes = c.timeInMillis
                
                val pessoasSelecionadas = _uiState.value.pessoasSelecionadas
                
                val transacoes = if (pessoasSelecionadas.isEmpty()) {
                    transacaoDao.getTransacoesCompletasDoMes(inicioMes, fimMes)
                } else {
                    transacaoDao.getTransacoesCompletasFiltradasPorDataEPessoas(inicioMes, fimMes, pessoasSelecionadas.toList())
                }
                
                if (transacoes.isEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Nenhuma transação encontrada para este filtro."
                    )
                    return@launch
                }
                
                val todasPessoas = _uiState.value.pessoas
                
                val uri = InvoiceExportHelper.gerarArquivoExportacao(
                    context = context,
                    transacoes = transacoes,
                    todasPessoas = todasPessoas,
                    formato = _uiState.value.formato,
                    isShare = isShare,
                    pessoasSelecionadas = pessoasSelecionadas,
                    targetUri = targetUri
                )
                
                if (uri != null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        exportSuccessUri = uri
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Erro ao gerar arquivo."
                    )
                }
                
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Erro interno: ${e.message}"
                )
            }
        }
    }
}
