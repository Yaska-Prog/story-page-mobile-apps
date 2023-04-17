package com.bangkit.mystoryapps.UI.Main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.mystoryapps.R
import com.bangkit.mystoryapps.UI.Landing.LandingActivity
import com.bangkit.mystoryapps.UI.UploadStory.AddStoryActivity
import com.bangkit.mystoryapps.data.remote.response.ListStoryItem
import com.bangkit.mystoryapps.data.viewmodels.ViewModelFactory
import com.bangkit.mystoryapps.databinding.ActivityMainBinding
import com.bangkit.mystoryapps.data.Result
import com.bangkit.mystoryapps.data.local.SharedPreferenceManager
import com.bangkit.mystoryapps.data.viewmodels.StoryViewModel

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val factory: ViewModelFactory = ViewModelFactory.getStoryInstance(this)
        val viewModel: StoryViewModel by viewModels {
            factory
        }
        viewModel.getStories().observe(this){result ->
            if(result!= null){
                when(result){
                    is Result.Loading -> {
                        binding!!.progressBarMain.visibility = View.VISIBLE
                    }
                    is Result.Error -> {
                        binding!!.progressBarMain.visibility = View.GONE
                        Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_LONG).show()
                    }
                    is Result.Success -> {
                        binding!!.progressBarMain.visibility = View.GONE
                        showData(result.data)
                    }
                }
            }
        }
        binding!!.fabAddStory.setOnClickListener{
            val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        val logout = menu?.findItem(R.id.btnLogout)
        val lang = menu?.findItem(R.id.btnChangeLang)
        val sharedPref = SharedPreferenceManager(this)
        logout?.setOnMenuItemClickListener { item: MenuItem? ->
            Toast.makeText(this, "Berhasil melakukan logout, silahkan kembali ke landing page!", Toast.LENGTH_LONG).show()
            sharedPref.clear()
            val intent = Intent(this@MainActivity, LandingActivity::class.java)
            startActivity(intent)
            finish()
            true
        }
        lang!!.setOnMenuItemClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            true
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        val factory: ViewModelFactory = ViewModelFactory.getStoryInstance(this)
        val viewModel: StoryViewModel by viewModels {
            factory
        }
        viewModel.getStories().observe(this){result ->
            if(result!= null){
                when(result){
                    is Result.Loading -> {
                        binding!!.progressBarMain.visibility = View.VISIBLE
                    }
                    is Result.Error -> {
                        binding!!.progressBarMain.visibility = View.GONE
                        Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_LONG).show()
                    }
                    is Result.Success -> {
                        binding!!.progressBarMain.visibility = View.GONE
                        showData(result.data)
                    }
                }
            }
        }
        super.onResume()
    }

    private fun showData(listStory: List<ListStoryItem>){
        binding!!.rvStory.layoutManager = LinearLayoutManager(this)
        val adapter = StoryAdapter(listStory)
        binding!!.rvStory.adapter = adapter
    }
}