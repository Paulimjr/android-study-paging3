package com.androidstudy.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.androidstudy.databinding.LayoutPostItemBinding
import com.androidstudy.service.domain.CharacterModel

class CharacterAdapter : PagingDataAdapter<CharacterModel, CharacterAdapter.PostViewHolder>(CharacterDiffCallback){

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutPostItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return PostViewHolder(view)
    }

    class PostViewHolder(private val binding: LayoutPostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CharacterModel) = with(binding) {
            tvId.text = item.id.toString()
            tvTitle.text = item.name
            ivCharacter.load(item.image)
        }
    }

    object CharacterDiffCallback : DiffUtil.ItemCallback<CharacterModel>() {

        override fun areItemsTheSame(oldItem: CharacterModel, newItem: CharacterModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CharacterModel, newItem: CharacterModel) =
            oldItem == newItem
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}
