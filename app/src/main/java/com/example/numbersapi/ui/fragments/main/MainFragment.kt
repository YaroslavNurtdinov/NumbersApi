package com.example.numbersapi.ui.fragments.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.numbersapi.R
import com.example.numbersapi.adapter.MainAdapter
import com.example.numbersapi.databinding.FragmentMainBinding
import com.example.numbersapi.util.NetworkResult
import com.example.numbersapi.util.hideKeyboard
import com.example.numbersapi.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()
    private val mAdapter by lazy { MainAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        setHasOptionsMenu(true)
        setupRecyclerView()

        mainViewModel.readNumbers.observe(requireActivity(), {
            mAdapter.setData(it.asReversed())
        })
        requestApiData()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.editText.text.clear()
    }

    private fun requestApiData() {
        binding.apply {
            btnRandomNumber.setOnClickListener {
                mainViewModel.getRandomNumber()
            }
            btnInputNumber.setOnClickListener {
                val inputNumber = binding.editText.text.toString()
                if (inputNumber.isNotEmpty()) {
                    mainViewModel.getInputNumber(Integer.parseInt(inputNumber))
                } else Toast.makeText(requireContext(), "Please, input Number", Toast.LENGTH_SHORT)
                    .show()
                hideKeyboard()
            }
        }

        mainViewModel.myResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    Toast.makeText(
                        requireContext(),
                        "${response.data?.number.toString()} has been added.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Error -> {
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {}
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_all) {
            mainViewModel.deleteAll()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = mAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }
}