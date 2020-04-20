package com.devapp.nasawallpaper.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.devapp.nasawallpaper.ui.MainActivity
import kotlinx.android.synthetic.main.view_toolbar.*

// https://medium.com/@programmerr47/give-toolbar-to-each-fragment-52c3a996deb5
// https://medium.com/@programmerr47/fragment-navigation-for-application-e2a7d5b29709
open class NavigationFragment : Fragment(){
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                findNavController().popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    protected fun displayHome(){
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    protected fun setTitle(title: String){
        (activity as MainActivity).supportActionBar?.title = title
    }
}