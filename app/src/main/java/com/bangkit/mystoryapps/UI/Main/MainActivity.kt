package com.bangkit.mystoryapps.UI.Main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.mystoryapps.R
import com.bangkit.mystoryapps.UI.Landing.LandingActivity
import com.bangkit.mystoryapps.UI.UploadStory.AddStoryActivity
import com.bangkit.mystoryapps.UI.maps.ListStoryMapsActivity
import com.bangkit.mystoryapps.data.viewmodels.ViewModelFactory
import com.bangkit.mystoryapps.databinding.ActivityMainBinding
import com.bangkit.mystoryapps.data.local.SharedPreferenceManager
import com.bangkit.mystoryapps.data.viewmodels.StoryViewModel

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        val factory: ViewModelFactory = ViewModelFactory.getStoryInstance(this)
        val viewmodel: StoryViewModel by viewModels {
            factory
        }
        showData(viewModel = viewmodel)
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
        val map = menu?.findItem(R.id.btnSeeMap)
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
        map!!.setOnMenuItemClickListener {
            val intent = Intent(this@MainActivity, ListStoryMapsActivity::class.java)
            startActivity(intent)
            true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        val factory: ViewModelFactory = ViewModelFactory.getStoryInstance(this)
        val viewmodel: StoryViewModel by viewModels {
            factory
        }
        showData(viewModel = viewmodel)
        super.onResume()
    }
    fun showData(viewModel: StoryViewModel){
        binding!!.rvStory.layoutManager = LinearLayoutManager(this)
        val adapter = StoryAdapter()
        binding!!.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                adapter.retry()
            }
        )
        viewModel.storyPaging().observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }
}