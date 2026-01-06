package com.example.intervalprotrainerapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.intervalprotrainerapp.databinding.ItemTrainingBinding
import com.example.intervalprotrainerapp.models.TrainingItem

class HomeAdapter : ListAdapter<TrainingItem, HomeAdapter.HomeViewHolder>(DIFF_CALLBACK) {

    inner class HomeViewHolder(private val binding: ItemTrainingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(training: TrainingItem) {
            binding.nameTraining.text = training.name
            binding.intervalWork.text = training.intervalWork.toString()
            binding.intervalRelax.text = training.intervalRelax.toString()
            binding.cycles.text = training.cycles.toString()
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeViewHolder {
        val binding = ItemTrainingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeAdapter.HomeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TrainingItem>() {
            override fun areItemsTheSame(oldItem: TrainingItem, newItem: TrainingItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TrainingItem, newItem: TrainingItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}