package com.example.converterjpgtopng.ui.interfaces

import android.net.Uri
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface MVPInterfaceForConverter : MvpView {

    fun init()

    fun showOriginImage(uri: Uri)

    fun showConvertedImage(uri: Uri)

    fun buttonConvertCommandEnable()

    fun buttonConverJPGtoPNGDisabled()

    fun cancelConverting()

    fun waitting()

    fun inCaseError()

    fun showProgressBar()

    fun progressBarGone()

   fun showCancelDialog()

    fun hideCancelDialog()
}