package com.narrapay

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.narrapay.ui.dashboard.DashboardScreen
import com.narrapay.ui.dashboard.DashboardViewModel
import com.narrapay.ui.pendencias.PendenciasScreen
import com.narrapay.ui.pendencias.PendenciasViewModel
import com.narrapay.ui.rateio.AtribuicaoEvent
import com.narrapay.ui.rateio.AtribuicaoViewModel
import com.narrapay.ui.rateio.RateioScreen
import com.narrapay.ui.theme.NarrapayTheme
import com.narrapay.ui.transacao.TransacaoFormScreen
import com.narrapay.ui.transacao.TransacaoFormViewModel
import com.narrapay.ui.extrato.ExtratoScreen
import com.narrapay.ui.extrato.ExtratoViewModel
import com.narrapay.ui.ajustes.categorias.CategoriasScreen
import com.narrapay.ui.ajustes.categorias.CategoriaViewModel
import com.narrapay.ui.ajustes.pessoas.PessoasScreen
import com.narrapay.ui.ajustes.pessoas.PessoaViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NarrapayTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val context = LocalContext.current

                    NavHost(navController = navController, startDestination = "dashboard") {
                        

                        composable("dashboard") {
                            val viewModel: DashboardViewModel = koinViewModel()
                            val uiState by viewModel.uiState.collectAsState()

                            DashboardScreen(
                                uiState = uiState,
                                onNavigateToPendencias = {
                                    navController.navigate("pendencias")
                                },
                                onSincronizarPluggy = {
                                    Toast.makeText(context, "Sincronizando com a Pluggy...", Toast.LENGTH_SHORT).show()
                                    viewModel.sincronizarPluggy()
                                },
                                onTransacaoClick = { transacaoId ->
                                    navController.navigate("rateio/$transacaoId")
                                },
                                onNavigateToExtrato = {
                                    navController.navigate("extrato")
                                },
                                onMudarMes = { offset -> viewModel.mudarMes(offset) },
                                onSelecionarMesAno = { m, a -> viewModel.selecionarMesAno(m, a) },
                                onAbrirSelecaoMesAno = { viewModel.setShowMesAnoDialog(true) },
                                onFecharSelecaoMesAno = { viewModel.setShowMesAnoDialog(false) },
                                onAbrirConfiguracao = { viewModel.setShowConfigDialog(true) },
                                onFecharConfiguracao = { viewModel.setShowConfigDialog(false) },
                                onSalvarConfiguracao = { f, v -> viewModel.salvarConfiguracaoFatura(f, v) }
                            )
                        }


                        composable("extrato") {
                            val viewModel: ExtratoViewModel = koinViewModel()
                            val uiState by viewModel.uiState.collectAsState()

                            ExtratoScreen(
                                uiState = uiState,
                                onBuscaChanged = { viewModel.atualizarBusca(it) },
                                onFiltroResponsavelChanged = { viewModel.atualizarResponsavel(it) },
                                onTransacaoClick = { transacaoId ->
                                    navController.navigate("rateio/$transacaoId")
                                },
                                onExcluirClick = { transacaoId ->
                                    viewModel.excluirTransacao(transacaoId)
                                },
                                onNavigateToHome = {
                                    navController.popBackStack("dashboard", inclusive = false)
                                },
                                onNavigateToPendencias = {
                                    navController.navigate("pendencias") {
                                        popUpTo("dashboard") { inclusive = false }
                                    }
                                }
                            )
                        }
                        

                        composable("pendencias") {
                            val viewModel: PendenciasViewModel = koinViewModel()
                            val uiState by viewModel.uiState.collectAsState()

                            PendenciasScreen(
                                uiState = uiState,
                                onBuscaChanged = { viewModel.atualizarBusca(it) },
                                onFilterSelected = { filtro ->
                                    viewModel.setFiltro(filtro)
                                },
                                onDividirClicked = { transacaoId ->
                                    navController.navigate("rateio/$transacaoId")
                                },
                                onExcluirClicked = { transacaoId ->
                                    viewModel.excluirTransacao(transacaoId)
                                },
                                onIgnorarClicked = { transacaoId ->
                                    viewModel.ignorarTransacao(transacaoId)
                                },
                                onReativarClicked = { transacaoId ->
                                    viewModel.reativarTransacao(transacaoId)
                                },
                                onNavigateToHome = {
                                    navController.popBackStack("dashboard", inclusive = false)
                                },
                                onNavigateToNovaDespesa = {
                                    navController.navigate("transacao/nova")
                                },
                                onNavigateToEditar = { transacaoId ->
                                    navController.navigate("transacao/edit/$transacaoId")
                                },
                                onNavigateToExtrato = {
                                    navController.navigate("extrato") {
                                        popUpTo("dashboard") { inclusive = false }
                                    }
                                },
                                onNavigateToCategorias = {
                                    navController.navigate("categorias")
                                },
                                onNavigateToPessoas = {
                                    navController.navigate("pessoas")
                                }
                            )
                        }
                        

                        composable("rateio/{transacaoId}") { backStackEntry ->
                            val transacaoId = backStackEntry.arguments?.getString("transacaoId") ?: ""
                            val viewModel: AtribuicaoViewModel = koinViewModel()
                            val uiState by viewModel.uiState.collectAsState()


                            LaunchedEffect(transacaoId) {
                                viewModel.carregarTransacao(transacaoId)
                            }
                            

                            LaunchedEffect(Unit) {
                                viewModel.eventos.collect { event ->
                                    when(event) {
                                        is AtribuicaoEvent.Sucesso -> {
                                            Toast.makeText(context, "Rateio salvo com sucesso!", Toast.LENGTH_SHORT).show()
                                            navController.popBackStack()
                                        }
                                    }
                                }
                            }

                            RateioScreen(
                                uiState = uiState,
                                onBackClicked = { navController.popBackStack() },
                                onConfirmarRateio = { viewModel.confirmarRateio() },
                                onParteChanged = { pessoaId, novoValor -> viewModel.atualizarParte(pessoaId, novoValor) },
                                onTituloChanged = { viewModel.atualizarTitulo(it) },
                                onValorTotalChanged = { viewModel.atualizarValorTotal(it) },
                                onCategoriaSelecionada = { viewModel.atualizarCategoria(it) },
                                onPessoaToggled = { viewModel.togglePessoaNaDivida(it) },
                                onClearError = { viewModel.clearError() },
                                onAddCategoria = { nome -> viewModel.adicionarCategoria(nome, "", "") },
                                onEditCategoria = { cat, nome -> viewModel.editarCategoria(cat, nome) },
                                onRemoveCategoria = { viewModel.deletarCategoria(it) },
                                onNavigateToPessoas = { navController.navigate("pessoas") }
                            )
                        }
                        

                        composable("transacao/nova") {
                            val viewModel: TransacaoFormViewModel = koinViewModel()
                            val uiState by viewModel.uiState.collectAsState()

                            LaunchedEffect(Unit) {
                                viewModel.carregarTransacao(null)
                            }

                            TransacaoFormScreen(
                                uiState = uiState,
                                onBackClicked = { navController.popBackStack() },
                                onDescricaoChanged = { viewModel.atualizarDescricao(it) },
                                onValorChanged = { viewModel.atualizarValor(it) },
                                onParcelasChanged = { viewModel.atualizarParcelas(it) },
                                onCategoriaChanged = { viewModel.atualizarCategoria(it) },
                                onSalvar = { viewModel.salvarTransacao() },
                                onExcluir = { viewModel.excluirTransacao() },
                                onLimparErro = { viewModel.limparErro() }
                            )
                        }

                        composable("transacao/edit/{id}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id")
                            val viewModel: TransacaoFormViewModel = koinViewModel()
                            
                            LaunchedEffect(id) {
                                viewModel.carregarTransacao(id)
                            }
                            
                            val uiState by viewModel.uiState.collectAsState()

                            TransacaoFormScreen(
                                uiState = uiState,
                                onBackClicked = { navController.popBackStack() },
                                onDescricaoChanged = { viewModel.atualizarDescricao(it) },
                                onValorChanged = { viewModel.atualizarValor(it) },
                                onParcelasChanged = { viewModel.atualizarParcelas(it) },
                                onCategoriaChanged = { viewModel.atualizarCategoria(it) },
                                onSalvar = { viewModel.salvarTransacao() },
                                onExcluir = { viewModel.excluirTransacao() },
                                onLimparErro = { viewModel.limparErro() }
                            )
                        }
                        

                        composable("categorias") {
                            val viewModel: CategoriaViewModel = koinViewModel()
                            CategoriasScreen(
                                viewModel = viewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        composable("pessoas") {
                            val viewModel: PessoaViewModel = koinViewModel()
                            PessoasScreen(
                                viewModel = viewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
