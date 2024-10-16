package android.azadevs.xchange.ui.currency.adapter

import android.azadevs.xchange.databinding.ItemCurrencyBinding
import android.azadevs.xchange.ui.model.CurrencyDisplayItem
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

/**
 * Created by : Azamat Kalmurzaev
 * 09/10/24
 */
class CurrencyAdapter :
    ListAdapter<CurrencyDisplayItem, CurrencyAdapter.CurrencyViewHolder>(diffUtil) {

    inner class CurrencyViewHolder(private val binding: ItemCurrencyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: CurrencyDisplayItem) {
            binding.apply {
                tvCurrencyName.text = data.code
                tvBuyingPrice.text = data.nbuBuyPrice.ifEmpty { "N/A" }
                tvSellingPrice.text = data.nbuCellPrice.ifEmpty { "N/A" }
                Glide.with(binding.root.context).load(data.currencyImageUri).into(ivCurrencyImage)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        return CurrencyViewHolder(
            ItemCurrencyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CurrencyDisplayItem>() {
            override fun areItemsTheSame(
                oldItem: CurrencyDisplayItem,
                newItem: CurrencyDisplayItem
            ): Boolean {
                return oldItem.code == newItem.code
            }

            override fun areContentsTheSame(
                oldItem: CurrencyDisplayItem,
                newItem: CurrencyDisplayItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}