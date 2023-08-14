package com.project.reddit.ui.subreddits.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.reddit.databinding.ViewSubredditsCommentsBinding
import com.project.reddit.entity.CommentItemData
import java.text.SimpleDateFormat
import java.util.Date

class CommentsAdapter(
    values: List<CommentItemData>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<CommentsAdapter.SubredditCommentViewHolder>() {

    private var votePosition = -1
    private var downVoted = false
    private var upVoted = false

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

    @SuppressLint("SetTextI18n")
    inner class SubredditCommentViewHolder(val binding: ViewSubredditsCommentsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                listener.onRootViewClick(binding.root, absoluteAdapterPosition)
            }
            binding.saveButton.setOnClickListener {
                listener.onSaveButtonClick(binding.saveButton, absoluteAdapterPosition)
            }
            binding.minusButton.setOnClickListener {
                if (votePosition != -1 && votePosition != absoluteAdapterPosition) {
                    downVoted = false
                    upVoted = false
                }
                if (downVoted) {
                } else {
                    var votes: Int = binding.votesNumber.text.toString().toInt()
                    binding.votesNumber.text = (votes - 1).toString()
                    listener.onMinusButtonClick(binding.votesNumber, absoluteAdapterPosition)
                    votePosition = absoluteAdapterPosition
                    downVoted = true
                    upVoted = false
                }
            }
            binding.plusButton.setOnClickListener {
                if (votePosition != -1 && votePosition != absoluteAdapterPosition) {
                    downVoted = false
                    upVoted = false
                }
                if (upVoted) {
                } else {
                    var votes: Int = binding.votesNumber.text.toString().toInt()
                    binding.votesNumber.text = (votes + 1).toString()
                    listener.onPlusButtonClick(binding.votesNumber, absoluteAdapterPosition)
                    votePosition = absoluteAdapterPosition
                    upVoted = true
                    downVoted = false
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onRootViewClick(view: View, position: Int)
        fun onSaveButtonClick(view: View, position: Int)
        fun onMinusButtonClick(view: View, position: Int)
        fun onPlusButtonClick(view: View, position: Int)
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

