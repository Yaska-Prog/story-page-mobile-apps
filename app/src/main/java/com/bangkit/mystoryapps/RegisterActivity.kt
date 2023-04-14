package com.bangkit.mystoryapps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.bangkit.mystoryapps.data.AuthViewModel
import com.bangkit.mystoryapps.data.Result
import com.bangkit.mystoryapps.data.ViewModelFactory
import com.bangkit.mystoryapps.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private var binding: ActivityRegisterBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        supportActionBar?.hide()
        val factory : ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: AuthViewModel by viewModels {
            factory
        }
        binding!!.btnRegis.setOnClickListener{
            val username = binding!!.txtUsernameRegister.text.toString()
            val password = binding!!.txtPasswordRegister.text.toString()
            val email = binding!!.txtEmailRegister.text.toString()

            viewModel.regisUser(username, email, password).observe(this){result ->
                if(result != null){
                    when(result){
                        is Result.Loading -> {
                            //tambah loading di button
                        }
                        is Result.Error -> {
                            Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_LONG).show()
                        }
                        is Result.Success -> {
                            Toast.makeText(this, "Sukses membuat akun dengan nama $username silahkan kembali ke halaman login!", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}