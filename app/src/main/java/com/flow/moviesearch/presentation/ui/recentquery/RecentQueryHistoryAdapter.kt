package com.flow.moviesearch.presentation.ui.recentquery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.flow.moviesearch.databinding.ItemRecentQueryHistoryBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecentQueryHistoryAdapter(
    private val itemClickListener: (String) -> Unit
) : RecyclerView.Adapter<RecentQueryHistoryAdapter.RecentQueryHistoryItemViewHolder>() {

    private var queryList: List<String> = emptyList()
    private fun setList(newList: List<String>) {
        queryList = newList
    }

    suspend fun updateList(newList: List<String>) {
        withContext(Dispatchers.Default) {
            val diffRes = DiffUtil.calculateDiff(RecentQueryHistoryDiffUtilCallback(queryList, newList))
            withContext(Dispatchers.Main) {
                setList(newList)
                diffRes.dispatchUpdatesTo(this@RecentQueryHistoryAdapter)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecentQueryHistoryItemViewHolder {
        return RecentQueryHistoryItemViewHolder.from(parent, itemClickListener)
    }

    override fun onBindViewHolder(holder: RecentQueryHistoryItemViewHolder, position: Int) {
        holder.bind(queryList[position])
    }

    override fun getItemCount(): Int = queryList.size

    class RecentQueryHistoryItemViewHolder(
        private val binding: ItemRecentQueryHistoryBinding,
        val itemClickListener: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root)  {
        companion object {
            fun from(
                parent: ViewGroup,
                itemClickListener: (String) -> Unit
            ): RecentQueryHistoryItemViewHolder {
                return RecentQueryHistoryItemViewHolder(
                    ItemRecentQueryHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                    itemClickListener
                )
            }
        }

        fun bind(query: String) {
            binding.query = query
            binding.holder = this
        }
    }
}