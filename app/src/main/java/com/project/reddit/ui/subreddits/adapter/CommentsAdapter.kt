package com.project.reddit.ui.subreddits.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.reddit.databinding.ViewSubredditsCommentsBinding
import com.project.reddit.entity.CommentData
import com.project.reddit.entity.CommentItemData
import com.project.reddit.entity.SubredditData
import java.text.SimpleDateFormat
import java.util.Date

class CommentsAdapter(
    values: List<CommentItemData>,
    private val listener: OnSaveButtonClickListener
) : RecyclerView.Adapter<CommentsAdapter.SubredditCommentViewHolder>() {

    private var values = values.toMutableList()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<CommentItemData>) {
        this.values = data.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubredditCommentViewHolder {
        val binding = ViewSubredditsCommentsBinding.inflate(LayoutInflater.from(parent.context))
        return SubredditCommentViewHolder(binding)
    }

    override fun getItemCount(): Int = values.size

    override fun onBindViewHolder(holder: SubredditCommentViewHolder, position: Int) {
        val item = values[position]

        holder.binding.commentText.text = item.data.text
        holder.binding.authorName.text = item.data.author
        holder.binding.votesNumber.text = item.data.score.toString()
        holder.binding.time.text = getDateTime(item.data.creationTime.toLong())

    }

    inner class SubredditCommentViewHolder(val binding: ViewSubredditsCommentsBinding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        init {
            binding.saveButton.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
//                binding.button.text = "unsubscribe"
//                binding.button.setBackgroundColor(Color.parseColor("#eb5528"))
                listener.onItemClick(position)
            }
        }
    }

    interface OnSaveButtonClickListener {
        fun onItemClick(position: Int)
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDateTime(time: Long): String? {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val netDate = Date(time * 1000)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }
}

