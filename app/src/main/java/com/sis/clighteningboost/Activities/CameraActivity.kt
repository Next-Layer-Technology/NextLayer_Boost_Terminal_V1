package com.sis.clighteningboost.Activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ScaleGestureDetector
import android.view.View
import android.view.animation.CycleInterpolator
import android.widget.Toast
import androidx.camera.core.AspectRatio.RATIO_4_3
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.sis.clighteningboost.R
import com.sis.clighteningboost.databinding.ActivityCameraBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : BaseActivity() {

    private var is1x1: Boolean = false
    private var capturedImageUri: String? = null

    private var imageCaptureBuilder = ImageCapture.Builder()
    private var imageCapture: ImageCapture? = null

    private lateinit var cameraExecutor: ExecutorService

    // Select back camera as a default
    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    private var _binding: ActivityCameraBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private var pressedTime: Long = 0

    enum class CameraAR {
        AR16x9, AR4x3, AR1x1
    }

    var currentCameraAR = CameraAR.AR16x9

    override fun onResume() {
        super.onResume()
        deleteAllPrivateFiles(this)
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_PERMISSIONS -> {
                var i = 0
                while (i < grantResults.size) {
                    if (grantResults.size > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        updateWithARSelectionAndStartCamera()
                    } else {
                        Toast.makeText(
                            this, "Please consider granting this permissions", Toast.LENGTH_LONG
                        ).show()
                    }
                    i++
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Request camera permissions
        if (allPermissionsGranted()) {
            updateWithARSelectionAndStartCamera()
        } else {
            requestPermissions(
                REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        binding.actionTakePhoto.setOnClickListener {
            animateShutterButton()
            getPreviewImage()
            previewCapturedImage()
        }
        binding.buttonYes.setOnClickListener {
            capturedImageUri?.toUri()?.let(::setResult)
        }
        binding.buttonNo.setOnClickListener {
            setVisibilityAfterCancelShot()
        }

        binding.buttonGallery.setOnClickListener {
//            findNavController().navigate(R.id.action_cameraFragment_to_galleryFragment) fixme: Need to complete
        }

        binding.buttonToggleCamera.setOnClickListener {
            flipCamera()
        }

        binding.buttonToggleAR.setOnClickListener {
            handleButtonARClickNew()
        }
    }

    private fun cropImage1x1(): String {
        //to get the image from the ImageView (say iv)
        val bitmap = binding.imageViewShot.drawable.toBitmap(2048, 2048)
        return saveToInternalStorage(bitmap)
    }

    private fun getPreviewImage() {
        val bitmap = binding.cameraView.bitmap
        if (bitmap != null) {
            capturedImageUri = saveToInternalStorage(bitmap)
        }
    }

    private fun saveToInternalStorage(bitmapImage: Bitmap): String {
        val cw = ContextWrapper(this.applicationContext)
        // path to /data/data/yourapp/app_data/imageDir
        val directory = cw.getDir("raw", Context.MODE_PRIVATE)
        // Create imageDir
        val mypath = File(directory, "original.jpg")
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return mypath.toUri().toString()
    }


    private fun handleButtonARClickNew() {
        currentCameraAR = when (currentCameraAR) {
            CameraAR.AR16x9 -> CameraAR.AR4x3
            CameraAR.AR4x3 -> CameraAR.AR1x1
            CameraAR.AR1x1 -> CameraAR.AR16x9
        }
        updateWithARSelectionAndStartCamera()
    }

    private fun updateWithARSelectionAndStartCamera() {
        when (currentCameraAR) {

            CameraAR.AR16x9 -> {
                (binding.cameraView.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio =
                    null
                (binding.imageViewShot.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio =
                    null
                binding.buttonToggleAR.setImageDrawable(
                    ContextCompat.getDrawable(
                        this, R.drawable.icon_size
                    )
                )
            }

            CameraAR.AR4x3 -> {
                (binding.cameraView.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio =
                    "3:4"
                (binding.imageViewShot.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio =
                    "3:4"
                binding.buttonToggleAR.setImageDrawable(
                    ContextCompat.getDrawable(
                        this, R.drawable.icon_size_3x4
                    )
                )
            }

            CameraAR.AR1x1 -> {
                (binding.cameraView.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio =
                    "1:1"
                (binding.imageViewShot.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio =
                    "1:1"
                binding.buttonToggleAR.setImageDrawable(
                    ContextCompat.getDrawable(
                        this, R.drawable.size_1x1
                    )
                )
            }
        }
        is1x1 = currentCameraAR == CameraAR.AR1x1

        startCamera()
    }

    private fun setVisibilityAfterCancelShot() {
        binding.imageViewShot.visibility = View.GONE
        binding.buttonYes.visibility = View.GONE
        binding.buttonNo.visibility = View.GONE

        binding.cameraView.visibility = View.VISIBLE
        binding.buttonToggleCamera.visibility = View.VISIBLE
        binding.buttonToggleAR.visibility = View.VISIBLE
    }

    private fun setVisibilityAfterShot() {
        binding.imageViewShot.visibility = View.VISIBLE
        binding.buttonYes.visibility = View.VISIBLE
        binding.buttonNo.visibility = View.VISIBLE

        binding.cameraView.visibility = View.GONE
        binding.buttonToggleCamera.visibility = View.GONE
        binding.buttonToggleAR.visibility = View.GONE
    }

    private fun flipCamera() {
        if (cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) cameraSelector =
            CameraSelector.DEFAULT_BACK_CAMERA
        else if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) cameraSelector =
            CameraSelector.DEFAULT_FRONT_CAMERA
        startCamera()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.cameraView.surfaceProvider)
            }
            imageCapture = imageCaptureBuilder.setTargetAspectRatio(RATIO_4_3).build()

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()
                // Bind use cases to camera
                val camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

                // Getting the CameraControl instance from the camera
                val cameraControl = camera.cameraControl

                // Getting the CameraInfo instance from the camera
                val cameraInfo = camera.cameraInfo

                // Listen to pinch gestures
                val listener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                    override fun onScale(detector: ScaleGestureDetector): Boolean {
                        // Get the camera's current zoom ratio
                        val currentZoomRatio = cameraInfo.zoomState.value?.zoomRatio ?: 0F

                        // Get the pinch gesture's scaling factor
                        val delta = detector.scaleFactor

                        // Update the camera's zoom ratio. This is an asynchronous operation that returns
                        // a ListenableFuture, allowing you to listen to when the operation completes.
                        cameraControl.setZoomRatio(currentZoomRatio * delta)

                        // Return true, as the event was handled
                        return true
                    }
                }
                val scaleGestureDetector = ScaleGestureDetector(this, listener)

                // Attach the pinch gesture listener to the viewfinder
                binding.cameraView.setOnTouchListener { _, event ->
                    scaleGestureDetector.onTouchEvent(event)
                    return@setOnTouchListener true
                }

//                cameraProvider.bindToLifecycle(
//                    this, cameraSelector, preview)

            } catch (exc: Exception) {
                Log.d(TAG, "Use case binding failed $exc")
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun animateShutterButton() {
        val animatorCompat =
            ViewCompat.animate(binding.actionTakePhoto).setDuration(500).scaleX(.9f).scaleY(.9f)
                .alpha(.5f).setInterpolator(CycleInterpolator(.5f))
        animatorCompat.withLayer().start()
    }

    private fun previewCapturedImage() {
        Glide.with(this).load(capturedImageUri).diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true).into(binding.imageViewShot)
        setVisibilityAfterShot()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            this, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    /*override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }*/

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun setResult(uri: Uri) {
        val intent = Intent()
        intent.data = uri
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    /**
     * User has cancelled the task
     */
    fun setResultCancel() {
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }


    private fun deleteAllPrivateFiles(context: Context) {
        val list = context.filesDir.listFiles()
        if (list != null) {
            for (i in list.indices) {
                list[i].delete()
            }
        }
    }


    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = mutableListOf(
            Manifest.permission.CAMERA,
        ).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }
}