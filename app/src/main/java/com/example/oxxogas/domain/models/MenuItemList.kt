package com.example.oxxogas.domain.models

data class MenuItemList (
    var idMenuItem: Int,
    var iconActiveMenuItem: Int,
    var iconInactiveMenuItem: Int,
    var menuNameItem: String,
    var status: Boolean
)