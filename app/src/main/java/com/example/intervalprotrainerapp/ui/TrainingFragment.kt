package com.example.intervalprotrainerapp.ui

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import com.example.intervalprotrainerapp.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.transition.Visibility
import com.example.intervalprotrainerapp.MainActivity
import com.example.intervalprotrainerapp.databinding.FragmentTrainingBinding
import com.example.intervalprotrainerapp.models.TimerTime
import com.example.intervalprotrainerapp.models.TrainingItem
import com.example.intervalprotrainerapp.service.TimerService

class TrainingFragment : Fragment() {

    private lateinit var training: TrainingItem
    private lateinit var progressBar: CustomProgressBarView


    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent?.action) {
                TimerService.ACTION_TIMER_STATE -> {
                    val timerState = intent.getBooleanExtra("is_running", false)
                    updateUi(training, timerState)
                }
                TimerService.ACTION_TIMER_UPDATE -> {
                    val progress = intent.getIntExtra(TimerService.EXTRA_COUNT, 1)
                    val shares = intent.getIntExtra(TimerService.EXTRA_INTERVAL, 10)
                    binding.customProgressBar.apply {
                        updateCountShares(shares)
                        setProgress(progress)
                        setTimer(shares - progress)
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
                IntentFilter().apply {
                    addAction(TimerService.ACTION_TIMER_STATE)
                    addAction(TimerService.ACTION_TIMER_UPDATE)
                }
            )
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
        training = arguments?.getParcelable<TrainingItem>("training")!!

        progressBar = binding.customProgressBar
        updateUi(training, false)
        setupItems(training)
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(requireContext())
            .unregisterReceiver(broadcastReceiver)
    }

    /** NO WORKEEEEEEEEEEEEEED buttonPause*/
    private fun setupItems(training: TrainingItem) {
        binding.titleTraining.text = training.name
        binding.countCycles.text = "Количество кругов: ${training.cycles}"
        binding.countWork.text = "Время работы: ${training.intervalWork}"
        binding.countRelax.text = "Время отдыха: ${training.intervalRelax}"

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


        binding.buttonStop.setOnClickListener {
            val intent = Intent(requireContext(), TimerService::class.java).apply {
                action = TimerService.ACTION_STOP
                putExtra("training", training)
            }
            requireContext().startService(intent)
        }
    }

    private fun updateUi(training: TrainingItem, state: Boolean) {
        if(state) {
            binding.buttonStart.visibility = View.GONE
            binding.buttonStop.visibility = View.VISIBLE

            when(training.color) {
                0 -> {
                    binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard1))
                    binding.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card1))
                    binding.buttonStop.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card1))
                }
                1 -> {
                    view?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard2))
                    binding.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card2))
                    binding.buttonStop.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card2))

                }
                2 -> {
                    view?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard3))
                    binding.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card3))
                    binding.buttonStop.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card3))

                }
                3 -> {
                    view?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard4))
                    binding.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card4))
                    binding.buttonStop.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card4))

                }
                4 -> {
                    view?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard5))
                    binding.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card5))
                    binding.buttonStop.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card5))

                }
                5 -> {
                    view?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard6))
                    binding.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card6))
                    binding.buttonStop.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card6))

                }
                6 -> {
                    view?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard7))
                    binding.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card7))
                    binding.buttonStop.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card7))

                }
                7 -> {
                    view?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard8))
                    binding.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card8))
                    binding.buttonStop.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card8))
                }
            }
        } else {
            binding.buttonStart.visibility = View.VISIBLE
            binding.buttonStop.visibility = View.GONE

            progressBar.setTimer(training.intervalWork)
            progressBar.updateCountShares(training.intervalWork)
            progressBar.setProgress(0)
            when(training.color) {
                0 -> {
                    binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card1))
                    binding.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard1))
                    binding.buttonStart.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard1))
                    progressBar.chooseColor(CustomProgressBarColors.RED)
                }
                1 -> {
                    view?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card2))
                    binding.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard2))
                    binding.buttonStart.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard2))
                    progressBar.chooseColor(CustomProgressBarColors.PURPLE)

                }
                2 -> {
                    view?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card3))
                    binding.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard3))
                    binding.buttonStart.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard3))
                    progressBar.chooseColor(CustomProgressBarColors.BLUE)

                }
                3 -> {
                    view?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card4))
                    binding.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard4))
                    binding.buttonStart.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard4))
                    progressBar.chooseColor(CustomProgressBarColors.SKY)

                }
                4 -> {
                    view?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card5))
                    binding.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard5))
                    binding.buttonStart.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard5))
                    progressBar.chooseColor(CustomProgressBarColors.ORANGE)

                }
                5 -> {
                    view?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card6))
                    binding.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard6))
                    binding.buttonStart.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard6))
                    progressBar.chooseColor(CustomProgressBarColors.GREEN)

                }
                6 -> {
                    view?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card7))
                    binding.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard7))
                    binding.buttonStart.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard7))
                    progressBar.chooseColor(CustomProgressBarColors.YELLOW)

                }
                7 -> {
                    view?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card8))
                    binding.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard8))
                    binding.buttonStart.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard8))
                    progressBar.chooseColor(CustomProgressBarColors.LITE_GREEN)
                }
            }
        }
    }
}