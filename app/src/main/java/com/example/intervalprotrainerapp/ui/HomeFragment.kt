package com.example.intervalprotrainerapp.ui

import android.graphics.Rect
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.intervalprotrainerapp.R
import com.example.intervalprotrainerapp.databinding.FragmentHomeBinding
import com.example.intervalprotrainerapp.models.TrainingItem

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
            TrainingItem(0, "Beg", 50, 40, 3),
            TrainingItem(1, "Beg", 50, 40, 3),
            TrainingItem(2, "Beg", 50, 40, 3),
            TrainingItem(3, "Beg", 50, 40, 3),
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