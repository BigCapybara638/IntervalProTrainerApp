package com.example.intervalprotrainerapp.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import com.example.intervalprotrainerapp.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.intervalprotrainerapp.databinding.FragmentTrainingBinding
import com.example.intervalprotrainerapp.models.TrainingItem
import com.example.intervalprotrainerapp.service.TimerService

class TrainingFragment : Fragment() {

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent?.action) {
                TimerService.ACTION_TIMER_UPDATE -> {
                    val progress = intent.getIntExtra(TimerService.EXTRA_COUNT, 1)
                    val shares = intent.getIntExtra(TimerService.EXTRA_INTERVAL, 10)
                    binding.customProgressBar.apply {
                        updateCountShares(shares)
                        setProgress(progress)
                    }
                }
            }
        }
    }

    private var _binding: FragmentTrainingBinding? = null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(broadcastReceiver,
                IntentFilter(TimerService.ACTION_TIMER_UPDATE))
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTrainingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val training = arguments?.getParcelable<TrainingItem>("training")

        when(training?.color) {
            0 -> {
                view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card1))
                binding.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard1))
                binding.customProgressBar.chooseColor(CustomProgressBarColors.RED)
            }
            1 -> {
                view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card2))
                binding.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard2))
                binding.customProgressBar.chooseColor(CustomProgressBarColors.PURPLE)

            }
            2 -> {
                view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card3))
                binding.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard3))
                binding.customProgressBar.chooseColor(CustomProgressBarColors.BLUE)

            }
            3 -> {
                view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card4))
                binding.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard4))
                binding.customProgressBar.chooseColor(CustomProgressBarColors.SKY)

            }
            4 -> {
                view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card5))
                binding.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard5))
                binding.customProgressBar.chooseColor(CustomProgressBarColors.ORANGE)

            }
            5 -> {
                view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card6))
                binding.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard6))
                binding.customProgressBar.chooseColor(CustomProgressBarColors.GREEN)

            }
            6 -> {
                view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card7))
                binding.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard7))
                binding.customProgressBar.chooseColor(CustomProgressBarColors.YELLOW)

            }
            7 -> {
                view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card8))
                binding.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard8))
                binding.customProgressBar.chooseColor(CustomProgressBarColors.LITE_GREEN)

            }
        }

        binding.buttonStart.setOnClickListener {
            Log.e("lifeCycle", "start_counter")
            val intent = Intent(requireContext(), TimerService::class.java).apply {
                action = TimerService.ACTION_START
                putExtra("training", training)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                requireContext().startForegroundService(intent)
            } else {
                requireContext().startService(intent)
            }
        }

    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(requireContext())
            .unregisterReceiver(broadcastReceiver)
    }
}