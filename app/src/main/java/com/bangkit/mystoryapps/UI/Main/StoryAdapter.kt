package com.bangkit.mystoryapps.UI.Main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.mystoryapps.UI.DetailStory.DetailStoryActivity
import com.bangkit.mystoryapps.data.remote.response.ListStoryItem
import com.bangkit.mystoryapps.databinding.ItemStoryBinding
import com.bumptech.glide.Glide

class StoryAdapter(private val listStory: List<ListStoryItem>) :
    RecyclerView.Adapter<StoryAdapter.ViewHolder>()
{
    class ViewHolder(view: ItemStoryBinding) : RecyclerView.ViewHolder(view.root){
        val txtUser = view.txtUsernameMain
        val imgStory = view.imgStoryMain
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listStory.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtUser.text = listStory[position].name
        Glide.with(holder.itemView.context).load(listStory[position].photoUrl).into(holder.imgStory)
        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, DetailStoryActivity::class.java)
            intent.putExtra(DetailStoryActivity.EXTRA_ID, listStory[position].id)
            holder.itemView.context.startActivity(intent)
        }
    }
}