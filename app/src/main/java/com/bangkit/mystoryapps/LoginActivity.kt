package com.bangkit.mystoryapps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.bangkit.mystoryapps.data.AuthViewModel
import com.bangkit.mystoryapps.data.Result
import com.bangkit.mystoryapps.data.ViewModelFactory
import com.bangkit.mystoryapps.data.local.Entity.UserEntity
import com.bangkit.mystoryapps.data.local.SharedPreferenceManager
import com.bangkit.mystoryapps.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private var binding: ActivityLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.hide()

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val sharedPreferenceManager = SharedPreferenceManager(this)
        val viewModel: AuthViewModel by viewModels {
            factory
        }

        binding!!.btnLogin.setOnClickListener{
            val email = binding!!.txtUsernameLogin.text.toString()
            val password = binding!!.txtPassword.text.toString()

            viewModel.loginUser(email, password).observe(this){result->
                if(result!= null){
                    when(result){
                        is Result.Success -> {
                            val user = UserEntity(result.data.loginResult.userId, result.data.loginResult.name, result.data.loginResult.token)
                            sharedPreferenceManager.saveUser(user)
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }
                        is Result.Error -> {
                            Log.d("resErr", result.error)
                            Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_LONG).show()
                        }
                        Result.Loading -> {
                            //pasang loading button
                        }
                    }
                }
            }
        }
    }
}