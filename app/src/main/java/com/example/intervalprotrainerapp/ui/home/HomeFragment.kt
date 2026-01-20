package com.example.intervalprotrainerapp.ui.home

import android.graphics.Rect
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.intervalprotrainerapp.R
import com.example.intervalprotrainerapp.databinding.FragmentHomeBinding
import com.example.intervalprotrainerapp.domain.models.TrainingItem
import kotlinx.coroutines.launch
import android.widget.Toast
import com.example.intervalprotrainerapp.ui.training.TrainingFragment

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeAdapter = HomeAdapter()

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

        setupRecyclerView()
        setupObservers()
    }

    /** [onDestroyView] - при уничтожении View удаляем View binding */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /** Настройка наблюдателей за [viewModel] */
    fun setupObservers() {
        /* lifecycleScope - скоуп связанный с жизненным циклом компонента
         (все корутины уничтожатся в onDestroy)
         viewLifecycleOwner - специально для Fragment, привязывает
         корутины к жц View фрагмента, а не к самому Fragment*/
        viewLifecycleOwner.lifecycleScope.launch {
            /* repeatOnLifecycle - корутина будет работать только в Lifecycle.State.STARTED
            * (уничтожатся в onStop)*/
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.trainingList.collect { state ->
                    when (state) {
                        is DataState.Error ->
                            Log.e("dataError", "Данные не были получены: ${state.message}")
                        is DataState.Loading -> {
                            Log.w("dataLoading", "Загрузка...")
                        }
                        is DataState.Success -> {
                            homeAdapter.submitList(state.data)
                        }
                    }
                }
            }
        }

        // общее состояние ошибки
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loadingState.collect { isLoading ->
                    binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
                }
            }
        }

        // общее состояние ошибки
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorState.collect { error ->
                    error?.let {
                        Toast.makeText(requireContext(), "Ошибка загрузки", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    /** Настройка RecyclerView (adapter, layoutManager, spacing).
     * Обработка нажатия на элемент*/
    fun setupRecyclerView() {
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
    }

    /** Навигация в [TrainingFragment] */
    fun navigateToSecondFragment(training: TrainingItem) {
        val fragment = TrainingFragment().apply {
            arguments = Bundle().apply {
                putParcelable("training", training)
            }
        }

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.itemFragment, fragment)
            .addToBackStack(null)
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