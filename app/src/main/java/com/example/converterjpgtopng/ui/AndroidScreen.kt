package com.example.converterjpgtopng.ui

import com.example.converterjpgtopng.ui.imageconverter.ConverterFragmentJPGtoPNG
import com.example.converterjpgtopng.ui.interfaces.IScreens
import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.FragmentScreen


class AndroidScreens : IScreens {


    override fun imageConverter(): Screen =
        FragmentScreen { ConverterFragmentJPGtoPNG() }
}



