package com.example.converterjpgtopng.ui.imageconverter

import android.app.Activity
import android.content.Intent
import android.graphics.RenderEffect
import android.graphics.Shader
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.converterjpgtopng.App
import com.example.converterjpgtopng.R
import com.example.converterjpgtopng.databinding.FragmentConverterBinding
import com.example.converterjpgtopng.entity.ConverterFromJpgToPng
import com.example.converterjpgtopng.ui.interfaces.BackButtonListener
import com.example.converterjpgtopng.ui.interfaces.MVPInterfaceForConverter
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class ConverterFragmentJPGtoPNG : MvpAppCompatFragment(), MVPInterfaceForConverter,
    BackButtonListener {

    private var _binding: FragmentConverterBinding? = null
    private val binding get() = _binding!!
    private lateinit var alertDialog: AlertDialog
    private var URIpicture: Uri? = null
    private val presenter: ConverterPresenter by moxyPresenter {
        ConverterPresenter(
            ConverterFromJpgToPng(requireContext()),
            App.instance.router
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentConverterBinding.inflate(inflater, container, false).also { _binding = it }.root


    override fun backPressed(): Boolean = presenter.backPressed()

    override fun init() {

        initialStateOfView()

        binding.apply {
            buttonImageChoose.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/jpg"
                startActivityForResult(intent, 1000)
            }
            buttonConvertJpgPng.setOnClickListener {
                URIpicture?.let(presenter::startConvertingJPGtoPNG)
                showCancelDialog()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1000) {
            URIpicture = data?.data
            URIpicture?.let { presenter.selectJPGpicture(it) }
        }
    }

    override fun showOriginImage(uri: Uri) {
        binding.imgViewJpg.setImageURI(uri)
    }

    override fun showConvertedImage(uri: Uri) {
        binding.imgViewConvertedImg.setImageURI(uri)
    }

    override fun showProgressBar() {
        binding.progressBarCommon.visibility = View.VISIBLE
    }

    override fun progressBarGone() {
        binding.progressBarCommon.visibility = View.GONE
    }

    override fun inCaseError() {
        binding.imgViewConvertedImg.setImageURI(null)

    }

    override fun buttonConvertCommandEnable() {
        binding.buttonConvertJpgPng.isEnabled = true
    }

    override fun buttonConverJPGtoPNGDisabled() {
        binding.buttonConvertJpgPng.isEnabled = false
    }

    override fun cancelConverting() {
        binding.imgViewConvertedImg.setImageURI(null)

    }

    override fun waitting() {
        binding.imgViewConvertedImg.setImageURI(null)
        Thread.sleep(2000L)
    }

    private fun initialStateOfView() {
        buttonConverJPGtoPNGDisabled()
        progressBarGone()
        waitting()
    }

    override fun showCancelDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val blurEffect = RenderEffect.createBlurEffect(
                10f, 2f,
                Shader.TileMode.CLAMP
            )
            binding.converterFragment.setRenderEffect(blurEffect)
        }
        var builder = AlertDialog.Builder(requireContext())
        builder
            .setTitle(getString(R.string.title_dialog))
            .setIcon(android.R.drawable.ic_menu_info_details)
            .setMessage(getString(R.string.message_dialog))
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                binding.converterFragment.setRenderEffect(null)
                presenter.cancelConvertingPicture()
                dialog.cancel()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                binding.converterFragment.setRenderEffect(null)
                dialog.cancel()
            }
            .setOnDismissListener {
                binding.converterFragment.setRenderEffect(null)
            }
        alertDialog = builder.create()
        alertDialog.show()

    }

    override fun hideCancelDialog() {
        alertDialog.apply {
            cancel()
            hide()
            dismiss()
        }
    }
}