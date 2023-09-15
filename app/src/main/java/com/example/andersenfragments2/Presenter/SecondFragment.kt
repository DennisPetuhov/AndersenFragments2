package com.example.andersenfragments2.Presenter

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.andersenfragments2.R
import com.example.andersenfragments2.databinding.FragmentSecondBinding
import com.example.twofragments.SharedViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)

class SecondFragment : Fragment() {


    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCatToSecondFragment()

        binding.button.setOnClickListener {
            sendCatToFirstFragment()
            navigateToFirstFragment()
        }


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun sendCatToFirstFragment() {
        val name = binding.nameSecond.text.toString()
        val surname = binding.surname.text.toString()
        val phone = binding.phone.text.toString().toInt()
        viewModel.SendCatToFirstFragment(name, surname, phone)
    }

    private fun getCatToSecondFragment() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.singleCat.collect {
                    if (it.id > 0) {
                        binding.nameSecond.setText(it.name)
                        binding.surname.setText(it.surname)
                        binding.phone.setText(it.phone.toString())
                    }
                }
            }
        }
    }

    private fun navigateToFirstFragment() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT && !isTablet(requireContext())) {
            findNavController().navigate(R.id.action_secondFragment_to_firstFragment)
        }
    }


    private fun isTablet(context: Context): Boolean {
        return context.resources
            .configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    companion object {
        fun newInstance() = SecondFragment()
    }


}