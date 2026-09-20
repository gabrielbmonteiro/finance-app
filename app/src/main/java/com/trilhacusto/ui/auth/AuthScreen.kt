package com.trilhacusto.ui.auth

import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import com.trilhacusto.ui.theme.BackgroundGradientEnd
import com.trilhacusto.ui.theme.BackgroundGradientStart
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.material.icons.filled.Fingerprint

fun Context.findActivity(): FragmentActivity? = when (this) {
    is FragmentActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    onNavigateToDashboard: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val context = LocalContext.current
    val isPluggyConfigured by viewModel.isPluggyConfigured.collectAsStateWithLifecycle()
    val hasRegistered by viewModel.hasRegistered.collectAsStateWithLifecycle()
    val passwordInput by viewModel.passwordInput.collectAsStateWithLifecycle()

    var authSuccess by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    var hasPromptedBiometrics by remember { mutableStateOf(false) }



    LaunchedEffect(authSuccess, isPluggyConfigured) {
        if (authSuccess && isPluggyConfigured != null) {
            if (isPluggyConfigured == true) {
                onNavigateToDashboard()
            } else {
                onNavigateToOnboarding()
            }
        }
    }

    val authenticateBiometrics = {
        val fragmentActivity = context.findActivity()
        if (fragmentActivity != null) {
            val executor = ContextCompat.getMainExecutor(context)
            val biometricPrompt = BiometricPrompt(
                fragmentActivity,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        Toast.makeText(context, "Erro Biometria: $errString", Toast.LENGTH_SHORT).show()
                    }
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        authSuccess = true
                    }
                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Toast.makeText(context, "Falha na biometria.", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticação Necessária")
                .setSubtitle("Use sua biometria para acessar")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK)
                .setNegativeButtonText("Cancelar")
                .build()

            biometricPrompt.authenticate(promptInfo)
        }
    }

    LaunchedEffect(hasRegistered) {
        if (hasRegistered == true && !hasPromptedBiometrics) {
            hasPromptedBiometrics = true
            authenticateBiometrics()
        }
    }

    if (hasRegistered != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(BackgroundGradientStart, BackgroundGradientEnd)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .systemBarsPadding()
                    .padding(32.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                androidx.compose.foundation.Image(
                    painter = androidx.compose.ui.res.painterResource(id = com.trilhacusto.R.drawable.ic_launcher_foreground),
                    contentDescription = "Logo TrilhaCusto",
                    modifier = Modifier.size(120.dp),
                    colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Bem-vindo ao TrilhaCusto",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    lineHeight = 36.sp
                )
                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = passwordInput,
                    onValueChange = { viewModel.updatePasswordInput(it) },
                    label = { Text("Senha") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF00A3FF),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color(0xFF00A3FF),
                        unfocusedLabelColor = Color.Gray
                    ),
                    trailingIcon = {
                        IconButton(
                            onClick = authenticateBiometrics,
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .size(48.dp)
                                .background(
                                    Color(0xFF00A3FF).copy(alpha = 0.15f),
                                    shape = androidx.compose.foundation.shape.CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = androidx.compose.material.icons.Icons.Default.Fingerprint,
                                contentDescription = "Usar Biometria",
                                tint = Color(0xFF00A3FF),
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.loginWithPassword(
                            onSuccess = { authSuccess = true },
                            onError = { Toast.makeText(context, "Senha incorreta.", Toast.LENGTH_SHORT).show() }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A3FF))
                ) {
                    Text("Entrar", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextButton(onClick = { showResetDialog = true }) {
                        Text("Esqueci minha senha", color = Color.Gray, fontSize = 14.sp)
                    }
                    TextButton(onClick = { onNavigateToRegister() }) {
                        Text("Primeiro acesso", color = Color.Gray, fontSize = 14.sp)
                    }
                }
            }
        }

        if (showResetDialog) {
            com.trilhacusto.ui.components.GlassConfirmDialog(
                onDismissRequest = { showResetDialog = false },
                onConfirm = {
                    showResetDialog = false
                    viewModel.resetApp {
                        onNavigateToRegister()
                    }
                },
                title = "Redefinir Aplicativo",
                text = "Como o Narrapay é 100% offline para garantir sua privacidade, a única forma de recuperar o acesso é resetando o app. Todos os dados locais (como categorias e transações manuais) serão apagados. Seus dados bancários poderão ser sincronizados novamente com a Pluggy. Deseja continuar?",
                isDestructive = true
            )
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize().background(Color(0xFF13131A)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    }
}
