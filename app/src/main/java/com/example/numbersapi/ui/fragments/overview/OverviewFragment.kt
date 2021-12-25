package com.example.numbersapi.ui.fragments.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.numbersapi.databinding.FragmentOverviewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OverviewFragment : Fragment() {
    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<OverviewFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewBinding.inflate(layoutInflater, container, false)


        binding.titleTextView.text = args.result.number.toString()
        binding.descriptionTextView.text = args.result.text

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}