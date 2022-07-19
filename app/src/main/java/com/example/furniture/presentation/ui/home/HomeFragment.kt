package com.example.furniture.presentation.ui.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.furniture.R
import com.example.furniture.data.api.SessionManager
import com.example.furniture.data.model.home.Category
import com.example.furniture.data.util.Resource
import com.example.furniture.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: HomeViewModelFactory

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var sessionManager: SessionManager

    @Inject
    lateinit var adapterValue: CustomHolderCategory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
        sessionManager = SessionManager(requireContext())
        sessionManager.setView(requireActivity())

        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.recCategory.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = adapterValue
        }

        adapterValue.setOnItemClickListener { item: Category ->
            Log.i("", "setOnItemClickListener category**** ")

        }

        displayItems()
    }

    private fun displayItems() {
        viewModel.getHome()
        viewModel.homeItem.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {

                        if (it.status) {
                            Log.i("Success", "category********* ${it.data.categories}")
                            if (it.data.categories.isNotEmpty())
                                adapterValue.differ.submitList(it.data.categories)
                            else
                                Toast.makeText(
                                    requireContext(),
                                    "لا يوجد اصناف",
                                    Toast.LENGTH_LONG
                                ).show()

                        }
                    }
                }
                is Resource.Error -> {
                    response.message?.let {
                        Toast.makeText(
                            context,
                            "An error occurred in home $it",
                            Toast.LENGTH_SHORT
                        ).show()

                        Log.i("", "error getHome********* $it")
                    }
                }
                is Resource.Loading -> {
                }
            }
        }
    }

}