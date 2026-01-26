package com.example.myfirstcomposeapp.ui.screens.trace

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.myfirstcomposeapp.R
import com.example.myfirstcomposeapp.ui.common.TypeDropdownField
import com.example.myfirstcomposeapp.ui.viewmodel.ChangeViewModel

@Composable
fun TraceScreen(
    vm: ChangeViewModel,
    onOpenTraceList: () -> Unit,
    onOpenSavedFilters: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(shape = MaterialTheme.shapes.large) {
            Column(
                Modifier.fillMaxWidth().padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(stringResource(R.string.trace_filters_title), style = MaterialTheme.typography.titleMedium)
                    TextButton(onClick = onOpenSavedFilters) {
                        Text(stringResource(R.string.trace_saved_filters))
                    }
                }

                OutlinedTextField(
                    value = vm.keyword,
                    onValueChange = { vm.keyword = it },
                    label = { Text(stringResource(R.string.trace_keyword_label)) },
                    placeholder = { Text(stringResource(R.string.trace_keyword_placeholder)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = vm.filterCreator,
                    onValueChange = { vm.filterCreator = it },
                    label = { Text(stringResource(R.string.label_creator)) },
                    placeholder = { Text(stringResource(R.string.trace_creator_placeholder)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FilterChip(
                        selected = vm.urgentOnly,
                        onClick = { vm.urgentOnly = !vm.urgentOnly },
                        label = { Text(stringResource(R.string.trace_urgent_only)) }
                    )
                }

                TypeDropdownField(
                    selected = vm.selectedType,
                    onSelect = { vm.selectedType = it },
                    label = stringResource(R.string.trace_type_label)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = vm.filterLine,
                        onValueChange = { vm.filterLine = it },
                        label = { Text(stringResource(R.string.label_line)) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = vm.filterEquipment,
                        onValueChange = { vm.filterEquipment = it },
                        label = { Text(stringResource(R.string.label_equipment)) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = vm.filterProcess,
                        onValueChange = { vm.filterProcess = it },
                        label = { Text(stringResource(R.string.label_process)) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = vm.startDate,
                        onValueChange = { vm.startDate = it },
                        label = { Text(stringResource(R.string.trace_start_date_label)) },
                        placeholder = { Text(stringResource(R.string.trace_date_placeholder)) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = vm.endDate,
                        onValueChange = { vm.endDate = it },
                        label = { Text(stringResource(R.string.trace_end_date_label)) },
                        placeholder = { Text(stringResource(R.string.trace_date_placeholder)) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = {
                            vm.keyword = ""
                            vm.filterCreator = ""
                            vm.urgentOnly = false
                            vm.selectedType = null
                            vm.filterLine = ""
                            vm.filterEquipment = ""
                            vm.filterProcess = ""
                            vm.startDate = ""
                            vm.endDate = ""
                        }
                    ) { Text(stringResource(R.string.action_clear)) }

                    Button(onClick = onOpenTraceList) { Text(stringResource(R.string.trace_apply)) }
                }
            }
        }

        Text(
            stringResource(R.string.trace_hint),
            style = MaterialTheme.typography.bodySmall
        )
    }
}
