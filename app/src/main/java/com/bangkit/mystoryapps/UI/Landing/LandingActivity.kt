package com.bangkit.mystoryapps.UI.Landing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.mystoryapps.UI.Login.LoginActivity
import com.bangkit.mystoryapps.UI.Regis.RegisterActivity
import com.bangkit.mystoryapps.databinding.ActivityLandingBinding

class LandingActivity : AppCompatActivity() {
    private var binding: ActivityLandingBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        supportActionBar?.hide()
        binding!!.btnLandingToRegis.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding!!.btnLandingToLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}