package com.project.reddit.ui.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.reddit.databinding.ViewCommentBinding
import com.project.reddit.entity.CommentData

class FavouriteCommentAdapter(
    values: List<CommentData>,
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<FavouriteCommentViewHolder>() {

    private var values = values.toMutableList()

    fun setData(data: List<CommentData>) {
        this.values = data.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteCommentViewHolder {
        val binding = ViewCommentBinding.inflate(LayoutInflater.from(parent.context))
        return FavouriteCommentViewHolder(binding, onClick)
    }

    override fun getItemCount(): Int = values.size

    override fun onBindViewHolder(holder: FavouriteCommentViewHolder, position: Int) {
        val item = values[position]
        holder.binding.root.setOnClickListener {
            onClick(position)
        }


        Glide.with(holder.binding.imageView).clear(holder.binding.imageView)
        Glide.with(holder.binding.imageView).load(com.project.reddit.R.drawable.ic_comment)
            .into(holder.binding.imageView)

        holder.binding.postName.text = item.info.postTitle
        holder.binding.body.text = item.info.body
        holder.binding.author.text = item.info.author
    }
}

class FavouriteCommentViewHolder(val binding: ViewCommentBinding, onClick: (Int) -> Unit) :
    RecyclerView.ViewHolder(binding.root)