package com.narrapay.ui.ajustes.pessoas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.narrapay.data.local.dao.PessoaDao
import com.narrapay.data.local.entity.PessoaEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PessoaViewModel(
    private val pessoaDao: PessoaDao
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

    fun excluirPessoa(pessoa: PessoaEntity) {
        viewModelScope.launch {
            pessoaDao.delete(pessoa)
        }
    }
}
