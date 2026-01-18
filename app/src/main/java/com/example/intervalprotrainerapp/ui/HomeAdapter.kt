package com.example.intervalprotrainerapp.ui

import com.example.intervalprotrainerapp.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.intervalprotrainerapp.databinding.ItemTrainingBinding
import com.example.intervalprotrainerapp.models.TimerTime
import com.example.intervalprotrainerapp.models.TrainingItem

class HomeAdapter : ListAdapter<TrainingItem, HomeAdapter.HomeViewHolder>(DIFF_CALLBACK) {
    var onItemClick: ((TrainingItem) -> Unit)? = null

    inner class HomeViewHolder(private val binding: ItemTrainingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(training: TrainingItem) {
            val context = binding.root.context
            val totalTime = training.intervalWork * training.cycles + training.intervalRelax + training.cycles - 1

            binding.nameTraining.text = training.name
            binding.cycles.text = "${TimerTime(totalTime).returnTime()}"


            when(training.color) {
                0 -> binding.trainingLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.card1))
                1 -> binding.trainingLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.card2))
                2 -> binding.trainingLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.card3))
                3 -> binding.trainingLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.card4))
                4 -> binding.trainingLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.card5))
                5 -> binding.trainingLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.card6))
                6 -> binding.trainingLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.card7))
                7 -> binding.trainingLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.card8))
            }

            binding.root.setOnClickListener {
                onItemClick?.invoke(training)
            }

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