package com.example.myfirstcomposeapp.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.myfirstcomposeapp.model.ChangeType

@Composable
fun TypeDropdownField(
    selected: ChangeType?,
    onSelect: (ChangeType?) -> Unit,
    label: String = "变化点类型",
    isError: Boolean = false,
    supportingText: String? = null
) {
    var expanded by remember { mutableStateOf(false) }
    val text = selected?.name ?: "全部"

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = text,
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            isError = isError,
            label = { Text(label) },
            supportingText = {
                if (!supportingText.isNullOrBlank()) Text(supportingText)
            },
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = "展开")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("全部") },
                onClick = { onSelect(null); expanded = false }
            )
            // 兼容 Kotlin 1.8/1.9：values() 最稳
            ChangeType.values().forEach { t ->
                DropdownMenuItem(
                    text = { Text(t.name) },
                    onClick = { onSelect(t); expanded = false }
                )
            }
        }
    }
}