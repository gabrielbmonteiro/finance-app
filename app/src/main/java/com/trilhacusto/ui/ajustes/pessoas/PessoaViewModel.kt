package com.trilhacusto.ui.ajustes.pessoas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trilhacusto.data.local.dao.PessoaDao
import com.trilhacusto.data.local.entity.PessoaEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PessoaViewModel(
    private val pessoaDao: PessoaDao,
    private val transacaoRepository: com.trilhacusto.domain.repository.TransacaoRepository
) : ViewModel() {

    val pessoas: StateFlow<List<PessoaEntity>> = pessoaDao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun salvarPessoa(pessoa: PessoaEntity) {
        viewModelScope.launch {
            if (pessoa.id == 0L) {
                pessoaDao.insert(pessoa)
            } else {
                pessoaDao.update(pessoa)
            }
        }
    }

    suspend fun checkHasDebts(pessoaId: Long): Boolean {
        return try {
            transacaoRepository.countAtribuicoesDaPessoa(pessoaId) > 0
        } catch (e: Exception) {
            false
        }
    }

    fun excluirPessoaComTratamento(pessoa: PessoaEntity) {
        viewModelScope.launch {
            try {
                transacaoRepository.reverterTransacoesDaPessoa(pessoa.id)
                pessoaDao.delete(pessoa)
            } catch (e: Exception) {
                // Em um app real, poderíamos emitir um erro via fluxo
                e.printStackTrace()
            }
        }
    }
}
