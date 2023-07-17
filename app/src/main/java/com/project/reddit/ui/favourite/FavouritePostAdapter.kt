package com.project.reddit.ui.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.reddit.databinding.ViewPostBinding
import com.project.reddit.entity.PostData

class FavouritePostAdapter(
    values: List<PostData>,
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<FavouritePostViewHolder>() {

    private var values = values.toMutableList()

    fun setData(data: List<PostData>) {
        this.values = data.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritePostViewHolder {
        val binding = ViewPostBinding.inflate(LayoutInflater.from(parent.context))
        return FavouritePostViewHolder(binding, onClick)
    }

    override fun getItemCount(): Int = values.size

    override fun onBindViewHolder(holder: FavouritePostViewHolder, position: Int) {
        val item = values[position]
        holder.binding.root.setOnClickListener {
            onClick(position)
        }

        if (item.info.image == "self") {
            Glide.with(holder.binding.imageView).clear(holder.binding.imageView)
            Glide.with(holder.binding.imageView).load(com.project.reddit.R.drawable.ic_list).into(holder.binding.imageView)
        } else {
            Glide.with(holder.binding.imageView).clear(holder.binding.imageView)
            Glide.with(holder.binding.imageView).load(item.info.image).into(holder.binding.imageView)
        }
        holder.binding.postName.text = item.info.title
        holder.binding.subredditName.text = item.info.subreddit
        holder.binding.commentAmount.text = item.info.commentNumber
    }
}

class FavouritePostViewHolder(val binding: ViewPostBinding, onClick: (Int) -> Unit) :
    RecyclerView.ViewHolder(binding.root)