package com.example.myfirstcomposeapp.ui.common

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction

/**
 * 通用对话框集合（Day14 最终态）
 * 说明：
 * - 仅用于 UI 占位 / 确认提示
 * - 不包含复杂业务逻辑
 */

/* ========= 简单确认对话框 ========= */

@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    confirmText: String = "确认",
    dismissText: String = "取消"
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = {
                onConfirm()
                onDismiss()
            }) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissText)
            }
        }
    )
}

/* ========= 信息提示对话框 ========= */

@Composable
fun InfoDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    buttonText: String = "知道了"
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(buttonText)
            }
        }
    )
}

/* ========= 文本输入对话框（用于驳回/重开原因、设置处理人等） ========= */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInputDialog(
    title: String,
    placeholder: String,
    initialValue: String,
    onCancel: () -> Unit,
    onConfirm: (String) -> Unit,
    confirmText: String = "确认",
    cancelText: String = "取消",
    singleLine: Boolean = false,
    modifier: Modifier = Modifier
) {
    var value by remember(initialValue) { mutableStateOf(initialValue) }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onCancel,
        title = { Text(title) },
        text = {
            OutlinedTextField(
                value = value,
                onValueChange = { value = it },
                placeholder = { Text(placeholder) },
                singleLine = singleLine,
                minLines = if (singleLine) 1 else 2,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(value) }) { Text(confirmText) }
        },
        dismissButton = {
            TextButton(onClick = onCancel) { Text(cancelText) }
        }
    )
}