package com.devapp.nasawallpaper.ui.screens.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.devapp.nasawallpaper.App
import com.devapp.nasawallpaper.R
import com.devapp.nasawallpaper.logic.usecases.GetImageUseCase
import com.devapp.nasawallpaper.ui.NavigationFragment
import com.devapp.nasawallpaper.utils.imageLoader.GlideDrawableLoader
import com.devapp.nasawallpaper.utils.extensions.observe
import kotlinx.android.synthetic.main.fragment_view_details.*

class ViewDetailsFragment : NavigationFragment() {

    private val viewModel by viewModels<ViewDetailsViewModel>(){
        val app = (requireActivity().application as App)
        val loader = GlideDrawableLoader(app.applicationContext)
        val useCase = GetImageUseCase(app.dataRepository, app.downloadController, loader)
        ViewDetailsViewModel.ViewModelFactory(app, app.dataRepository, useCase)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val args =
            ViewDetailsFragmentArgs.fromBundle(
                requireArguments()
            )
        viewModel.imageId = args.imageId

        displayHome()
        setTitle(getString(R.string.app_name))

        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedTitleTextAppearance)

        viewModel.imageInfo.observe(viewLifecycleOwner){
            it?.let {
                collapsingToolbar.title = it.name
                description.text = it.description
            }
        }

        viewModel.imageDrawable.observe(viewLifecycleOwner){ image.setImageDrawable(it) }
    }

}
