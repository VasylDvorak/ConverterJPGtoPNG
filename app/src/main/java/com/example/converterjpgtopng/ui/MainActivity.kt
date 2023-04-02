package com.example.converterjpgtopng.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.converterjpgtopng.App
import com.example.converterjpgtopng.R
import com.example.converterjpgtopng.databinding.ActivityMainBinding

import com.example.converterjpgtopng.ui.interfaces.BackButtonListener
import com.example.converterjpgtopng.ui.interfaces.MainView
import com.example.converterjpgtopng.ui.presenters.MainPresenter

import com.github.terrakok.cicerone.androidx.AppNavigator


import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
private const val REQUEST_CODE = 100
class MainActivity : MvpAppCompatActivity(), MainView {
    val navigator = AppNavigator( this , R.id.container)

  private val presenter  by  moxyPresenter { MainPresenter(App.instance.router, AndroidScreens()) }

    private var vb: ActivityMainBinding? = null
    override fun onCreate (savedInstanceState: Bundle ?) {
        super.onCreate(savedInstanceState)

        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb?.root)

        if (!(ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
                    )
        ) {
            askPermission()
        }

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
    private fun askPermission() {
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            if (!(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(
                    this@MainActivity,
                    "Please provide the required permissions",
                    Toast.LENGTH_SHORT
                ).show()
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}
