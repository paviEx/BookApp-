package com.example.android.bookstore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.bookstore.R.layout.fragment_dashboard_fragment

class profile_fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view =inflater.inflate(R.layout.fragment_profile_fragment,container,false)
        return view
    }

}