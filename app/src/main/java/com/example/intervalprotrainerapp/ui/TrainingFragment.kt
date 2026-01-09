package com.example.intervalprotrainerapp.ui

import com.example.intervalprotrainerapp.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.intervalprotrainerapp.databinding.FragmentTrainingBinding

class TrainingFragment : Fragment() {

    private var _binding: FragmentTrainingBinding? = null
    private val binding get() = _binding!!

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
        val color = arguments?.getString("color",
            "1")

        when(color?.toInt()) {
            0 -> view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card1))
            1 -> view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card2))
            2 -> view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card3))
            3 -> view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card4))
        }

    }


}