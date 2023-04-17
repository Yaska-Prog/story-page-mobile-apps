package com.bangkit.mystoryapps.UI.Landing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.mystoryapps.UI.Login.LoginActivity
import com.bangkit.mystoryapps.UI.Main.MainActivity
import com.bangkit.mystoryapps.UI.Regis.RegisterActivity
import com.bangkit.mystoryapps.data.local.SharedPreferenceManager
import com.bangkit.mystoryapps.databinding.ActivityLandingBinding

class LandingActivity : AppCompatActivity() {
    private var binding: ActivityLandingBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        supportActionBar?.hide()
        val sharePref = SharedPreferenceManager(this)
        if(sharePref.getUser() != null){
            val intent = Intent(this@LandingActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
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