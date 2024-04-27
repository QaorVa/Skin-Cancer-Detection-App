package com.dicoding.asclepius.view.result

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.dicoding.asclepius.databinding.ActivityResultBinding
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.model.Article

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var viewModel: ResultViewModel
    private lateinit var adapter: ArticleAdapter

    private var label: String? = ""
    private var confidenceScore: String? = ""
    private var imageUri: Uri? = null
    private var isSaved = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setIntent()

        setActions()

        setUI()

        setAdapter()

        setRecyclerView()

        setViewModel()
    }


    @SuppressLint("SetTextI18n")
    private fun setActions() {
        binding.apply {
            btSave.setOnClickListener {
                if(!isSaved) {
                    viewModel.addHistory(
                        label ?: "",
                        confidenceScore ?: "",
                        imageUri
                    )
                    showToast("Result Has Been Saved!")
                    isSaved = true
                    btSave.text = "Result Saved!"
                    btSave.setBackgroundColor(resources.getColor(R.color.gray, null))
                }

            }
        }
    }

    private fun ImageView.setImage(uri: Uri?) {
        Glide.with(this).load(uri).apply(RequestOptions()).into(this)
    }

    @SuppressLint("SetTextI18n")
    private fun setUI() {
        binding.apply {
            resultText.text = label
            resultConfidenceScore.text = confidenceScore
            if(label == "Cancer") {
                resultDesc.text = getString(R.string.cancer)
                resultText.setTextColor(resources.getColor(R.color.red, null))
                resultConfidenceScore.setTextColor(resources.getColor(R.color.red, null))
            } else {
                resultDesc.text = getString(R.string.non_cancer)
                resultText.setTextColor(resources.getColor(R.color.blue, null))
                resultConfidenceScore.setTextColor(resources.getColor(R.color.blue, null))
            }
            resultImage.setImage(imageUri)

            if(isSaved) {
                btSave.text = "Result Saved"
                btSave.setBackgroundColor(resources.getColor(R.color.gray, null))
            }
        }
    }

    private fun setIntent() {
        label = intent.getStringExtra(EXTRA_LABEL)
        confidenceScore = intent.getStringExtra(EXTRA_CONFIDENCE_SCORE)
        imageUri = intent.getParcelableExtra(EXTRA_IMAGE_URI)
        isSaved = intent.getBooleanExtra(EXTRA_IS_SAVED, false)
    }

    private fun setAdapter() {
        adapter = ArticleAdapter(object : ArticleAdapter.OnItemClickCallback {

            override fun onItemClicked(data: Article) {
                val uri = Uri.parse(data.url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
        })

    }

    private fun setRecyclerView() {
        with(binding) {
            val layoutManager = LinearLayoutManager(this@ResultActivity, LinearLayoutManager.HORIZONTAL, false)
            rvArticle.layoutManager = layoutManager
            rvArticle.adapter = adapter
        }
    }

    private fun setViewModel() {

        viewModel = ViewModelProvider(this)[ResultViewModel::class.java]

        viewModel.setArticles()

        viewModel.getArticles().observe(this) { articleList ->
            articleList?.let {
                adapter.submitList(articleList)
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.isError.observe(this) {
            showError(it)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if(isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showError(isError: Boolean) {
        if(isError) {
            binding.ivError.visibility = View.VISIBLE
        } else {
            binding.ivError.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_LABEL = "extra_label"
        const val EXTRA_CONFIDENCE_SCORE = "extra_confidence_score"
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_IS_SAVED = "extra_is_saved"
    }

}