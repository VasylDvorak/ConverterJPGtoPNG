package com.example.converterjpgtopng.ui.presenters

import com.example.converterjpgtopng.ui.interfaces.IScreens
import com.example.converterjpgtopng.ui.interfaces.MainView
import com.github.terrakok.cicerone.Router

import moxy.MvpPresenter

class MainPresenter (val router: Router, val screens: IScreens) :
    MvpPresenter<MainView>() {
    override fun onFirstViewAttach () {
        super.onFirstViewAttach()
        router.replaceScreen(screens.imageConverter())
    }

    fun backClicked () {
        router.exit()
    }
}
