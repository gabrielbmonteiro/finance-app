package com.trilhacusto.ui.account

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.ContentCopy
import com.trilhacusto.ui.components.GlassCard
import com.trilhacusto.ui.components.GlassTextField
import com.trilhacusto.ui.components.GlassButton
import com.trilhacusto.ui.components.GlassConfirmDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    viewModel: AccountViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToExtrato: () -> Unit,
    onNavigateToPendencias: () -> Unit,
    onNavigateToAuth: () -> Unit,
    onNavigateToOnboarding: () -> Unit
) {

    val clientId by viewModel.clientId.collectAsStateWithLifecycle()
    val clientSecret by viewModel.clientSecret.collectAsStateWithLifecycle()
    val accountId by viewModel.accountId.collectAsStateWithLifecycle()
    val newPassword by viewModel.newPassword.collectAsStateWithLifecycle()
    val confirmNewPassword by viewModel.confirmNewPassword.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    var editClientId by remember { mutableStateOf("") }
    var editClientSecret by remember { mutableStateOf("") }
    var editAccountId by remember { mutableStateOf("") }

    var showClientId by remember { mutableStateOf(false) }
    var showClientSecret by remember { mutableStateOf(false) }
    var showAccountId by remember { mutableStateOf(false) }

    var showLogoutDialog by remember { mutableStateOf(false) }
    var showUpdatePasswordDialog by remember { mutableStateOf(false) }

    LaunchedEffect(clientId, clientSecret, accountId) {
        editClientId = clientId ?: ""
        editClientSecret = clientSecret ?: ""
        editAccountId = accountId ?: ""
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Minha Conta", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
                actions = {
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Sair da Conta",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Início") },
                    label = { Text("Início") },
                    selected = false,
                    onClick = { onNavigateToHome() }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Menu, contentDescription = "Extrato") },
                    label = { Text("Extrato") },
                    selected = false,
                    onClick = { onNavigateToExtrato() }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Notifications, contentDescription = "Pendências") },
                    label = { Text("Pendências") },
                    selected = false,
                    onClick = { onNavigateToPendencias() }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Conta") },
                    label = { Text("Conta") },
                    selected = true,
                    onClick = { }
                )
            }
        },
        containerColor = Color.Transparent
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            com.trilhacusto.ui.theme.BackgroundGradientStart,
                            com.trilhacusto.ui.theme.BackgroundGradientEnd
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                GlassCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Alterar Senha", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))

                        GlassTextField(
                            value = newPassword,
                            onValueChange = { viewModel.updateNewPassword(it) },
                            label = { Text("Nova Senha") },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        GlassTextField(
                            value = confirmNewPassword,
                            onValueChange = { viewModel.updateConfirmNewPassword(it) },
                            label = { Text("Confirmar Nova Senha") },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        GlassButton(
                            text = "Atualizar Senha",
                            onClick = {
                                showUpdatePasswordDialog = true
                            },
                            enabled = newPassword.isNotBlank() && confirmNewPassword.isNotBlank(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                GlassCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Chaves da Pluggy", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            TextButton(onClick = onNavigateToOnboarding) {
                                Text("Ver Tutorial", color = com.trilhacusto.ui.theme.PrimaryAccent, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        GlassTextField(
                            value = editClientId,
                            onValueChange = { editClientId = it },
                            label = { Text("Client ID") },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = if (showClientId) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { showClientId = !showClientId }) {
                                    Icon(
                                        imageVector = if (showClientId) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = if (showClientId) "Esconder" else "Mostrar",
                                        tint = Color.Gray
                                    )
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        GlassTextField(
                            value = editClientSecret,
                            onValueChange = { editClientSecret = it },
                            label = { Text("Client Secret") },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = if (showClientSecret) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { showClientSecret = !showClientSecret }) {
                                    Icon(
                                        imageVector = if (showClientSecret) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = if (showClientSecret) "Esconder" else "Mostrar",
                                        tint = Color.Gray
                                    )
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        GlassTextField(
                            value = editAccountId,
                            onValueChange = { editAccountId = it },
                            label = { Text("Item ID") },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = if (showAccountId) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { showAccountId = !showAccountId }) {
                                    Icon(
                                        imageVector = if (showAccountId) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = if (showAccountId) "Esconder" else "Mostrar",
                                        tint = Color.Gray
                                    )
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        GlassButton(
                            text = "Salvar Chaves Pluggy",
                            onClick = {
                                viewModel.updatePluggyCredentials(editClientId, editClientSecret, editAccountId)
                                Toast.makeText(context, "Chaves da Pluggy atualizadas", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            if (showUpdatePasswordDialog) {
                GlassConfirmDialog(
                    title = "Atualizar Senha",
                    text = "Tem certeza que deseja atualizar sua senha?",
                    isDarker = true,
                    onConfirm = {
                        showUpdatePasswordDialog = false
                        viewModel.updatePassword(
                            onSuccess = { Toast.makeText(context, "Senha atualizada com sucesso", Toast.LENGTH_SHORT).show() },
                            onError = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                        )
                    },
                    onDismissRequest = { showUpdatePasswordDialog = false }
                )
            }

            if (showLogoutDialog) {
                GlassConfirmDialog(
                    title = "Sair da Conta",
                    text = "Tem certeza que deseja sair da sua conta? Você precisará fazer login novamente.",
                    isDarker = true,
                    isDestructive = true,
                    onConfirm = {
                        showLogoutDialog = false
                        onNavigateToAuth()
                    },
                    onDismissRequest = { showLogoutDialog = false }
                )
            }
        }
    }
}
