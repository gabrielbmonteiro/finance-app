package com.trilhacusto.ui.ajustes.categorias

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trilhacusto.data.local.dao.CategoriaDao
import com.trilhacusto.data.local.entity.CategoriaEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoriaViewModel(
    private val categoriaDao: CategoriaDao
) : ViewModel() {

    val categorias: StateFlow<List<CategoriaEntity>> = categoriaDao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _error = kotlinx.coroutines.flow.MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun clearError() { _error.value = null }

    fun salvarCategoria(categoria: CategoriaEntity) {
        viewModelScope.launch {
            try {
                if (categoria.id == 0L) {
                    categoriaDao.insert(categoria)
                } else {
                    categoriaDao.update(categoria)
                }
            } catch (e: Exception) {
                _error.value = "Erro ao salvar categoria: ${e.message}"
            }
        }
    }

    fun excluirCategoria(categoria: CategoriaEntity) {
        viewModelScope.launch {
            try {
                categoriaDao.delete(categoria)
            } catch (e: Exception) {
                _error.value = "Erro ao excluir categoria: ${e.message}"
            }
        }
    }
}
