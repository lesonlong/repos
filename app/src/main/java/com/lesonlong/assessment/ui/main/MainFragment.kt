package com.lesonlong.assessment.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lesonlong.assessment.R
import com.lesonlong.assessment.databinding.MainFragmentBinding
import com.lesonlong.assessment.util.autoCleared

class MainFragment : Fragment(R.layout.main_fragment) {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels()
    private var binding by autoCleared<MainFragmentBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MainFragmentBinding.bind(view)
        setupView()
        subscribeUi()

        viewModel.loadData()
    }

    private fun setupView() {
        binding?.ivImage?.setOnClickListener {
            viewModel.onImageClicked()
        }
        binding?.btnRetry?.setOnClickListener {
            viewModel.onRetryClicked()
        }
    }

    private fun subscribeUi() {

        viewModel.movie.observe(viewLifecycleOwner) { movie ->
            binding?.tvTitle?.text = movie.title
        }

        viewModel.bitmap.observe(viewLifecycleOwner) { bitmap ->
            binding?.ivImage?.setImageBitmap(bitmap)
        }

        viewModel.progress.observe(viewLifecycleOwner) { text ->
            binding?.tvDownloading?.text = text
        }

        viewModel.hasError.observe(viewLifecycleOwner) { hasError ->
            binding?.ivImage?.isEnabled = hasError != true
            binding?.btnRetry?.visibility = if (hasError == true) View.VISIBLE else View.GONE
        }
    }
}