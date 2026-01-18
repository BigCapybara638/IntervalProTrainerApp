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
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.intervalprotrainerapp.R
import com.example.intervalprotrainerapp.databinding.FragmentHomeBinding
import com.example.intervalprotrainerapp.models.TrainingItem
import com.example.intervalprotrainerapp.service.TimerService
import kotlinx.coroutines.launch

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

        viewModel.loadData()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.trainingList.collect { state ->
                when(state) {
                    is DataState.Error -> println("Error")
                    is DataState.Loading -> {
                        println("loading")
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is DataState.Success -> {
                        homeAdapter.submitList(state.data)
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }

        binding.trainingList.apply {
            adapter = homeAdapter
            layoutManager = GridLayoutManager(requireContext(),
                2,
                GridLayoutManager.VERTICAL,
                false)

            val spacing = (12 * resources.displayMetrics.density).toInt()
            addItemDecoration(SpacesItemDecoration(spacing))
        }

        homeAdapter.onItemClick = { training ->
            navigateToSecondFragment(training)
        }

        binding.floatingActionButton.setOnClickListener {
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
            .addToBackStack(fragment.javaClass.simpleName) // Добавляем в стек возврата
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