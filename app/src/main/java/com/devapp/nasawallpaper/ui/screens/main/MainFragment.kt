package com.devapp.nasawallpaper.ui.screens.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.devapp.nasawallpaper.*
import com.devapp.nasawallpaper.logic.usecases.GetImageUseCase
import com.devapp.nasawallpaper.logic.usecases.SetRateUseCase
import com.devapp.nasawallpaper.ui.NavigationFragment
import com.devapp.nasawallpaper.utils.imageLoader.GlideDrawableLoader
import com.devapp.nasawallpaper.utils.observe
import com.devapp.nasawallpaper.utils.observeNonNull
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : NavigationFragment() {

    companion object {
        fun newInstance() =
            MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val nav = findNavController()
        val app = (requireActivity().application as App)

        val loader = GlideDrawableLoader(app.applicationContext)
        val getImageUseCase = GetImageUseCase(app.dataRepository, app.downloadController, loader)
        val setRateUseCase = SetRateUseCase(app.dataRepository)
        viewModel = ViewModelProviders.of(this, MainViewModel.createFactory(app, getImageUseCase, setRateUseCase, app.dataRepository, nav)).get(
            MainViewModel::class.java)

        setTitle(getString(R.string.app_name))

        imagesList.actionListener = viewModel.getImageListener()

        viewModel.pagedList.observeNonNull(viewLifecycleOwner){
            if(it.size > 0){
                emptyListInfo.visibility = GONE
            }
            else {
                emptyListInfo.visibility = VISIBLE
            }
            imagesList.submitList(it)
        }

        app.appController.getErrorInfo().observe(viewLifecycleOwner){
            if(!TextUtils.isEmpty(it)){
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_settings -> {
                val action =
                    MainFragmentDirections.actionMainFragmentToSettingsFragment()
                findNavController().navigate(action)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
