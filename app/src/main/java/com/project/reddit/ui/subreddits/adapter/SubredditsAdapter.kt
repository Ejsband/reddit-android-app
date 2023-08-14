package com.project.reddit.ui.subreddits.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.reddit.databinding.ViewSubredditBinding
import com.project.reddit.entity.SubredditData

class SubredditsAdapter(
    values: List<SubredditData>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<SubredditsAdapter.SubredditViewHolder>() {

    private var values = values.toMutableList()

    @SuppressLint("NotifyDataSetChanged")
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

    inner class SubredditViewHolder(val binding: ViewSubredditBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                listener.onRootViewClick(binding.root, absoluteAdapterPosition)
            }
            binding.button.setOnClickListener {
                if (binding.button.text == "subscribe") {
                    binding.button.text = "unsubscribe"
                    binding.button.setBackgroundColor(Color.parseColor("#eb5528"))
                    listener.onSubscribeButtonClick(binding.root, absoluteAdapterPosition)
                } else {
                    binding.button.text = "subscribe"
                    binding.button.setBackgroundColor(Color.parseColor("#000000"))
                    listener.onSubscribeButtonClick(binding.root, absoluteAdapterPosition)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onSubscribeButtonClick(view: View, position: Int)
        fun onRootViewClick(view: View, position: Int)
    }
}
