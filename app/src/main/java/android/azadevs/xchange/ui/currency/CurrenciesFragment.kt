package android.azadevs.xchange.ui.currency

import android.azadevs.xchange.R
import android.azadevs.xchange.databinding.FragmentCurrenciesBinding
import android.azadevs.xchange.ui.currency.adapter.CurrencyAdapter
import android.azadevs.xchange.ui.currency.viewmodel.CurrenciesViewModel
import android.azadevs.xchange.ui.utils.UiState
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Created by : Azamat Kalmurzaev
 * 09/10/24
 */
@AndroidEntryPoint
class CurrenciesFragment : Fragment(R.layout.fragment_currencies) {

    private var _binding: FragmentCurrenciesBinding? = null

    private val binding get() = _binding!!

    private val viewmodel by viewModels<CurrenciesViewModel>()

    private val adapter by lazy {
        CurrencyAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCurrenciesBinding.bind(view)

        observeCurrenciesFlow()

        configureMenuSearchIcon()

        binding.rvCurrency.adapter = adapter

        observeSearchFlow()

    }

    private fun observeCurrenciesFlow() {
        viewmodel.currenciesFlow
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                when (state) {
                    is UiState.Error -> {
                        Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                        binding.progressBar.visibility = View.INVISIBLE
                    }

                    UiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is UiState.Success -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        adapter.submitList(state.data)
                    }

                    else -> {}
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchFlow() {
        viewmodel.searchCurrenciesFlow
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .debounce(300)
            .distinctUntilChanged()
            .onEach {
                adapter.submitList(it)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun configureMenuSearchIcon() {
        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_search, menu)
                    val searchItem = menu.findItem(R.id.search)
                    val searchView = searchItem.actionView as SearchView
                    searchView.queryHint = getString(R.string.text_search_hint)

                    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            if (query != null) {
                                viewmodel.searchCurrencyByCode(query)
                            }
                            return true
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            if (newText != null) {
                                viewmodel.searchCurrencyByCode(newText)
                            }
                            return true
                        }
                    })
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return menuItem.itemId == R.id.search
                }
            },
            viewLifecycleOwner, Lifecycle.State.RESUMED
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}