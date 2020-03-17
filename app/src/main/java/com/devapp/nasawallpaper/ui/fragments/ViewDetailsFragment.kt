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
import kotlinx.android.synthetic.main.view_details_fragment.*
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
        return inflater.inflate(R.layout.view_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val args = ViewDetailsFragmentArgs.fromBundle(arguments!!)
        val app = (activity!!.application as App)
        viewModel = ViewModelProviders.of(this, ViewDetailsViewModel.createFactory(app, app.dataRepository, app.downloadController)).get(ViewDetailsViewModel::class.java)
        viewModel.imageId = args.imageId

        displayHome()
        setTitle(getString(R.string.app_name))

        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedTitleTextAppearance)

        viewModel.imageInfo.observe(viewLifecycleOwner, Observer {
            collapsingToolbar.title = it.name
            description.text = it.description
            GlobalScope.launch {
                withContext(Dispatchers.IO){
                    val drawable = viewModel.getImageDrawable()
                    drawable?.let {
                        image.post {
                            image.setImageDrawable(drawable)
                        }
                    }
                }
            }
        })
    }

}
