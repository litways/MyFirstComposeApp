package com.example.myfirstcomposeapp.ui.screens.login

import android.app.Activity
import android.content.Context
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myfirstcomposeapp.R

@Composable
fun LoginScreen(
    onLogin: () -> Unit
) {
    val context = LocalContext.current
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
    var showPassword by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }

    val onExit = {
        (context as? Activity)?.finishAffinity()
    }

    val actionFontSize = MaterialTheme.typography.bodyLarge.fontSize * 1.25f
    val titleFontSize = MaterialTheme.typography.headlineLarge.fontSize * 1.5f

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text(text = "提示") },
            text = { Text(text = "确定要退出应用吗？") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showExitDialog = false
                        onExit()
                    }
                ) {
                    Text(text = "确认")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text(text = "取消")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(top = 8.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = stringResource(R.string.login_title),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = titleFontSize,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = username,
                onValueChange = {
                    username = it
                    if (usernameError && it.isNotBlank()) usernameError = false
                },
                placeholder = { Text(stringResource(R.string.login_username_placeholder)) },
                modifier = Modifier.fillMaxWidth(0.9f),
                isError = usernameError,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF2F2F2),
                    unfocusedContainerColor = Color(0xFFF2F2F2),
                    disabledContainerColor = Color(0xFFF2F2F2),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = {
                    password = it
                    if (passwordError && it.isNotBlank()) passwordError = false
                },
                placeholder = { Text(stringResource(R.string.login_password_placeholder)) },
                modifier = Modifier.fillMaxWidth(0.9f),
                isError = passwordError,
                visualTransformation = if (showPassword) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) {
                                Icons.Filled.VisibilityOff
                            } else {
                                Icons.Filled.Visibility
                            },
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF2F2F2),
                    unfocusedContainerColor = Color(0xFFF2F2F2),
                    disabledContainerColor = Color(0xFFF2F2F2),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(0.9f),
                horizontalArrangement = Arrangement.SpaceBetween,
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
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(R.string.login_remember_account))
                }
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
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(R.string.login_remember_password))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

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
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0D6EFD),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = stringResource(R.string.action_login),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = actionFontSize
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { showExitDialog = true },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE0E0E0),
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = stringResource(R.string.action_exit),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = actionFontSize
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.login_forgot_password),
                color = Color(0xFF0D6EFD),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
