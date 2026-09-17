package com.trilhacusto

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.trilhacusto.ui.ajustes.categorias.CategoriaViewModel
import com.trilhacusto.ui.ajustes.categorias.CategoriasScreen
import com.trilhacusto.ui.ajustes.pessoas.PessoaViewModel
import com.trilhacusto.ui.ajustes.pessoas.PessoasScreen
import com.trilhacusto.ui.dashboard.DashboardScreen
import com.trilhacusto.ui.dashboard.DashboardViewModel
import com.trilhacusto.ui.export.ExportScreen
import com.trilhacusto.ui.export.ExportViewModel
import com.trilhacusto.ui.extrato.ExtratoScreen
import com.trilhacusto.ui.extrato.ExtratoViewModel
import com.trilhacusto.ui.pendencias.PendenciasScreen
import com.trilhacusto.ui.pendencias.PendenciasViewModel
import com.trilhacusto.ui.rateio.AtribuicaoEvent
import com.trilhacusto.ui.rateio.AtribuicaoViewModel
import com.trilhacusto.ui.rateio.RateioScreen
import com.trilhacusto.ui.theme.TrilhaCustoTheme
import com.trilhacusto.ui.transacao.TransacaoFormScreen
import com.trilhacusto.ui.transacao.TransacaoFormViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted.
        } else {
            // Explain to the user that the feature is unavailable.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        setContent {
            TrilhaCustoTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                colors = listOf(
                                    com.trilhacusto.ui.theme.BackgroundGradientStart,
                                    com.trilhacusto.ui.theme.BackgroundGradientEnd
                                )
                            )
                        )
                ) {
                    val navController = rememberNavController()
                    val context = LocalContext.current

                    NavHost(navController = navController, startDestination = "splash") {
                        
                        composable("splash") {
                            com.trilhacusto.ui.splash.SplashScreen(
                                onSplashFinished = {
                                    navController.navigate("dashboard") {
                                        popUpTo("splash") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("dashboard") {
                            val viewModel: DashboardViewModel = koinViewModel()
                            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                            DashboardScreen(
                                uiState = uiState,
                                onNavigateToPendencias = {
                                    navController.navigate("pendencias")
                                },
                                onSincronizarPluggy = {
                                    viewModel.sincronizarPluggy()
                                },
                                onTransacaoClick = { transacaoId ->
                                    navController.navigate("rateio/$transacaoId")
                                },
                                onNavigateToExtrato = { pessoaId ->
                                    if (pessoaId != null) {
                                        navController.navigate("extrato?pessoaId=$pessoaId")
                                    } else {
                                        navController.navigate("extrato")
                                    }
                                },
                                onMudarMes = { offset -> viewModel.mudarMes(offset) },
                                onSelecionarMesAno = { m, a -> viewModel.selecionarMesAno(m, a) },
                                onAbrirSelecaoMesAno = { viewModel.setShowMesAnoDialog(true) },
                                onFecharSelecaoMesAno = { viewModel.setShowMesAnoDialog(false) },
                                onAbrirConfiguracao = { viewModel.setShowConfigDialog(true) },
                                onFecharConfiguracao = { viewModel.setShowConfigDialog(false) },
                                onSalvarConfiguracao = { f, v -> viewModel.salvarConfiguracaoFatura(f, v) },
                                onTogglePrivacyMode = { viewModel.togglePrivacyMode() },
                                onNavigateToExport = { navController.navigate("export") }
                            )
                        }

                        composable("export") {
                            val viewModel: ExportViewModel = koinViewModel()
                            ExportScreen(
                                viewModel = viewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }


                        composable(
                            "extrato?pessoaId={pessoaId}",
                            arguments = listOf(navArgument("pessoaId") { nullable = true; type = NavType.StringType })
                        ) { backStackEntry ->
                            val pessoaIdStr = backStackEntry.arguments?.getString("pessoaId")
                            val viewModel: ExtratoViewModel = koinViewModel()
                            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                            
                            LaunchedEffect(pessoaIdStr) {
                                viewModel.initFilterFromRoute(pessoaIdStr?.toLongOrNull())
                            }

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
                                },
                                onLimparErro = { viewModel.limparErro() },
                                onTogglePrivacyMode = { viewModel.togglePrivacyMode() }
                            )
                        }
                        

                        composable("pendencias") {
                            val viewModel: PendenciasViewModel = koinViewModel()
                            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
                                },
                                onTogglePrivacyMode = { viewModel.togglePrivacyMode() }
                            )
                        }
                        

                        composable("rateio/{transacaoId}") { backStackEntry ->
                            val transacaoId = backStackEntry.arguments?.getString("transacaoId") ?: ""
                            val viewModel: AtribuicaoViewModel = koinViewModel()
                            val uiState by viewModel.uiState.collectAsStateWithLifecycle()


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
                                onDataHoraChanged = { viewModel.atualizarDataHora(it) },
                                onPessoaToggled = { viewModel.togglePessoaNaDivida(it) },
                                onClearError = { viewModel.clearError() },
                                onAddCategoria = { nome -> viewModel.adicionarCategoria(nome, "", "") },
                                onEditCategoria = { cat, nome -> viewModel.editarCategoria(cat, nome) },
                                onRemoveCategoria = { viewModel.deletarCategoria(it) },
                                onNavigateToPessoas = { navController.navigate("pessoas") },
                                onDividirIgualmente = { viewModel.dividirIgualmente() },
                                onAceitarSugestao = { viewModel.aceitarSugestao() },
                                onDropdownPessoaChanged = { viewModel.atualizarDropdownPessoa(it) },
                                onDropdownValorChanged = { viewModel.atualizarDropdownValor(it) },
                                onAdicionarResponsavelDropdown = { id -> viewModel.adicionarResponsavelDropdown(id) },
                                onRemoverResponsavelDropdown = { viewModel.removerResponsavelDropdown(it) }
                            )
                        }
                        

                        composable("transacao/nova") {
                            val viewModel: TransacaoFormViewModel = koinViewModel()
                            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
                                onDataHoraChanged = { viewModel.atualizarDataHora(it) },
                                onSalvar = { viewModel.salvarTransacao() },
                                onExcluir = { viewModel.excluirTransacao() },
                                onLimparErro = { viewModel.limparErro() },
                                onSalvarCategoria = { nome, cor, id -> viewModel.salvarCategoria(nome, cor, id) },
                                onDeletarCategoria = { viewModel.deletarCategoria(it) },
                                onPessoaSugestaoChanged = { viewModel.atualizarPessoaSugestao(it) },
                                onValorSugestaoChanged = { viewModel.atualizarValorSugestao(it) },
                                onAdicionarResponsavel = { id -> viewModel.adicionarResponsavel(id) },
                                onRemoverResponsavel = { viewModel.removerResponsavel(it) },
                                onParteChanged = { id, valor -> viewModel.atualizarParteResponsavel(id, valor) },
                                onDividirIgualmente = { viewModel.dividirIgualmente() },
                                onNavigateToPessoas = { navController.navigate("pessoas") },
                                onAceitarSugestao = { viewModel.aceitarSugestao() }
                            )
                        }

                        composable("transacao/edit/{id}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id")
                            val viewModel: TransacaoFormViewModel = koinViewModel()
                            
                            LaunchedEffect(id) {
                                viewModel.carregarTransacao(id)
                            }
                            
                            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                            TransacaoFormScreen(
                                uiState = uiState,
                                onBackClicked = { navController.popBackStack() },
                                onDescricaoChanged = { viewModel.atualizarDescricao(it) },
                                onValorChanged = { viewModel.atualizarValor(it) },
                                onParcelasChanged = { viewModel.atualizarParcelas(it) },
                                onCategoriaChanged = { viewModel.atualizarCategoria(it) },
                                onDataHoraChanged = { viewModel.atualizarDataHora(it) },
                                onSalvar = { viewModel.salvarTransacao() },
                                onExcluir = { viewModel.excluirTransacao() },
                                onLimparErro = { viewModel.limparErro() },
                                onSalvarCategoria = { nome, cor, id -> viewModel.salvarCategoria(nome, cor, id) },
                                onDeletarCategoria = { viewModel.deletarCategoria(it) },
                                onPessoaSugestaoChanged = { viewModel.atualizarPessoaSugestao(it) },
                                onValorSugestaoChanged = { viewModel.atualizarValorSugestao(it) },
                                onAdicionarResponsavel = { id -> viewModel.adicionarResponsavel(id) },
                                onRemoverResponsavel = { viewModel.removerResponsavel(it) },
                                onParteChanged = { id, valor -> viewModel.atualizarParteResponsavel(id, valor) },
                                onDividirIgualmente = { viewModel.dividirIgualmente() },
                                onNavigateToPessoas = { navController.navigate("pessoas") },
                                onAceitarSugestao = { viewModel.aceitarSugestao() }
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
