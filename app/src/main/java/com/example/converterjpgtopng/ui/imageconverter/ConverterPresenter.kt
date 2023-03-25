package com.example.converterjpgtopng.ui.imageconverter

import android.net.Uri
import com.example.converterjpgtopng.entity.ConverterFromJpgToPng
import com.example.converterjpgtopng.ui.interfaces.MVPInterfaceForConverter
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import moxy.MvpPresenter

class ConverterPresenter(
    private val converter: ConverterFromJpgToPng,
    val router: Router
) : MvpPresenter<MVPInterfaceForConverter>() {

    var disposables = CompositeDisposable()

    fun backPressed(): Boolean {
        router.exit()
        return true
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    override fun onDestroy() {
        disposables.clear()
    }

    fun startConvertingJPGtoPNG(pictureUri: Uri) {
        viewState.apply {
            waitting()
            showCancelDialog()
            showProgressBar()
        }

        converter
            .convertRx(pictureUri)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Uri> {
                override fun onSubscribe(d: Disposable?) {
                    disposables.add(d)
                }

                override fun onSuccess(t: Uri?) {
                    if (t != null) {
                        SuccessConverting(t)
                    }
                }

                override fun onError(e: Throwable?) {
                    ErrorConverting(e)
                }
            })
    }


    private fun SuccessConverting(uri: Uri) {
        viewState.apply {
            showConvertedImage(uri)
            hideCancelDialog()
            progressBarGone()

        }
    }

    private fun ErrorConverting(e: Throwable?) {
        viewState.apply {
            progressBarGone()
            inCaseError()
            hideCancelDialog()
        }

    }

    fun cancelConvertingPicture() {
        Schedulers.shutdown()
        viewState.apply {
            progressBarGone()

            cancelConverting()
        }
        Schedulers.start()
    }

    fun selectJPGpicture(imageUri: Uri) {
        viewState.apply {
            buttonConvertCommandEnable()
            waitting()
            showOriginImage(imageUri)
        }
    }
}