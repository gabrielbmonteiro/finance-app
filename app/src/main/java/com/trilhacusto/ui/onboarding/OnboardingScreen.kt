package com.trilhacusto.ui.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.trilhacusto.R
import com.trilhacusto.ui.components.GlassCard
import com.trilhacusto.ui.components.GlassTextField
import com.trilhacusto.ui.components.GlassButton
import com.trilhacusto.ui.theme.PrimaryAccent
import kotlinx.coroutines.launch

data class OnboardingStepData(
    val title: String,
    val text: String,
    val imageResId: Int?,
    val actionText: String? = null,
    val actionUrl: String? = null
)

val onboardingSteps = listOf(
    OnboardingStepData(
        title = "Cadastro Meu Pluggy",
        text = "Acesse a plataforma Meu Pluggy e realize o seu cadastro para iniciar o processo de conexão.",
        actionText = "Acessar Meu Pluggy",
        actionUrl = "https://my-pluggy.us.auth0.com/login?state=hKFo2SBUNzA2LXNtcHFEOERwcEM3cGh6Qms3ZnVlOTNzU3BYcKFupWxvZ2luo3RpZNkgN3d6ODlNT1dGVTlNT0l6ZnZaZmFYUVMwVTNScXZKVWujY2lk2SBXelZVN0pCUlVWZHJHYko1STB6MkpxTjVJemd3WjMzTg&client=WzVU7JBRUVdrGbJ5I0z2JqN5IzgwZ33N&protocol=oauth2&scope=openid%20profile%20email&response_type=code&redirect_uri=https%3A%2F%2Fmeu.pluggy.ai%2Fapi%2Fauth%2Fcallback&audience=https%3A%2F%2Fmy-api.pluggy.ai&nonce=DaKLooJHsIbb3hxaJ33WGfTJbEI5EK6yMylxOiwU2hU&code_challenge_method=S256&code_challenge=avG9EXxPccaQ5sLusI97f8RQ-Ud6660XoeF6P7wCAwY",
        imageResId = R.drawable.pluggy_step_1
    ),
    OnboardingStepData(
        title = "Dashboard Meu Pluggy",
        text = "Esta é a tela inicial que aparecerá após o login ser efetuado com sucesso.",
        imageResId = R.drawable.pluggy_step_2
    ),
    OnboardingStepData(
        title = "Conectar Conta Bancária",
        text = "Clique em \"Conectar minha conta\", selecione a sua instituição financeira (banco) que deseja monitorar e preencha os dados de acesso solicitados.",
        imageResId = R.drawable.pluggy_step_3
    ),
    OnboardingStepData(
        title = "Cadastro Dashboard Pluggy",
        text = "Agora, precisaremos criar uma conta de desenvolvedor no Pluggy Dashboard para gerar as credenciais de acesso.",
        actionText = "Criar conta no Dashboard",
        actionUrl = "https://pluggy.auth0.com/u/signup?state=hKFo2SA1WFNVTm11bkpiU1EwTVlBT25hN29UbG50di1CSjd0WKFur3VuaXZlcnNhbC1sb2dpbqN0aWTZIGlXRDlOQ29ISmFnMkdWb0NqcV9mOWNYaC0zaXRpdHpMo2NpZNkgWlpCWHBWY2lGSDU1Mm5vZFBNNFN6U3VQTE5EaTJobUM",
        imageResId = R.drawable.pluggy_step_4
    ),
    OnboardingStepData(
        title = "Boas-vindas Dashboard",
        text = "Após finalizar o cadastro, clique em \"Começar\" para acessar seu painel.",
        imageResId = R.drawable.pluggy_step_5
    ),
    OnboardingStepData(
        title = "Pular Configuração Inicial",
        text = "Na próxima tela, você pode clicar em \"Volto depois\" para pular o guia inicial.",
        imageResId = R.drawable.pluggy_step_6
    ),
    OnboardingStepData(
        title = "Conexões do Dashboard",
        text = "No seu painel de testes, localize a aba ou botão e clique em \"Conectar\".",
        imageResId = R.drawable.pluggy_step_7
    ),
    OnboardingStepData(
        title = "Iniciar Conexão",
        text = "Em seguida, clique no botão \"Conectar Conta\" para iniciar o vínculo.",
        imageResId = R.drawable.pluggy_step_8
    ),
    OnboardingStepData(
        title = "Selecionar Fonte MeuPluggy",
        text = "Na lista de instituições, escolha a opção \"MeuPluggy\". Isso irá conectar nossa aplicação ao serviço que monitora seus bancos.",
        imageResId = R.drawable.pluggy_step_9
    ),
    OnboardingStepData(
        title = "Copiar Item ID",
        text = "Após a conexão ser estabelecida, clique na nova conexão criada (1) e em seguida copie o valor do \"Item ID\" (2). Você precisará dele logo mais.",
        imageResId = R.drawable.pluggy_step_10
    ),
    OnboardingStepData(
        title = "Acessar Menu",
        text = "Agora precisamos das chaves da API. Clique no ícone superior para acessar o menu do Dashboard.",
        imageResId = R.drawable.pluggy_step_11
    ),
    OnboardingStepData(
        title = "Acessar Aplicações",
        text = "Com o menu aberto, clique na seção \"Aplicações\" para ver seu aplicativo padrão do Pluggy.",
        imageResId = R.drawable.pluggy_step_12
    ),
    OnboardingStepData(
        title = "Configurações da Aplicação",
        text = "Clique no ícone de engrenagem ao lado da sua aplicação para abrir as configurações e detalhes.",
        imageResId = R.drawable.pluggy_step_13
    ),
    OnboardingStepData(
        title = "Copiar Credenciais",
        text = "Por fim, copie o \"Client ID\" (1) e o \"Client Secret\" (2). Salve eles com segurança, pois usaremos na próxima tela!",
        imageResId = R.drawable.pluggy_step_14
    )
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel,
    onNavigateToDashboard: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val totalPages = onboardingSteps.size + 1 // +1 for the final form
    val pagerState = rememberPagerState(pageCount = { totalPages })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        containerColor = Color.Transparent
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp, top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = onNavigateBack
                ) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.Close,
                        contentDescription = "Fechar passo a passo",
                        tint = Color.White
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                if (page < onboardingSteps.size) {
                    OnboardingStepView(onboardingSteps[page])
                } else {
                    CredentialsStep(viewModel, onNavigateToDashboard)
                }
            }

            // Bottom Navigation and Indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back / Skip button
                if (pagerState.currentPage > 0) {
                    TextButton(onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }) {
                        Text("Voltar", color = Color.LightGray)
                    }
                } else {
                    TextButton(onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(totalPages - 1) // Skip to form
                        }
                    }) {
                        Text("Pular", color = Color.Gray)
                    }
                }

                // Page Indicator (using LazyRow or wrapping to avoid overflow)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    val start = maxOf(0, pagerState.currentPage - 2)
                    val end = minOf(totalPages - 1, start + 4) // Show at most 5 dots to prevent overflow
                    
                    if (start > 0) {
                        Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(Color.Gray.copy(alpha = 0.3f)))
                    }
                    
                    for (iteration in start..end) {
                        val color = if (pagerState.currentPage == iteration) PrimaryAccent else Color.Gray.copy(alpha = 0.5f)
                        val width = if (pagerState.currentPage == iteration) 16.dp else 6.dp
                        Box(
                            modifier = Modifier
                                .height(6.dp)
                                .width(width)
                                .clip(CircleShape)
                                .background(color)
                        )
                    }
                    
                    if (end < totalPages - 1) {
                        Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(Color.Gray.copy(alpha = 0.3f)))
                    }
                }

                // Next Button
                if (pagerState.currentPage < totalPages - 1) {
                    TextButton(onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }) {
                        Text("Próximo", color = PrimaryAccent, fontWeight = FontWeight.Bold)
                    }
                } else {
                    // Placeholder for spacing to keep indicator centered
                    Spacer(modifier = Modifier.width(72.dp)) 
                }
            }
        }
    }
}

