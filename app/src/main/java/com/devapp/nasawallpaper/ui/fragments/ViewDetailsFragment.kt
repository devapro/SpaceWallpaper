package com.devapp.nasawallpaper.ui.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.devapp.nasawallpaper.R
import com.devapp.nasawallpaper.logic.viewmodels.ViewDetailsViewModel

class ViewDetailsFragment : Fragment() {

    companion object {
        fun newInstance() =
            ViewDetailsFragment()
    }

    private lateinit var viewModel: ViewDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.view_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ViewDetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
