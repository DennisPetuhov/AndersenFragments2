package com.example.andersenfragments2.Presenter

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.andersenfragments2.Data.Cat
import com.example.andersenfragments2.R
import com.example.andersenfragments2.databinding.FragmentFirstBinding
import kotlinx.coroutines.launch

class FirstFragment : Fragment(), ClickFirstFragment {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SharedViewModel by activityViewModels()
    private val adapter = CatAdapter(mutableListOf(), this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeChangesOfListOfCat()
        initRecycler()
        findCatByTextViaSearchView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun findCatByTextViaSearchView() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.queryFlow.value = newText
                return true
            }
        })

    }

    private fun observeChangesOfListOfCat() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.listCat.collect {
                    adapter.addData(it)
                }

            }
        }
    }

    private fun initRecycler() {
        with(binding) {
            recyclerview.adapter = adapter
            recyclerview.layoutManager = LinearLayoutManager(requireContext())
            recyclerview.addItemDecoration(CatDecoration(requireContext(), R.drawable.border))
        }
    }

    override fun onClick(cat: Cat) {
        viewModel.updateFlowOfSingleCat(cat)
        navigateToSecondFragment()
    }

    override fun deleteCat(cat: Cat) {
        myAlertDialog(cat)
    }

    private fun navigateToSecondFragment() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
            && !isTablet(requireContext())
        ) {
            findNavController().navigate(
                R.id.action_firstFragment_to_secondFragment
            )
        }

    }

    private fun myAlertDialog(cat: Cat) {
        val builder = AlertDialog.Builder(requireContext())

        builder.setMessage("Do you want to delete cat ${cat.name.uppercase()} ?")
        builder.setTitle("Alert !")
        builder.setCancelable(false)
        builder.setPositiveButton("Yes") { dialog, which ->
            viewModel.deleteCat(cat)
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.cancel()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun isTablet(context: Context): Boolean {
        return context.resources
            .configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    companion object {
        fun newInstance() = FirstFragment()
    }


}