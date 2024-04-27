package com.dicoding.asclepius.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.dicoding.asclepius.R

import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.view.history.HistoryActivity
import com.dicoding.asclepius.view.result.ResultActivity
import com.yalantis.ucrop.UCrop
import java.io.File

class MainActivity : AppCompatActivity(), ImageClassifierHelper.ClassifierListener  {
    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null
    private var currentLabel: String? = null
    private var currentConfidenceScore: String? = null

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if(uri != null) {
            startUCrop(uri)
        } else {
            Log.d("Photo Picker", "No Image Selected")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.apply {
            galleryButton.setOnClickListener {
                startGallery()
            }

            analyzeButton.setOnClickListener {
                analyzeImage()
            }
        }
    }

    private fun startUCrop(sourceUri: Uri) {
        val destinationUri = Uri.fromFile(
            File(this.cacheDir, "image_cropped_${System.currentTimeMillis()}.jpg")
        )
        val uCropOptions = UCrop.Options()
        uCropOptions.setCompressionFormat(Bitmap.CompressFormat.JPEG)
        UCrop.of(sourceUri, destinationUri).withOptions(uCropOptions).start(this)
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

    }

    private fun showImage() {
        binding.apply {
            previewImageView.setImageURI(currentImageUri)
        }
    }

    private fun analyzeImage() {
        if(currentImageUri == null) {
            showToast("No Image Selected")
            return
        }

        val imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = this
        )
        imageClassifierHelper.classifyStaticImage(currentImageUri!!)
    }

    override fun onError(error: String) {
        showToast(error)
    }

    override fun onResults(label: String, confidenceScore: String) {
        currentLabel = label
        currentConfidenceScore = confidenceScore

        moveToResult()
    }

    private fun moveToResult() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_LABEL, currentLabel)
        intent.putExtra(ResultActivity.EXTRA_CONFIDENCE_SCORE, currentConfidenceScore)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, currentImageUri)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                UCrop.REQUEST_CROP -> {
                    val croppedImageUri = data?.let { UCrop.getOutput(it) }
                    currentImageUri = croppedImageUri
                    showImage()
                }
            }
        }
    }

    private fun moveToHistory() {
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_history, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.history_menu -> {
                moveToHistory()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}