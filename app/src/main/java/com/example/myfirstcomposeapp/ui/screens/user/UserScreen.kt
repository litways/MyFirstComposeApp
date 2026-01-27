package com.example.myfirstcomposeapp.ui.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.myfirstcomposeapp.R
import com.example.myfirstcomposeapp.ui.viewmodel.ChangeViewModel

@Composable
fun UserScreen(
    vm: ChangeViewModel,
    onLogout: () -> Unit
) {
    var showPasswordDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var changeResult by remember { mutableStateOf<String?>(null) }
    val passwordChangedText = stringResource(R.string.user_password_changed)

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp)
        ) {
            Text(stringResource(R.string.nav_user), color = MaterialTheme.colorScheme.onPrimary)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(stringResource(R.string.user_info_title), style = MaterialTheme.typography.titleMedium)
                    Text(stringResource(R.string.user_account_label, vm.currentUser))
                    Text(stringResource(R.string.user_role_label, stringResource(vm.currentRole.labelRes)))
                }
            }

            Card {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(stringResource(R.string.user_actions_title), style = MaterialTheme.typography.titleMedium)
                    Row {
                        Button(onClick = { showPasswordDialog = true }) {
                            Text(stringResource(R.string.user_change_password))
                        }
                        Spacer(Modifier.width(12.dp))
                        Button(onClick = { showLogoutDialog = true }) {
                            Text(stringResource(R.string.user_logout))
                        }
                    }
                    changeResult?.let { result ->
                        Text(result, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Card {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(stringResource(R.string.common_note_title), style = MaterialTheme.typography.titleMedium)
                    Text(stringResource(R.string.user_note_change_password))
                    Text(stringResource(R.string.user_note_real_account))
                }
            }
        }
    }

    if (showPasswordDialog) {
        PasswordChangeDialog(
            onDismiss = { showPasswordDialog = false },
            onConfirm = {
                changeResult = passwordChangedText
                showPasswordDialog = false
            }
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("提示") },
            text = { Text("确定要退出当前用户登录吗？") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    }
                ) {
                    Text("确认")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
}

@Composable
private fun PasswordChangeDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val passwordsMatch = newPassword.isNotBlank() && newPassword == confirmPassword

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.user_change_password)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    label = { Text(stringResource(R.string.user_old_password)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation()
                )
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text(stringResource(R.string.user_new_password)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation()
                )
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text(stringResource(R.string.user_confirm_password)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation()
                )
                if (confirmPassword.isNotEmpty() && !passwordsMatch) {
                    Text(stringResource(R.string.user_password_mismatch), color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = passwordsMatch && oldPassword.isNotBlank()
            ) {
                Text(stringResource(R.string.action_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.action_cancel))
            }
        }
    )
}
