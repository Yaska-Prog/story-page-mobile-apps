package com.bangkit.mystoryapps.UI.DetailStory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.bangkit.mystoryapps.data.Result
import com.bangkit.mystoryapps.data.remote.response.Story
import com.bangkit.mystoryapps.data.viewmodels.StoryViewModel
import com.bangkit.mystoryapps.data.viewmodels.ViewModelFactory
import com.bangkit.mystoryapps.databinding.ActivityDetailStoryBinding
import com.bumptech.glide.Glide

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.getStringExtra(EXTRA_ID) as String
        val factory: ViewModelFactory = ViewModelFactory.getStoryInstance(this)
        val viewModel: StoryViewModel by viewModels {
            factory
        }
        viewModel.getDetailStory(id).observe(this){result ->
            if(result != null){
                when(result){
                    is Result.Error -> {
                        binding.progressBarDetail.visibility = View.GONE
                        Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_LONG).show()
                    }
                    is Result.Loading -> {
                        binding.progressBarDetail.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        val data = result.data
                        binding.progressBarDetail.visibility = View.GONE
                        showData(data)
                    }
                }
            }
        }
    }
    private fun showData(data: Story){
        Glide.with(this@DetailStoryActivity)
            .load(data.photoUrl)
            .into(binding.imgDetailPhoto)
        binding.txtUsernameDetail.text = data.name
        binding.txtDescriptionDetail.text = data.description
    }
    companion object{
        const val EXTRA_ID = "EXTRA_ID"
    }
}