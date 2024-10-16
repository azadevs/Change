package android.azadevs.xchange.core.di

import android.azadevs.xchange.domain.usecase.GetCurrenciesUseCase
import android.azadevs.xchange.domain.usecase.GetCurrencyByCodeUseCase
import android.azadevs.xchange.domain.usecase.GetCurrencyWithDatesUseCase
import android.azadevs.xchange.domain.usecase.GetSearchCurrencyByCodeUseCase
import android.azadevs.xchange.domain.usecase.ProdGetCurrenciesUseCase
import android.azadevs.xchange.domain.usecase.ProdGetCurrencyByCodeUseCase
import android.azadevs.xchange.domain.usecase.ProdGetCurrencyWithDatesUseCase
import android.azadevs.xchange.domain.usecase.ProdSearchCurrencyByCodeUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by : Azamat Kalmurzaev
 * 04/10/24
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    @Singleton
    abstract fun bindGetCurrenciesUseCase(
        useCase: ProdGetCurrenciesUseCase
    ): GetCurrenciesUseCase

    @Binds
    @Singleton
    abstract fun bindGetCurrencyByCode(
        useCase: ProdGetCurrencyByCodeUseCase
    ): GetCurrencyByCodeUseCase

    @Binds
    @Singleton
    abstract fun bindSearchCurrencyByCode(
        useCase: ProdSearchCurrencyByCodeUseCase
    ): GetSearchCurrencyByCodeUseCase

    @Binds
    @Singleton
    abstract fun bindGetCurrencyWithDates(
        useCase: ProdGetCurrencyWithDatesUseCase
    ): GetCurrencyWithDatesUseCase


}