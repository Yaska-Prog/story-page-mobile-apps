package com.bangkit.mystoryapps.UI.Landing

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
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
    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}