package com.dicoding.asclepius.view.history

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.data.model.History
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.view.result.ResultActivity

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var adapter: HistoryAdapter
    private lateinit var viewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAdapter()

        setRecyclerView()

        setViewModel()
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this)[HistoryViewModel::class.java]

        viewModel.getAllHistory()?.observe(this) {listHistory ->
            if(listHistory != null) {
                adapter.submitList(listHistory)
            }
        }
    }

    private fun setRecyclerView() {
        with(binding) {
            val layoutManager = LinearLayoutManager(this@HistoryActivity)
            rvHistory.layoutManager = layoutManager
            rvHistory.setHasFixedSize(true)
            val itemDecoration = DividerItemDecoration(this@HistoryActivity, layoutManager.orientation)
            rvHistory.addItemDecoration(itemDecoration)
            rvHistory.adapter = adapter
        }
    }

    private fun setAdapter() {
        adapter = HistoryAdapter(object: HistoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: History) {
                val intent = Intent(this@HistoryActivity, ResultActivity::class.java)
                intent.putExtra(ResultActivity.EXTRA_LABEL, data.label)
                intent.putExtra(ResultActivity.EXTRA_CONFIDENCE_SCORE, data.confidence)
                intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, data.imageUri)
                intent.putExtra(ResultActivity.EXTRA_IS_SAVED, true)
                startActivity(intent)
            }
        }, object : HistoryAdapter.OnDeleteClickListener {
            override fun onDeleteClicked(data: History) {
                viewModel.deleteHistory(data.id)
            }

        })
    }

}