package com.example.intervalprotrainerapp.presentation.ui.training

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
import androidx.navigation.fragment.findNavController
import com.example.intervalprotrainerapp.databinding.FragmentTrainingBinding
import com.example.intervalprotrainerapp.domain.models.TrainingItem
import com.example.intervalprotrainerapp.data.service.TimerService
import com.example.intervalprotrainerapp.presentation.ui.customviews.CustomProgressBarColors
import com.example.intervalprotrainerapp.presentation.ui.customviews.CustomProgressBarView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Singleton

@AndroidEntryPoint
class TrainingFragment : Fragment() {

    private var _binding: FragmentTrainingBinding? = null
    private val binding get() = _binding!!
    private lateinit var training: TrainingItem
    private lateinit var progressBar: CustomProgressBarView
    private var progress = 1
    private var shares = 10

    private var cycle = 1
    private var timerState: String = "WORK"
    private var isRunning: Boolean = false

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent?.action) {
                TimerService.ACTION_TIMER_STATE -> {
                    isRunning = intent.getBooleanExtra("is_running", false)
                    updateUi(training, isRunning)
                }
                TimerService.ACTION_TIMER_UPDATE -> {
                    progress = intent.getIntExtra(TimerService.EXTRA_COUNT, 1)
                    shares = intent.getIntExtra(TimerService.EXTRA_INTERVAL, 10)
                    cycle = intent.getIntExtra(TimerService.EXTRA_CYCLE, 1)
                    timerState = intent.getStringExtra("state").toString()

                    binding.customProgressBar.apply {
                        updateCountShares(shares)
                        setProgress(progress)
                        setTimer(shares - progress)
                    }
                }
            }
        }
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
        training = arguments?.getParcelable(TimerService.EXTRA_TRAINING)!!

        progressBar = binding.customProgressBar
        isRunning = savedInstanceState?.getBoolean("timerState") ?: false

        updateUi(training, isRunning)
        setupItems(training)

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

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

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(requireContext())
            .unregisterReceiver(broadcastReceiver)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("training", training)
        outState.putBoolean("timerState", isRunning)
        outState.putInt(TimerService.EXTRA_CYCLE, cycle)
        outState.putString("state", timerState)
        outState.putInt("progress", progress)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        val intent = Intent(requireContext(), TimerService::class.java).apply {
            action = TimerService.ACTION_STOP
        }
        requireContext().startService(intent)
    }

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

        binding.buttonSkip.setOnClickListener {
            val intent = Intent(requireContext(), TimerService::class.java).apply {
                action = TimerService.ACTION_SKIP
            }
            requireContext().startService(intent)

        }
    }

    private fun updateUi(training: TrainingItem, state: Boolean) {
        if(state) {
            binding.buttonStart.visibility = View.GONE
            binding.buttonStop.visibility = View.VISIBLE
            binding.buttonSkip.visibility = View.VISIBLE
            binding.buttonBack.visibility = View.GONE

            when(training.color) {
                0 -> {
                    binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard1))
                    binding.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card1))
                    binding.buttonStop.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card1))
                    binding.buttonSkip.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card1))

                }
                1 -> {
                    view?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard2))
                    binding.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card2))
                    binding.buttonStop.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card2))
                    binding.buttonSkip.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card2))

                }
                2 -> {
                    view?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard3))
                    binding.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card3))
                    binding.buttonStop.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card3))
                    binding.buttonSkip.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card3))

                }
                3 -> {
                    view?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard4))
                    binding.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card4))
                    binding.buttonStop.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card4))
                    binding.buttonSkip.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card4))


                }
                4 -> {
                    view?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard5))
                    binding.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card5))
                    binding.buttonStop.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card5))
                    binding.buttonSkip.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card5))

                }
                5 -> {
                    view?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard6))
                    binding.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card6))
                    binding.buttonStop.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card6))
                    binding.buttonSkip.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card6))

                }
                6 -> {
                    view?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard7))
                    binding.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card7))
                    binding.buttonStop.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card7))
                    binding.buttonSkip.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card7))

                }
                7 -> {
                    view?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blackcard8))
                    binding.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card8))
                    binding.buttonStop.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card8))
                    binding.buttonSkip.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card8))
                }

            }
        } else {
            binding.buttonStart.visibility = View.VISIBLE
            binding.buttonStop.visibility = View.GONE
            binding.buttonSkip.visibility = View.GONE
            binding.buttonBack.visibility = View.VISIBLE


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