package com.project.reddit.ui.subreddits.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.reddit.databinding.ViewSubredditPostsBinding
import com.project.reddit.entity.PostItemData

class PostsAdapter(
    values: List<PostItemData>,
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<PostsViewHolder>() {

    private var values = values.toMutableList()

    fun setData(data: List<PostItemData>) {
        this.values = data.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val binding = ViewSubredditPostsBinding.inflate(LayoutInflater.from(parent.context))
        return PostsViewHolder(binding, onClick)
    }

    override fun getItemCount(): Int = values.size

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        val item = values[position]

        holder.binding.root.setOnClickListener {
            onClick(position)
        }

        if (item.data.image == "") {
            Glide.with(holder.binding.imageView).clear(holder.binding.imageView)
            Glide.with(holder.binding.imageView).load(com.project.reddit.R.drawable.ic_list).into(holder.binding.imageView)
        } else {
            Glide.with(holder.binding.imageView).clear(holder.binding.imageView)
            Glide.with(holder.binding.imageView).load(item.data.image).into(holder.binding.imageView)
        }
        holder.binding.postTitle.text = item.data.title
        holder.binding.author.text = item.data.author
    }
}

class PostsViewHolder(val binding: ViewSubredditPostsBinding, onClick: (Int) -> Unit) :
    RecyclerView.ViewHolder(binding.root)