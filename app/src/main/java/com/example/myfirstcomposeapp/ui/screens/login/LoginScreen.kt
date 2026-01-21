package com.example.myfirstcomposeapp.ui.screens.login

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardActions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    onLogin: () -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val prefs = remember {
        context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
    }
    val savedRememberAccount = remember { prefs.getBoolean("remember_account", false) }
    val savedRememberPassword = remember { prefs.getBoolean("remember_password", false) }
    var username by remember {
        mutableStateOf(
            if (savedRememberAccount) prefs.getString("username", "") ?: "" else ""
        )
    }
    var password by remember {
        mutableStateOf(
            if (savedRememberPassword) prefs.getString("password", "") ?: "" else ""
        )
    }
    var rememberAccount by remember { mutableStateOf(savedRememberAccount) }
    var rememberPassword by remember { mutableStateOf(savedRememberPassword) }
    var usernameError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .width(48.dp)
                            .height(48.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "UI Unicorn",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "欢迎回来",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "登录以继续使用",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "账号",
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        if (usernameError && it.isNotBlank()) {
                            usernameError = false
                        }
                    },
                    placeholder = { Text("请输入账号") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = usernameError,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    singleLine = true,
                    supportingText = {
                        if (usernameError) {
                            Text(
                                text = "账号不能为空",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "密码",
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        if (passwordError && it.isNotBlank()) {
                            passwordError = false
                        }
                    },
                    placeholder = { Text("请输入密码") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = passwordError,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),
                    singleLine = true,
                    supportingText = {
                        if (passwordError) {
                            Text(
                                text = "密码不能为空",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = rememberAccount,
                            onCheckedChange = { isChecked ->
                                rememberAccount = isChecked
                                prefs.edit().putBoolean("remember_account", isChecked).apply()
                                if (!isChecked) {
                                    prefs.edit().remove("username").apply()
                                }
                            }
                        )
                        Text(
                            text = "记住账号",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = rememberPassword,
                            onCheckedChange = { isChecked ->
                                rememberPassword = isChecked
                                prefs.edit().putBoolean("remember_password", isChecked).apply()
                                if (!isChecked) {
                                    prefs.edit().remove("password").apply()
                                }
                            }
                        )
                        Text(
                            text = "记住密码",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { }) {
                        Text("忘记密码？")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "当前是演示状态，账号密码还是默认admin 123456",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        val isUsernameBlank = username.isBlank()
                        val isPasswordBlank = password.isBlank()
                        usernameError = isUsernameBlank
                        passwordError = isPasswordBlank
                        if (!isUsernameBlank && !isPasswordBlank) {
                            if (rememberAccount) {
                                prefs.edit().putString("username", username).apply()
                            } else {
                                prefs.edit().remove("username").apply()
                            }
                            if (rememberPassword) {
                                prefs.edit().putString("password", password).apply()
                            } else {
                                prefs.edit().remove("password").apply()
                            }
                            onLogin()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("登录")
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { showExitDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("退出")
                }
                Spacer(modifier = Modifier.height(20.dp))
                Divider()
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "还没有账号？现在注册",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("退出提示") },
            text = { Text("确定要退出应用吗？") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showExitDialog = false
                        (context as? Activity)?.finish()
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
}
