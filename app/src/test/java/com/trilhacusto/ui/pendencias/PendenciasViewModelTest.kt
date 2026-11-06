package com.trilhacusto.ui.pendencias

import com.trilhacusto.data.local.dao.ConfiguracoesDao
import com.trilhacusto.data.local.dao.TransacaoDao
import com.trilhacusto.data.local.entity.ConfiguracoesEntity
import com.trilhacusto.data.local.entity.TransacaoEntity
import com.trilhacusto.data.local.relation.TransacaoCompleta
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PendenciasViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val transacaoDao: TransacaoDao = mockk(relaxed = true)
    private val configuracoesDao: ConfiguracoesDao = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun deve_separar_transacoes_pendentes_e_excluidas_corretamente() = runTest(testDispatcher) {
        val transacaoPendente = TransacaoCompleta(
            transacao = TransacaoEntity(
                id = "tx_1",
                valorTotal = 100.0,
                descricaoOriginal = "Supermercado",
                dataHora = 1000L,
                statusAtribuicao = "PENDENTE"
            ),
            categoria = null,
            atribuicoes = emptyList()
        )

        val transacaoExcluida = TransacaoCompleta(
            transacao = TransacaoEntity(
                id = "tx_2",
                valorTotal = 50.0,
                descricaoOriginal = "Farmacia",
                dataHora = 2000L,
                statusAtribuicao = "IGNORADO"
            ),
            categoria = null,
            atribuicoes = emptyList()
        )

        every { transacaoDao.getTransacoesPendentes() } returns flowOf(listOf(transacaoPendente, transacaoExcluida))
        every { configuracoesDao.getConfiguracoes() } returns flowOf(ConfiguracoesEntity(id = 1))

        val viewModel = PendenciasViewModel(transacaoDao, configuracoesDao)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.transacoesPendentes.size)
        assertEquals("tx_1", state.transacoesPendentes[0].transacao.id)

        assertEquals(1, state.transacoesExcluidas.size)
        assertEquals("tx_2", state.transacoesExcluidas[0].transacao.id)
    }

    @Test
    fun deve_mover_para_excluidas_ao_chamar_excluirTransacao() = runTest(testDispatcher) {
        val transacao = TransacaoEntity(
            id = "tx_1",
            valorTotal = 100.0,
            descricaoOriginal = "Supermercado",
            dataHora = 1000L,
            statusAtribuicao = "PENDENTE"
        )

        every { transacaoDao.getTransacoesPendentes() } returns flowOf(emptyList())
        every { configuracoesDao.getConfiguracoes() } returns flowOf(null)
        coEvery { transacaoDao.getTransacaoById("tx_1") } returns transacao

        val viewModel = PendenciasViewModel(transacaoDao, configuracoesDao)
        advanceUntilIdle()

        viewModel.excluirTransacao("tx_1")
        advanceUntilIdle()

        coVerify {
            transacaoDao.updateTransacao(match { it.id == "tx_1" && it.statusAtribuicao == "IGNORADO" })
        }
        assertEquals("Pendência movida para excluídas", viewModel.uiState.value.mensagemSucesso)
    }

    @Test
    fun deve_reativar_transacao_retirando_da_exclusao() = runTest(testDispatcher) {
        val transacaoExcluida = TransacaoEntity(
            id = "tx_2",
            valorTotal = 50.0,
            descricaoOriginal = "Farmacia",
            dataHora = 2000L,
            statusAtribuicao = "IGNORADO"
        )

        every { transacaoDao.getTransacoesPendentes() } returns flowOf(emptyList())
        every { configuracoesDao.getConfiguracoes() } returns flowOf(null)
        coEvery { transacaoDao.getTransacaoById("tx_2") } returns transacaoExcluida

        val viewModel = PendenciasViewModel(transacaoDao, configuracoesDao)
        advanceUntilIdle()

        viewModel.reativarTransacao("tx_2")
        advanceUntilIdle()

        coVerify {
            transacaoDao.updateTransacao(match { it.id == "tx_2" && it.statusAtribuicao == "PENDENTE" })
        }
        assertEquals("Pendência retirada da exclusão!", viewModel.uiState.value.mensagemSucesso)
    }

    @Test
    fun deve_excluir_definitivamente_do_banco() = runTest(testDispatcher) {
        every { transacaoDao.getTransacoesPendentes() } returns flowOf(emptyList())
        every { configuracoesDao.getConfiguracoes() } returns flowOf(null)

        val viewModel = PendenciasViewModel(transacaoDao, configuracoesDao)
        advanceUntilIdle()

        viewModel.excluirDefinitivamente("tx_999")
        advanceUntilIdle()

        coVerify {
            transacaoDao.deleteTransacaoById("tx_999")
        }
        assertEquals("Pendência excluída permanentemente", viewModel.uiState.value.mensagemSucesso)
    }
}
