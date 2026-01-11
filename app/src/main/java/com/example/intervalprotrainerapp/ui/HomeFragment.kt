package com.example.intervalprotrainerapp.ui

import android.content.Intent
import android.graphics.Rect
import android.os.Build
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.intervalprotrainerapp.R
import com.example.intervalprotrainerapp.databinding.FragmentHomeBinding
import com.example.intervalprotrainerapp.models.TrainingItem
import com.example.intervalprotrainerapp.service.TimerService

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeAdapter = HomeAdapter()


    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = listOf(
            TrainingItem(0, "Бег", 50, 40, 3),
            TrainingItem(1, "Жим", 50, 40, 3, 1),
            TrainingItem(2, "Тяга", 50, 40, 3, 2),
            TrainingItem(3, "Кардио", 50, 40, 3, 3),
            TrainingItem(4, "Кардио", 50, 40, 3, 4),
            TrainingItem(5, "Кардио", 50, 40, 3, 5),
            TrainingItem(6, "Кардио", 50, 40, 3, 6),
            TrainingItem(7, "Кардио", 50, 40, 3, 7),
            )

        binding.trainingList.apply {
            adapter = homeAdapter
            layoutManager = GridLayoutManager(requireContext(),
                2,
                GridLayoutManager.VERTICAL,
                false)

            val spacing = (12 * resources.displayMetrics.density).toInt()
            addItemDecoration(SpacesItemDecoration(spacing))
        }

        homeAdapter.submitList(list)

        homeAdapter.onItemClick = { training ->
            navigateToSecondFragment(training)
        }

        binding.floatingActionButton.setOnClickListener {
//            Log.e("lifeCycle", "start_counter")
//            val intent = Intent(requireContext(), TimerService::class.java).apply {
//                action = TimerService.ACTION_START
//                putExtra("end_value", 60)
//            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                requireContext().startForegroundService(intent)
//            } else {
//                requireContext().startService(intent)
//            }
        }

    }

    fun navigateToSecondFragment(training: TrainingItem) {
        val fragment = TrainingFragment().apply {
            arguments = Bundle().apply {
                putParcelable("training", training)
            }
        }

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.itemFragment, fragment)
            .addToBackStack("first") // Добавляем в стек возврата
            .commit()
    }

}

class SpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = space / 2
        outRect.right = space / 2
        outRect.top = space / 4
        outRect.bottom = space / 2
    }
}