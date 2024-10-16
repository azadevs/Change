package android.azadevs.xchange.ui.home.pager

import android.azadevs.xchange.R
import android.azadevs.xchange.databinding.FragmentCurrencyPagerBinding
import android.azadevs.xchange.ui.home.pager.viewmodel.CurrencyPagerViewModel
import android.azadevs.xchange.ui.model.CurrencyDisplayItem
import android.azadevs.xchange.ui.utils.Constants.ARG_CURRENCY_CODE
import android.azadevs.xchange.ui.utils.UiState
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Created by : Azamat Kalmurzaev
 * 07/10/24
 */
@AndroidEntryPoint
class CurrencyPagerFragment : Fragment(R.layout.fragment_currency_pager) {

    private var _binding: FragmentCurrencyPagerBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<CurrencyPagerViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCurrencyPagerBinding.bind(view)

        observeCurrentCurrencyFlow()

    }

    private fun observeCurrentCurrencyFlow() {
        viewModel.currentCurrencyFlow
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .distinctUntilChanged()
            .onEach { state ->
                when (state) {
                    is UiState.Error -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                    }

                    UiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is UiState.Success -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        configureUi(state.data)
                    }
                    else -> {}
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun configureUi(data: CurrencyDisplayItem) {
        binding.apply {
            tvCurrencyUpdatedDate.text = data.date
            tvBuyingPrice.text = data.nbuBuyPrice
            tvSellingPrice.text = data.nbuCellPrice
            Glide.with(requireContext()).load(data.currencyImageUri).into(ivCurrencyImage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(currencyName: String) = CurrencyPagerFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_CURRENCY_CODE, currencyName)
            }
        }
    }
}