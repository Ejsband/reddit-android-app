package com.project.reddit.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.reddit.databinding.ViewFriendBinding
import com.project.reddit.entity.User

class ProfileFriendAdapter(
    values: List<User>
) : RecyclerView.Adapter<ProfileFriendViewHolder>() {

    private var values = values.toMutableList()

    fun setData(data: List<User>) {
        this.values = data.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileFriendViewHolder {
        val binding = ViewFriendBinding.inflate(LayoutInflater.from(parent.context))
        return ProfileFriendViewHolder(binding)
    }

    override fun getItemCount(): Int = values.size

    override fun onBindViewHolder(holder: ProfileFriendViewHolder, position: Int) {
        val item = values[position]

        Glide.with(holder.binding.profilePhoto).clear(holder.binding.profilePhoto)
        Glide.with(holder.binding.profilePhoto).load(item.image).into(holder.binding.profilePhoto)
        holder.binding.name.text = item.name
    }
}

class ProfileFriendViewHolder(val binding: ViewFriendBinding) :
    RecyclerView.ViewHolder(binding.root)