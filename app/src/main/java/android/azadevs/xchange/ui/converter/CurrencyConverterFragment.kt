package android.azadevs.xchange.ui.converter

import android.annotation.SuppressLint
import android.azadevs.xchange.R
import android.azadevs.xchange.databinding.FragmentCurrencyConverterBinding
import android.azadevs.xchange.ui.converter.adapter.CurrencySpinnerAdapter
import android.azadevs.xchange.ui.converter.viewmodel.CurrencyConverterViewModel
import android.azadevs.xchange.ui.model.CurrencyDisplayItem
import android.azadevs.xchange.ui.utils.UiState
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Locale

/**
 * Created by : Azamat Kalmurzaev
 * 13/10/24
 */
@AndroidEntryPoint
class CurrencyConverterFragment : Fragment(R.layout.fragment_currency_converter) {

    private var _binding: FragmentCurrencyConverterBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<CurrencyConverterViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCurrencyConverterBinding.bind(view)

        observeCurrenciesFlow()

        binding.ivConverter.setOnClickListener {
            convertCurrency()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun convertCurrency() {
        val topCurrency = binding.topCurrencySpinner.selectedItem as CurrencyDisplayItem
        val bottomCurrency = binding.bottomCurrencySpinner.selectedItem as CurrencyDisplayItem
        val amount = binding.edtAmount.text.toString().toDoubleOrNull()
        if (amount != null) {
            val convertedAmount =
                topCurrency.cbPrice.toDouble() / bottomCurrency.cbPrice.toDouble() * amount
            binding.tvResult.text = "${topCurrency.code} $amount = ${
                String.format(Locale.getDefault(), "%.2f", convertedAmount)
            } ${bottomCurrency.code}"
        } else {
            Toast.makeText(requireContext(), "Invalid amount", Toast.LENGTH_SHORT).show()
        }

    }

    private fun observeCurrenciesFlow() {
        viewModel.currenciesFlow
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .distinctUntilChanged()
            .onEach { state ->
                when (state) {
                    is UiState.Error -> {
                        Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                    }

                    UiState.Loading -> {}

                    is UiState.Success -> {
                        configureTopSpinner(state.data)
                        configureBottomSpinner(state.data)
                    }
                    else -> {}
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun configureTopSpinner(
        currencies: List<CurrencyDisplayItem>
    ) {
        val adapter = CurrencySpinnerAdapter(
            requireContext(), currencies
        )
        binding.topCurrencySpinner.adapter = adapter
        binding.topCurrencySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    binding.topCurrencySpinner.setSelection(position, true)
                    binding.tvBuyingPrice.text =
                        currencies[position].nbuBuyPrice.ifEmpty { "N/A" }
                    binding.tvSellingPrice.text =
                        currencies[position].nbuCellPrice.ifEmpty { "N/A" }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
    }

    private fun configureBottomSpinner(
        currencies: List<CurrencyDisplayItem>
    ) {
        val adapter = CurrencySpinnerAdapter(requireContext(), currencies)
        binding.bottomCurrencySpinner.adapter = adapter
        binding.bottomCurrencySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    binding.bottomCurrencySpinner.setSelection(position, true)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}