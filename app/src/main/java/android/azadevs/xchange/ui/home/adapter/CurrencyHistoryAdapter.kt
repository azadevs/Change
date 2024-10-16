package android.azadevs.xchange.ui.home.adapter

import android.azadevs.xchange.databinding.ItemCurrencyHistoryBinding
import android.azadevs.xchange.ui.model.CurrencyHistoryItem
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by : Azamat Kalmurzaev
 * 11/10/24
 */
class CurrencyHistoryAdapter :
    ListAdapter<CurrencyHistoryItem, CurrencyHistoryAdapter.CurrencyHistoryViewHolder>(
        DiffUtilCurrency()
    ) {

    inner class CurrencyHistoryViewHolder(val binding: ItemCurrencyHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: CurrencyHistoryItem) {
            binding.apply {
                tvDate.text = item.date
                tvBuyingPrice.text = item.nbuBuyPrice
                tvSellingPrice.text = item.nbuCellPrice
                tvTime.text = item.time
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyHistoryViewHolder {
        return CurrencyHistoryViewHolder(
            ItemCurrencyHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CurrencyHistoryViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    companion object {
        class DiffUtilCurrency : DiffUtil.ItemCallback<CurrencyHistoryItem>() {
            override fun areItemsTheSame(
                oldItem: CurrencyHistoryItem,
                newItem: CurrencyHistoryItem
            ): Boolean {
                return oldItem.date == newItem.date
            }

            override fun areContentsTheSame(
                oldItem: CurrencyHistoryItem,
                newItem: CurrencyHistoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}