@Composable
fun OnboardingStepView(step: OnboardingStepData) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = step.title,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = step.text,
            color = Color.LightGray,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            textAlign = TextAlign.Center
        )
        
        if (step.actionText != null && step.actionUrl != null) {
            Spacer(modifier = Modifier.height(24.dp))
            GlassButton(
                text = step.actionText,
                onClick = { uriHandler.openUri(step.actionUrl) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        if (step.imageResId != null) {
            Spacer(modifier = Modifier.height(32.dp))
            GlassCard {
                Image(
                    painter = painterResource(id = step.imageResId),
                    contentDescription = step.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }
}

@Composable
fun CredentialsStep(
    viewModel: OnboardingViewModel,
    onNavigateToDashboard: () -> Unit
) {
    val clientId by viewModel.clientId.collectAsStateWithLifecycle()
    val clientSecret by viewModel.clientSecret.collectAsStateWithLifecycle()
    val accountId by viewModel.accountId.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Tudo Pronto!",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Agora só falta inserir as credenciais da sua Aplicação no Dashboard e o Item ID da conta conectada.",
            color = Color.LightGray,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))

        GlassTextField(
            value = clientId,
            onValueChange = { viewModel.updateClientId(it) },
            label = { Text("Client ID") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(16.dp))

        GlassTextField(
            value = clientSecret,
            onValueChange = { viewModel.updateClientSecret(it) },
            label = { Text("Client Secret") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(16.dp))

        GlassTextField(
            value = accountId,
            onValueChange = { viewModel.updateAccountId(it) },
            label = { Text("Item ID") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
        )

        Spacer(modifier = Modifier.height(48.dp))

        GlassButton(
            text = "Salvar e Entrar",
            onClick = {
                viewModel.saveCredentials(onComplete = onNavigateToDashboard)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = clientId.isNotBlank() && clientSecret.isNotBlank() && accountId.isNotBlank()
        )
    }
}
