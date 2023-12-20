package com.example.storyapp2.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp2.data.response.ListStoryItem
import com.example.storyapp2.databinding.ListStoryBinding
import com.example.storyapp2.ui.detail.DetailActivity
import com.example.storyapp2.ui.detail.DetailActivity.Companion.EXTRA_DATA

class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.ViewHolder>( DIFF_CALLBACK) {

    class ViewHolder(val binding: ListStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story :ListStoryItem){
            binding.listName.text = "${story.name}"
            binding.listDesc.text = "${story.description}"
            Glide.with(itemView.context)
                .load("${story.photoUrl}")
                .into(binding.photo)

            binding.cardView.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.photo, "photo"),
                        Pair(binding.listName, "name"),
                        Pair(binding.listDesc, "description")
                    )

                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(EXTRA_DATA, story)
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data !=null ) {
            holder.bind(data)
        }


    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>(){
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}