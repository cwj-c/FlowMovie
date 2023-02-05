package com.flow.moviesearch.presentation.ui.recentquery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.flow.moviesearch.R
import com.flow.moviesearch.databinding.ActivityRecentQueryHistoryBinding
import com.flow.moviesearch.presentation.ui.base.BindingActivity
import com.flow.moviesearch.presentation.ui.extension.repeatOnStarted
import com.flow.moviesearch.presentation.viewmodel.RecentQueryHistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecentQueryHistoryActivity : BindingActivity<ActivityRecentQueryHistoryBinding>() {
    companion object {
        const val KEY_EXTRA_HISTORY = "key_extra_history"
        fun get(context: Context): Intent {
            return Intent(context, RecentQueryHistoryActivity::class.java)
        }
    }
    override val layoutResId: Int = R.layout.activity_recent_query_history

    private val viewModel: RecentQueryHistoryViewModel by viewModels()
    private val historyAdapter: RecentQueryHistoryAdapter by lazy { RecentQueryHistoryAdapter(viewModel::historyClickEvent) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView()
        supportActionBar?.title = getString(R.string.recent_query_history)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        observeViewModel()
    }

    private fun bindView() {
        binding.viewModel = viewModel
        binding.adapter = historyAdapter
    }

    private fun observeViewModel() {
        repeatOnStarted {
            launch {
                viewModel.uiState.collect {
                    when (it) {
                        is RecentQueryHistoryViewModel.UiState.FinishView -> {
                            it.message?.let { message ->
                                AlertDialog.Builder(this@RecentQueryHistoryActivity)
                                    .setMessage(message)
                                    .setPositiveButton(R.string.close
                                    ) { dialog, _ ->
                                        dialog.dismiss()
                                        finish()
                                    }
                                    .setCancelable(false)
                                    .create()
                                    .show()
                            }
                        }

                        is RecentQueryHistoryViewModel.UiState.HistoryClick -> {
                            setResult(Activity.RESULT_OK, Intent().apply {
                                putExtra(KEY_EXTRA_HISTORY, it.query)
                            })
                            finish()
                        }
                    }
                }
            }

            launch {
                viewModel.history.collectLatest {
                    historyAdapter.updateList(it)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}