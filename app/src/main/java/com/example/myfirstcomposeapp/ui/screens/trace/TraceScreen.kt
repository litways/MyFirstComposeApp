package com.example.myfirstcomposeapp.ui.screens.trace

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
                    Text("查询条件", style = MaterialTheme.typography.titleMedium)
                    TextButton(onClick = onOpenSavedFilters) { Text("保存的筛选") }
                }

                OutlinedTextField(
                    value = vm.keyword,
                    onValueChange = { vm.keyword = it },
                    label = { Text("关键字") },
                    placeholder = { Text("变化点/内容/创建人/产线/设备/工序") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = vm.filterCreator,
                    onValueChange = { vm.filterCreator = it },
                    label = { Text("创建人") },
                    placeholder = { Text("例如：张三") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FilterChip(
                        selected = vm.urgentOnly,
                        onClick = { vm.urgentOnly = !vm.urgentOnly },
                        label = { Text("仅紧急") }
                    )
                }

                TypeDropdownField(
                    selected = vm.selectedType,
                    onSelect = { vm.selectedType = it },
                    label = "类型：全部"
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = vm.filterLine,
                        onValueChange = { vm.filterLine = it },
                        label = { Text("产线") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = vm.filterEquipment,
                        onValueChange = { vm.filterEquipment = it },
                        label = { Text("设备") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = vm.filterProcess,
                        onValueChange = { vm.filterProcess = it },
                        label = { Text("工序") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = vm.startDate,
                        onValueChange = { vm.startDate = it },
                        label = { Text("起始日期（更新时间）") },
                        placeholder = { Text("yyyy-MM-dd") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = vm.endDate,
                        onValueChange = { vm.endDate = it },
                        label = { Text("结束日期（更新时间）") },
                        placeholder = { Text("yyyy-MM-dd") },
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
                    ) { Text("清空") }

                    Button(onClick = onOpenTraceList) { Text("应用并查看列表") }
                }
            }
        }

        Text(
            "说明：日期范围按“更新时间”过滤（格式 yyyy-MM-dd）。",
            style = MaterialTheme.typography.bodySmall
        )
    }
}