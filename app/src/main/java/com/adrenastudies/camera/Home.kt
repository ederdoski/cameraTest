package com.adrenastudies.camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

class Home: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnTakePhoto.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_home_to_photo)
        }

        btnTakeVideo.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_home_to_video)
        }
    }
}