package com.example.myfirstcomposeapp.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    onLogin: () -> Unit
) {
    // TODO: replace with values from the user table when available.
    val defaultUsername = "admin"
    val defaultPassword = "123456"

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberAccount by remember { mutableStateOf(false) }
    var rememberPassword by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "变化点管理",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "用户登录",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                errorMessage = null
            },
            label = { Text("账号") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                errorMessage = null
            },
            label = { Text("密码") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.weight(1f)) {
                Checkbox(
                    checked = rememberAccount,
                    onCheckedChange = { rememberAccount = it }
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "记住账号")
            }
            Row(
                modifier = Modifier.wrapContentWidth()
            ) {
                Checkbox(
                    checked = rememberPassword,
                    onCheckedChange = { rememberPassword = it }
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "记住密码")
            }
        }
        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = errorMessage.orEmpty(),
                color = MaterialTheme.colorScheme.error
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                errorMessage = when {
                    username.isBlank() -> "请输入账号"
                    password.isBlank() -> "请输入密码"
                    username != defaultUsername -> "账号错误，请检查确认"
                    password != defaultPassword -> "密码错误，请检查确认"
                    else -> null
                }

                if (errorMessage == null) {
                    onLogin()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("登录")
        }
    }
}
