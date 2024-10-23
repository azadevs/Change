package android.azadevs.xchange.ui.home

import android.azadevs.xchange.R
import android.azadevs.xchange.core.utils.LocaleHelper
import android.azadevs.xchange.databinding.DialogLanguageBinding
import android.azadevs.xchange.databinding.FragmentHomeBinding
import android.azadevs.xchange.ui.MainActivity
import android.azadevs.xchange.ui.home.adapter.CurrencyHistoryAdapter
import android.azadevs.xchange.ui.home.pager.adapter.ViewpagerAdapter
import android.azadevs.xchange.ui.home.viewmodel.HomeViewModel
import android.azadevs.xchange.ui.model.CurrencyHistoryItem
import android.azadevs.xchange.ui.utils.Constants.LANGUAGE_ENGLISH
import android.azadevs.xchange.ui.utils.Constants.LANGUAGE_RUSSIAN
import android.azadevs.xchange.ui.utils.Constants.LANGUAGE_UZBEK
import android.azadevs.xchange.ui.utils.UiState
import android.azadevs.xchange.ui.utils.asString
import android.azadevs.xchange.ui.utils.getCurrencyCodes
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Created by : Azamat Kalmurzaev
 * 01/10/24
 */
@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private val viewmodel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        observeStateFlow()

        observeCurrencyWithDatesFlow()

        configureMenuLanguage()

    }

    private fun observeStateFlow() {
        viewmodel.currenciesFlow
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .distinctUntilChanged()
            .onEach { state ->
                when (state) {
                    is UiState.Error -> {
                        Snackbar.make(
                            binding.root,
                            state.message.asString(requireContext()),
                            Snackbar.LENGTH_LONG
                        ).show()
                        binding.frameLoading.visibility = View.INVISIBLE
                    }

                    UiState.Loading -> {
                        binding.frameLoading.visibility = View.VISIBLE
                    }

                    is UiState.Success -> {
                        binding.frameLoading.visibility = View.INVISIBLE
                        configureViewPagerWithTabs(getCurrencyCodes(state.data))
                    }

                    is UiState.Empty -> {
                        binding.frameLoading.visibility = View.INVISIBLE
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeCurrencyWithDatesFlow() {
        viewmodel.currencyWithDates
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .distinctUntilChanged()
            .onEach { state ->
                when (state) {
                    is UiState.Error -> {
                        binding.historyProgressBar.visibility = View.INVISIBLE
                        Snackbar.make(
                            binding.root,
                            state.message.asString(requireContext()),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                    UiState.Loading -> {
                        binding.historyProgressBar.visibility = View.VISIBLE
                    }

                    is UiState.Success -> {
                        binding.historyProgressBar.visibility = View.INVISIBLE
                        configureHistoryAdapter(state.data)
                    }

                    is UiState.Empty -> {
                        binding.historyProgressBar.visibility = View.INVISIBLE
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun configureHistoryAdapter(data: List<CurrencyHistoryItem>) {
        val adapter = CurrencyHistoryAdapter()
        adapter.submitList(data)
        binding.rvCurrenciesHistory.adapter = adapter
    }

    private fun configureViewPagerWithTabs(codes: List<String>) {
        binding.viewPager.adapter = ViewpagerAdapter(this, codes)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = codes[position]
        }.attach()

        binding.viewPager.registerOnPageChangeCallback(
            object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewmodel.getCurrencyWithDates(codes[position])
                }
            }
        )

        binding.dotsIndicator.attachToPager(binding.viewPager)
    }

    private fun selectedLanguageBackground(dialogBinding: DialogLanguageBinding) {
        val currentLocale = LocaleHelper.getLanguage(binding.root.context)
        when (currentLocale) {
            LANGUAGE_ENGLISH -> dialogBinding.rbEnglish.isChecked = true
            LANGUAGE_RUSSIAN -> dialogBinding.rbRussian.isChecked = true
            LANGUAGE_UZBEK -> dialogBinding.rbUzbek.isChecked = true
        }
    }

    private fun changeLanguageDialog() {
        val currentLocale = LocaleHelper.getLanguage(binding.root.context)
        val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        val dialogBinding =
            DialogLanguageBinding.inflate(LayoutInflater.from(requireContext()), null, false)
        selectedLanguageBackground(dialogBinding)
        builder.setView(dialogBinding.root)
        builder.setCanceledOnTouchOutside(false)
        dialogBinding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val language = when (checkedId) {
                R.id.rbEnglish -> {
                    LANGUAGE_ENGLISH
                }

                R.id.rbRussian -> LANGUAGE_RUSSIAN
                R.id.rbUzbek -> LANGUAGE_UZBEK
                else -> ""
            }
            if (language != currentLocale) {
                LocaleHelper.setLocale(binding.root.context, language)
                (activity as MainActivity).recreate()
                builder.dismiss()
            }
        }
        builder.show()
    }

    private fun configureMenuLanguage() {
        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_language, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return if (menuItem.itemId == R.id.language) {
                        changeLanguageDialog()
                        true
                    } else false

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