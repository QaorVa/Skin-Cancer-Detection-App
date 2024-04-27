package com.dicoding.asclepius.view.history

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.model.History
import com.dicoding.asclepius.databinding.ItemHistoryBinding

class HistoryAdapter(private val onItemClickCallback: OnItemClickCallback, private val onDeleteClickListener: OnDeleteClickListener) : ListAdapter<History, HistoryAdapter.HistoryViewHolder>(HistoryDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(getItem(holder.adapterPosition)) }
    }


    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: History) {
            binding.apply {
                imgItemPhoto.setImage(history.imageUri)
                tvItemName.text = history.label
                tvItemConfidence.text = history.confidence

                if(history.label == "Cancer") {
                    tvItemConfidence.setTextColor(itemView.context.getColor(R.color.red))
                    tvItemName.setTextColor(itemView.context.getColor(R.color.red))
                } else {
                    tvItemConfidence.setTextColor(itemView.context.getColor(R.color.blue))
                    tvItemName.setTextColor(itemView.context.getColor(R.color.blue))
                }

                btDelete.setOnClickListener { onDeleteClickListener.onDeleteClicked(history) }

            }
        }
    }


    interface OnItemClickCallback {
        fun onItemClicked(data: History)
    }

    interface OnDeleteClickListener {
        fun onDeleteClicked(data: History)
    }

    class HistoryDiffCallback : DiffUtil.ItemCallback<History>(){

        override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
            return oldItem == newItem
        }
    }

    private fun ImageView.setImage(uri: Uri?) {
        Glide.with(this).load(uri).apply(RequestOptions()).into(this)
    }
}