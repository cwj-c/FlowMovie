package com.flow.moviesearch.presentation.ui.moviesearch

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.flow.moviesearch.R
import com.flow.moviesearch.databinding.ActivityMovieSearchBinding
import com.flow.moviesearch.presentation.ui.base.BindingActivity
import com.flow.moviesearch.presentation.ui.extension.repeatOnStarted
import com.flow.moviesearch.presentation.ui.extension.setSoftKeyboardVisible
import com.flow.moviesearch.presentation.ui.extension.showToast
import com.flow.moviesearch.presentation.ui.recentquery.RecentQueryHistoryActivity
import com.flow.moviesearch.presentation.viewmodel.MovieSearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieSearchActivity : BindingActivity<ActivityMovieSearchBinding>() {

    override val layoutResId: Int = R.layout.activity_movie_search
    private val viewModel: MovieSearchViewModel by viewModels()
    private val searchAdapter: MovieSearchAdapter by lazy { MovieSearchAdapter(viewModel::movieClickEvent) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView()
        setListener()
        observeViewModel()
    }

    private fun bindView() {
        binding.viewModel = viewModel
        binding.adapter = searchAdapter
        binding.editorActionListener = object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                when (actionId) {
                    EditorInfo.IME_ACTION_SEARCH -> {
                        viewModel.inputFinishEvent(v?.text.toString())
                        return true
                    }
                }
                return false
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListener() {
        binding.rvSearch.setOnTouchListener { _, event ->
            when (event?.action) {
                MotionEvent.ACTION_UP -> viewModel.backgroundTouchEvent()
            }
            return@setOnTouchListener false
        }

        binding.rvSearch.addOnScrollListener(pagingListener)
    }

    private val pagingListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            // direction => Vertically 기준으로 -1이 위쪽, 1이 아래쪽
            when {
                !recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE -> {
                    recyclerView.adapter?.itemCount?.let {
                        if(it != 0){
                            viewModel.nextPageEvent()
                        }
                    }
                }
            }
        }
    }

    private fun observeViewModel() {
        repeatOnStarted {
            launch {
                viewModel.uiState.collect {
                    when (it) {
                        is MovieSearchViewModel.UiState.ShowToast -> showToast(it.message)

                        is MovieSearchViewModel.UiState.KeyboardShown -> setSoftKeyboardVisible(binding.root, it.visible)

                        is MovieSearchViewModel.UiState.NavigateMovieUrl -> startActivity(
                            Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
                        )

                        is MovieSearchViewModel.UiState.NavigateRecentQueryHistory -> navigateHistoryView.launch(
                            RecentQueryHistoryActivity.get(this@MovieSearchActivity)
                        )
                    }
                }
            }

            launch {
                viewModel.movieSearchList.collectLatest {
                    searchAdapter.updateList(it)
                }
            }
        }
    }

    private val navigateHistoryView = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.data == null || it.resultCode != Activity.RESULT_OK)
            return@registerForActivityResult
        it.data!!.extras?.getString(RecentQueryHistoryActivity.KEY_EXTRA_HISTORY)?.let { q ->
            viewModel.inputFinishEvent(q)
        }
    }

}