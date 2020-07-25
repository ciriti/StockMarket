package com.example.stockmarket.core

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.init(@IdRes resId: Int, fragment: Fragment) {
    supportFragmentManager
        .beginTransaction()
        .add(resId, fragment, fragment::class.java.name)
        .commit()
}
