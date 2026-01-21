package com.example.myfirstcomposeapp.ui.navigation

object Routes {
    const val LOGIN = "app/login"
    // Home tabs
    const val OVERVIEW = "home/overview"   // 态势/总览
    const val WORK = "home/work"           // 待办
    const val CREATE = "home/create"       // 发起
    const val TRACE = "home/trace"         // 追溯
    const val USER = "home/user"           // 用户

    // Detail
    const val DETAIL = "change/detail"
    const val DETAIL_WITH_ARG = "change/detail/{id}"
    fun detail(id: String) = "change/detail/$id"

    // Evidence
    const val EVIDENCE = "change/evidence"
    const val EVIDENCE_WITH_ARG = "change/evidence/{id}"
    fun evidence(id: String) = "change/evidence/$id"

    // Settings / Help / Report / SavedFilters（UI-only）
    const val SETTINGS = "app/settings"
    const val HELP = "app/help"
    const val REPORT_CENTER = "app/report_center"
    const val SAVED_FILTERS = "trace/saved_filters"

    // Legacy routes
    const val LEGACY_LIST = "legacy/list"
    const val LEGACY_ADD = "legacy/add"
    const val LEGACY_EDIT_WITH_ARG = "legacy/edit/{id}"
    fun legacyEdit(id: String) = "legacy/edit/$id"
}
