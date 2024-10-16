package android.azadevs.xchange.ui.home.pager.adapter

import android.azadevs.xchange.ui.home.pager.CurrencyPagerFragment
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Created by : Azamat Kalmurzaev
 * 07/10/24
 */
class ViewpagerAdapter(fragment: Fragment, private val items: List<String>) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun createFragment(position: Int): Fragment {
        return CurrencyPagerFragment.newInstance(items[position])
    }
}