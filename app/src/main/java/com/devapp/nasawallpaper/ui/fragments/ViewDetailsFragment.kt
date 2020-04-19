package com.devapp.nasawallpaper.ui.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.devapp.nasawallpaper.App
import com.devapp.nasawallpaper.R
import com.devapp.nasawallpaper.logic.viewmodels.ViewDetailsViewModel
import kotlinx.android.synthetic.main.fragment_view_details.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewDetailsFragment : NavigationFragment() {

    companion object {
        fun newInstance() =
            ViewDetailsFragment()
    }

    private lateinit var viewModel: ViewDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val args = ViewDetailsFragmentArgs.fromBundle(requireArguments())
        val app = (requireActivity().application as App)
        viewModel = ViewModelProviders.of(this, ViewDetailsViewModel.createFactory(app, app.dataRepository, app.downloadController)).get(ViewDetailsViewModel::class.java)
        viewModel.imageId = args.imageId

        displayHome()
        setTitle(getString(R.string.app_name))

        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedTitleTextAppearance)

        viewModel.imageInfo.observe(viewLifecycleOwner, Observer {
            collapsingToolbar.title = it.name
            description.text = it.description
        })
        viewModel.imageDrawable.observe(viewLifecycleOwner, Observer {
            image.setImageDrawable(it)
        })
    }

}
