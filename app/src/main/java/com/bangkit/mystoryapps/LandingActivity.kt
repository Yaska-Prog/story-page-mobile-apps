package com.bangkit.mystoryapps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class LandingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        supportActionBar?.hide()
    }
}