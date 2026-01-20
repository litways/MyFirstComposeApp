package com.example.myfirstcomposeapp.model

enum class ChangeType(val label: String) {
    PROCESS("工艺"),
    EQUIPMENT("设备"),
    MATERIAL("物料"),
    QUALITY("质量"),
    OTHER("其他")
}
