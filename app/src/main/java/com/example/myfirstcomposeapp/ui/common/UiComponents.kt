package com.example.myfirstcomposeapp.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.myfirstcomposeapp.R
import com.example.myfirstcomposeapp.model.ChangeType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeDropdownField(
    selected: ChangeType?,
    onSelect: (ChangeType?) -> Unit,
    label: String,
    isError: Boolean = false,
    supportingText: String? = null
) {
    var expanded by remember { mutableStateOf(false) }
    val text = selected?.label ?: stringResource(R.string.filter_all_label)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
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
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.filter_all_label)) },
                onClick = { onSelect(null); expanded = false }
            )
            // 兼容 Kotlin 1.8/1.9：values() 最稳
            ChangeType.entries.forEach { t ->
                DropdownMenuItem(
                    text = { Text(t.label) },
                    onClick = { onSelect(t); expanded = false }
                )
            }
        }
    }
}
