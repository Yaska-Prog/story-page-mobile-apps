package com.bangkit.mystoryapps.UI.Regis

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.bangkit.mystoryapps.data.viewmodels.AuthViewModel
import com.bangkit.mystoryapps.data.Result
import com.bangkit.mystoryapps.data.viewmodels.ViewModelFactory
import com.bangkit.mystoryapps.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private var binding: ActivityRegisterBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        supportActionBar?.hide()
        playAnimation()
        val factory : ViewModelFactory = ViewModelFactory.getUserInstance(this)
        val viewModel: AuthViewModel by viewModels {
            factory
        }
        binding!!.btnRegis.setOnClickListener{
            if(binding!!.txtEmailRegister.text.isNullOrEmpty() || binding!!.txtUsernameRegister.text.isNullOrEmpty() || binding!!.txtPasswordRegister.text.isNullOrEmpty()){
                Toast.makeText(this, "Data tidak boleh kosong", Toast.LENGTH_LONG).show()
            }
            else{
                if(binding!!.txtEmailRegister.error.isNullOrEmpty() && binding!!.txtUsernameRegister.error.isNullOrEmpty() && binding!!.txtPasswordRegister.error.isNullOrEmpty()){
                    val username = binding!!.txtUsernameRegister.text.toString()
                    val password = binding!!.txtPasswordRegister.text.toString()
                    val email = binding!!.txtEmailRegister.text.toString()

                    viewModel.regisUser(username, email, password).observe(this){result ->
                        if(result != null){
                            when (result){
                                is Result.Loading -> {
                                }
                                is Result.Error -> {
                                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_LONG).show()
                                }
                                is Result.Success -> {
                                    Toast.makeText(this, "Sukses membuat akun dengan nama $username silahkan kembali ke halaman login!", Toast.LENGTH_LONG).show()
                                }
                                else -> {
                                    Toast.makeText(this, "Result not found", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                }
                else{
                    Toast.makeText(this, "Data tidak valid!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun playAnimation(){
        val textRegis = ObjectAnimator.ofFloat(binding!!.txtRegRegister, View.ALPHA, 1f).setDuration(500)
        val textUsername = ObjectAnimator.ofFloat(binding!!.txtUserRegister, View.ALPHA, 1f).setDuration(500)
        val textPassword = ObjectAnimator.ofFloat(binding!!.textPasswordRegister, View.ALPHA, 1f).setDuration(500)
        val textEmail = ObjectAnimator.ofFloat(binding!!.textEmailRegister, View.ALPHA, 1f).setDuration(500)
        val txtUsername = ObjectAnimator.ofFloat(binding!!.txtUsernameRegister, View.ALPHA, 1f).setDuration(500)
        val txtPassword = ObjectAnimator.ofFloat(binding!!.txtPasswordRegister, View.ALPHA, 1f).setDuration(500)
        val txtEmail = ObjectAnimator.ofFloat(binding!!.txtEmailRegister, View.ALPHA, 1f).setDuration(500)
        val btnRegis = ObjectAnimator.ofFloat(binding!!.btnRegis, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(textRegis, textUsername, txtUsername, textEmail, txtEmail, textPassword, txtPassword, btnRegis)
            start()
        }
    }
}