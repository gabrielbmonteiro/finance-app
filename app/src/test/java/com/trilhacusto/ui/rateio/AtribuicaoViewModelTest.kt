package com.trilhacusto.ui.rateio

import com.trilhacusto.data.local.dao.CategoriaDao
import com.trilhacusto.data.local.dao.PessoaDao
import com.trilhacusto.data.local.dao.TransacaoDao
import com.trilhacusto.data.local.entity.PessoaEntity
import com.trilhacusto.data.local.entity.TransacaoEntity
import com.trilhacusto.data.local.relation.TransacaoCompleta
import com.trilhacusto.domain.usecase.AtribuirValoresUseCase
import io.mockk.coEvery
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AtribuicaoViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val transacaoDao: TransacaoDao = mockk(relaxed = true)
    private val pessoaDao: PessoaDao = mockk(relaxed = true)
    private val categoriaDao: CategoriaDao = mockk(relaxed = true)
    private val atribuirValoresUseCase: AtribuirValoresUseCase = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun deve_adicionar_pessoas_favoritas_automaticamente_em_pendencia_a_ratear() = runTest(testDispatcher) {
        val transacaoPendente = TransacaoCompleta(
            transacao = TransacaoEntity(
                id = "tx_pendente_1",
                valorTotal = 150.0,
                descricaoOriginal = "Restaurante",
                dataHora = 123456L,
                statusAtribuicao = "PENDENTE"
            ),
            categoria = null,
            atribuicoes = emptyList() // Sem atribuições ainda
        )

        val favoritos = listOf(
            PessoaEntity(id = 1L, nome = "Gabriel", corHex = "#FF0000", isFavorito = true),
            PessoaEntity(id = 2L, nome = "Pai", corHex = "#00FF00", isFavorito = true)
        )

        every { categoriaDao.getAll() } returns flowOf(emptyList())
        every { pessoaDao.getAll() } returns flowOf(favoritos)
        coEvery { transacaoDao.getTransacaoCompletaById("tx_pendente_1") } returns transacaoPendente
        coEvery { pessoaDao.getFavoritos() } returns favoritos

        val viewModel = AtribuicaoViewModel(
            transacaoDao = transacaoDao,
            pessoaDao = pessoaDao,
            categoriaDao = categoriaDao,
            atribuirValoresUseCase = atribuirValoresUseCase
        )
        advanceUntilIdle()

        viewModel.carregarTransacao("tx_pendente_1")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(2, state.pessoasNaDivida.size)
        assertTrue(state.pessoasNaDivida.any { it.id == 1L })
        assertTrue(state.pessoasNaDivida.any { it.id == 2L })
    }
}
