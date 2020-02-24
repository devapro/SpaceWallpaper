package com.devapp.nasawallpaper.ui


import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import com.devapp.nasawallpaper.*
import com.devapp.nasawallpaper.logic.entity.EntityImage
import com.devapp.nasawallpaper.logic.viewmodels.MainViewModel
import com.devapp.nasawallpaper.ui.customview.ImagesListAdapter
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class MainFragment : NavigationFragment() {

    companion object {
        fun newInstance() = MainFragment()
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
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        setTitle(getString(R.string.app_name))

        val hasPermission = (activity as MainActivity)
            .permissionUtils
            .checkAndRequestPermissions(
                this,
                object : UtilPermission.PermissionCallback(Array(1){Permission.STORAGE_WRITE}){
                override fun onSuccessGrantedAll() {
                    init()
                }
            })
        if(hasPermission){
           init()
        }

        viewModel.pagedList.observe(
            this,
            Observer<PagedList<EntityImage>> { items -> imagesList.submitList(items) }
            )

        imagesList.setActionListener(object : ImagesListAdapter.ActionListener{
            override fun downloadImage(item: EntityImage) {
                GlobalScope.launch {
                    (activity?.application as App).downloadController.downloadImage(item)
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_settings -> {
                val action = MainFragmentDirections.actionMainFragmentToSettingsFragment()
                findNavController().navigate(action)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init(){
       activity?.run {
           (application as App).appController.checkUpdates()
       }
    }

}
