package com.rick.shop

import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.setNickname(nickname:String) {
    getSharedPreferences("shop", AppCompatActivity.MODE_PRIVATE)
        .edit()
        .putString("NICKNAME", nickname)
        .apply()
}

fun AppCompatActivity.getNickname(): String? {
    return getSharedPreferences("shop", AppCompatActivity.MODE_PRIVATE).getString("NICKNAME", "")
}