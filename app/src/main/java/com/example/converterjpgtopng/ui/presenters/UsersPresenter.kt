package com.example.converterjpgtopng.ui.presenters


import android.annotation.SuppressLint
import com.example.converterjpgtopng.ui.AndroidScreens

import com.example.converterjpgtopng.ui.interfaces.IScreens
import com.example.converterjpgtopng.ui.interfaces.ConverterView
import com.github.terrakok.cicerone.Router

import moxy.MvpPresenter

class UsersPresenter(var router: Router, var screens: IScreens) :
    MvpPresenter<ConverterView>() {



    @SuppressLint("SuspiciousIndentation")
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()

    }

    fun goToImageConverter() {
        router.navigateTo(AndroidScreens().imageConverter())
    }


    fun backPressed(): Boolean {
        router.replaceScreen(screens.imageConverter())
        return true
    }
}
