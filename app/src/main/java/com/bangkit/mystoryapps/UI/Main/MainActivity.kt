package com.bangkit.mystoryapps.UI.Main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.mystoryapps.UI.UploadStory.AddStoryActivity
import com.bangkit.mystoryapps.data.remote.response.ListStoryItem
import com.bangkit.mystoryapps.data.viewmodels.ViewModelFactory
import com.bangkit.mystoryapps.databinding.ActivityMainBinding
import com.bangkit.mystoryapps.data.Result
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

    private fun showData(listStory: List<ListStoryItem>){
        binding!!.rvStory.layoutManager = LinearLayoutManager(this)
        val adapter = StoryAdapter(listStory)
        binding!!.rvStory.adapter = adapter
    }
}