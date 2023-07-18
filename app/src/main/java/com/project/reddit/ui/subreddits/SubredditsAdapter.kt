package com.project.reddit.ui.subreddits

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.reddit.databinding.ViewSubredditBinding
import com.project.reddit.entity.SubredditData

class SubredditsAdapter(
    values: List<SubredditData>
) : RecyclerView.Adapter<SubredditViewHolder>() {

    private var values = values.toMutableList()

    fun setData(data: List<SubredditData>) {
        this.values = data.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubredditViewHolder {
        val binding = ViewSubredditBinding.inflate(LayoutInflater.from(parent.context))
        return SubredditViewHolder(binding)
    }

    override fun getItemCount(): Int = values.size

    override fun onBindViewHolder(holder: SubredditViewHolder, position: Int) {
        val item = values[position]

        Glide.with(holder.binding.image).clear(holder.binding.image)
        Glide.with(holder.binding.image).load(item.data.icon).into(holder.binding.image)
        holder.binding.title.text = item.data.title
        holder.binding.url.text = item.data.url
    }
}

class SubredditViewHolder(val binding: ViewSubredditBinding) :
    RecyclerView.ViewHolder(binding.root)