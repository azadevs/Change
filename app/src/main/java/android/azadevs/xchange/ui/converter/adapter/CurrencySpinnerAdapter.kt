package android.azadevs.xchange.ui.converter.adapter

import android.azadevs.xchange.databinding.ItemCustomSpinnerBinding
import android.azadevs.xchange.ui.model.CurrencyDisplayItem
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bumptech.glide.Glide

/**
 * Created by : Azamat Kalmurzaev
 * 13/10/24
 */
class CurrencySpinnerAdapter(
    val context: Context,
    private val items: List<CurrencyDisplayItem>,
) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(p0: Int): Any = items[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        val binding: ItemCustomSpinnerBinding = if (view == null) {
            ItemCustomSpinnerBinding.inflate(LayoutInflater.from(context), viewGroup, false)
        } else {
            ItemCustomSpinnerBinding.bind(view)
        }
        binding.tvCurrencyName.text = items[position].code
        Glide.with(binding.root).load(items[position].currencyImageUri)
            .into(binding.ivCurrencyImage)

        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getView(position, convertView, parent)
    }
}