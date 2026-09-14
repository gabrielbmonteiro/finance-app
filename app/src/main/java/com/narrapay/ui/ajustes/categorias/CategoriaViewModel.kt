package com.narrapay.ui.ajustes.categorias

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.narrapay.data.local.dao.CategoriaDao
import com.narrapay.data.local.entity.CategoriaEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoriaViewModel(
    private val categoriaDao: CategoriaDao
) : ViewModel() {

    val categorias: StateFlow<List<CategoriaEntity>> = categoriaDao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun salvarCategoria(categoria: CategoriaEntity) {
        viewModelScope.launch {
            if (categoria.id == 0L) {
                categoriaDao.insert(categoria)
            } else {
                categoriaDao.update(categoria)
            }
        }
    }

    fun excluirCategoria(categoria: CategoriaEntity) {
        viewModelScope.launch {
            categoriaDao.delete(categoria)
        }
    }
}
