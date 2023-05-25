package com.bangkit.mystoryapps.UI.Login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.bangkit.mystoryapps.UI.Main.MainActivity
import com.bangkit.mystoryapps.UI.Regis.RegisterActivity
import com.bangkit.mystoryapps.data.viewmodels.AuthViewModel
import com.bangkit.mystoryapps.data.Result
import com.bangkit.mystoryapps.data.viewmodels.ViewModelFactory
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

        val factory: ViewModelFactory = ViewModelFactory.getUserInstance(this)
        val sharedPreferenceManager = SharedPreferenceManager(this)
        val viewModel: AuthViewModel by viewModels {
            factory
        }
        playAnimation()
        binding!!.txtSignUpFromLogin.setOnClickListener{
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding!!.btnLogin.setOnClickListener{
            if(binding!!.txtUsernameLogin.text.isNullOrEmpty() || binding!!.txtPassword.text.isNullOrEmpty()){
                Toast.makeText(this, "Data tidak boleh kosong", Toast.LENGTH_LONG).show()
            }
            else{
                if(binding!!.txtPassword.error != null || binding!!.txtUsernameLogin.error != null){
                    Toast.makeText(this, "Data tidak valid!", Toast.LENGTH_LONG).show()
                }
                else{
                    val email = binding!!.txtUsernameLogin.text.toString()
                    val password = binding!!.txtPassword.text.toString()

                    viewModel.loginUser(email, password).observe(this){result->
                        if(result!= null){
                            when(result){
                                is Result.Success -> {
                                    binding!!.progressLogin.visibility = View.GONE
                                    val user = UserEntity(result.data.loginResult.userId, result.data.loginResult.name, result.data.loginResult.token)
                                    sharedPreferenceManager.saveUser(user)
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                is Result.Error -> {
                                    binding!!.progressLogin.visibility = View.GONE
                                    Log.e("resErr", result.error)
                                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_LONG).show()
                                }
                                Result.Loading -> {
                                    //pasang loading button
                                    binding!!.progressLogin.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    private fun playAnimation() {
        val username = ObjectAnimator.ofFloat(binding!!.txtUsernameLogin, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding!!.txtPassword, View.ALPHA, 1f).setDuration(500)
        val btnLogin = ObjectAnimator.ofFloat(binding!!.btnLogin, View.ALPHA, 1f).setDuration(500)
        val txtPasswordTextLogin = ObjectAnimator.ofFloat(binding!!.txtPasswordTextLogin, View.ALPHA, 1f).setDuration(500)
        val txtLoginText = ObjectAnimator.ofFloat(binding!!.txtLoginText, View.ALPHA, 1f).setDuration(500)
        val txtEmailTextLogin = ObjectAnimator.ofFloat(binding!!.txtEmailTextLogin, View.ALPHA, 1f).setDuration(500)
        val txtSignUp = ObjectAnimator.ofFloat(binding!!.txtSignUpFromLogin, View.ALPHA, 1f).setDuration(500)


//        val together = AnimatorSet().apply {
//            playTogether(username, password, btnLogin, txtSignUp)
//        }

        AnimatorSet().apply {
            playSequentially(txtLoginText, txtEmailTextLogin, username, txtPasswordTextLogin, password, btnLogin, txtSignUp)
            start()
        }
        binding!!.progressLogin.visibility = View.GONE
    }
}