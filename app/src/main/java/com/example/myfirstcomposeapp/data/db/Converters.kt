package com.example.myfirstcomposeapp.data.db

import androidx.room.TypeConverter
import com.example.myfirstcomposeapp.model.ActionType
import com.example.myfirstcomposeapp.model.ChangeStatus
import com.example.myfirstcomposeapp.model.ChangeType
import com.example.myfirstcomposeapp.model.UserRole

class Converters {

    // ChangeType
    @TypeConverter
    fun fromChangeType(type: ChangeType): String = type.name

    @TypeConverter
    fun toChangeType(value: String): ChangeType = ChangeType.valueOf(value)

    // ChangeStatus
    @TypeConverter
    fun fromChangeStatus(status: ChangeStatus): String = status.name

    @TypeConverter
    fun toChangeStatus(value: String): ChangeStatus = ChangeStatus.valueOf(value)

    // ActionType
    @TypeConverter
    fun fromActionType(action: ActionType): String = action.name

    @TypeConverter
    fun toActionType(value: String): ActionType = ActionType.valueOf(value)

    // UserRole（先留着，后面权限策略会用）
    @TypeConverter
    fun fromUserRole(role: UserRole): String = role.name

    @TypeConverter
    fun toUserRole(value: String): UserRole = UserRole.valueOf(value)
}