package com.example.converterjpgtopng.ui

import android.os.Bundle
import com.example.converterjpgtopng.App
import com.example.converterjpgtopng.R
import com.example.converterjpgtopng.databinding.ActivityMainBinding

import com.example.converterjpgtopng.ui.interfaces.BackButtonListener
import com.example.converterjpgtopng.ui.interfaces.MainView
import com.example.converterjpgtopng.ui.presenters.MainPresenter

import com.github.terrakok.cicerone.androidx.AppNavigator


import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter

class MainActivity : MvpAppCompatActivity(), MainView {
    val navigator = AppNavigator( this , R.id.container)

  private val presenter  by  moxyPresenter { MainPresenter(App.instance.router, AndroidScreens()) }

    private var vb: ActivityMainBinding? = null
    override fun onCreate (savedInstanceState: Bundle ?) {
        super.onCreate(savedInstanceState)

        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb?.root)

    }
    override fun onResumeFragments () {
        super.onResumeFragments()
        App.instance.navigatorHolder.setNavigator(navigator)
    }
    override fun onPause () {
        super.onPause()
        App.instance.navigatorHolder.removeNavigator()
    }
    override fun onBackPressed () {
        supportFragmentManager.fragments.forEach {
            if (it is BackButtonListener && it.backPressed()){
                return
            }
        }

        presenter.backClicked()
    }
}
