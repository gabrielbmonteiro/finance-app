package com.trilhacusto.ui.ajustes.pessoas

import com.trilhacusto.data.local.dao.PessoaDao
import com.trilhacusto.data.local.entity.PessoaEntity
import com.trilhacusto.domain.repository.TransacaoRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PessoaViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val pessoaDao: PessoaDao = mockk(relaxed = true)
    private val transacaoRepository: TransacaoRepository = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun deve_alternar_favorito_ao_chamar_toggleFavorito() = runTest(testDispatcher) {
        val pessoa = PessoaEntity(id = 1L, nome = "Gabriel", corHex = "#FF0000", isFavorito = false)
        every { pessoaDao.getAll() } returns flowOf(listOf(pessoa))

        val viewModel = PessoaViewModel(pessoaDao, transacaoRepository)
        advanceUntilIdle()

        viewModel.toggleFavorito(pessoa)
        advanceUntilIdle()

        coVerify {
            pessoaDao.updateFavorito(1L, true)
        }
    }

    @Test
    fun deve_salvar_pessoa_com_isFavorito() = runTest(testDispatcher) {
        val pessoa = PessoaEntity(id = 0L, nome = "Pai", corHex = "#00FF00", isFavorito = true)
        every { pessoaDao.getAll() } returns flowOf(emptyList())

        val viewModel = PessoaViewModel(pessoaDao, transacaoRepository)
        advanceUntilIdle()

        viewModel.salvarPessoa(pessoa)
        advanceUntilIdle()

        coVerify {
            pessoaDao.insert(match { it.nome == "Pai" && it.isFavorito })
        }
    }
}
