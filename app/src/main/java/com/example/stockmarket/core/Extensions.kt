package com.example.stockmarket.core

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat

fun AppCompatActivity.init(@IdRes resId: Int, fragment: Fragment) {
    supportFragmentManager
        .beginTransaction()
        .add(resId, fragment, fragment::class.java.name)
        .commit()
}
