package com.adrenastudies.camera.Camera

import android.media.CamcorderProfile
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.adrenastudies.camera.R
import com.adrenastudies.camera.models.QualitySize
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraPhotoV2 : Fragment() {

    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService

    private var qualityLow    = QualitySize(320, 240, CamcorderProfile.QUALITY_QVGA)
    private var qualityMEDIUM = QualitySize(720, 480, CamcorderProfile.QUALITY_480P)
    private var qualityHIGH   = QualitySize(1280, 720, CamcorderProfile.QUALITY_720P)

    private lateinit var btnTakePhoto: ImageView
    private lateinit var viewFinder: PreviewView

    private fun setResolutionForCameraPreview(resolution:Size):Preview {
        return Preview.Builder().setTargetResolution(resolution).build().also { it.setSurfaceProvider(viewFinder.surfaceProvider) }
    }

    private fun setResolutionForPhoto(resolution:Size):ImageCapture {
        return ImageCapture.Builder().setTargetResolution(resolution).build()
    }

    private fun initCamera(resolution: QualitySize) {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(Runnable {

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val cameraPreview = setResolutionForCameraPreview(Size(resolution.width, resolution.height))
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            imageCapture = setResolutionForPhoto(Size(resolution.width, resolution.height))

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, cameraPreview, imageCapture)
            } catch(exc: Exception) {
                Log.e("TAG", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun getOutputDirectory(): File {
        val mediaDir = requireContext().externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireContext().filesDir
    }

    private fun takePhoto() {
        if(imageCapture != null) {

            val photoFile = File(getOutputDirectory(), SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US).format(System.currentTimeMillis()) + ".jpg")

            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

            imageCapture!!.takePicture(outputOptions, ContextCompat.getMainExecutor(requireContext()), object : ImageCapture.OnImageSavedCallback {

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo capture succeeded: $savedUri"
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                }

                override fun onError(exc: ImageCaptureException) {
                    Log.e("TAG", "Photo capture failed: ${exc.message}", exc)
                }

            })
        }else{
            Toast.makeText(requireContext(), "Error al inicializar la camara", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getResolution() {
        //TODO Si la resolucion no es soportada por el device la libreria se encarga de buscar una similar
        // Puede elegir entre cualquiera de las 3 declaradas
        initCamera(qualityHIGH)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_camera_photo_v2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getResolution()

        btnTakePhoto = view.findViewById(R.id.btnTakePhoto)
        viewFinder = view.findViewById(R.id.viewFinder)

        btnTakePhoto.setOnClickListener { takePhoto() }
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

}