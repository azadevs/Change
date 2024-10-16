package android.azadevs.xchange.core.di

import android.azadevs.xchange.core.utils.Constants.BASE_URL
import android.azadevs.xchange.data.remote.service.ExchangeServiceApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by : Azamat Kalmurzaev
 * 03/10/24
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient = OkHttpClient.Builder().addNetworkInterceptor(
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    ).build()

    @Provides
    @Singleton
    fun provideRetrofitBuilder(
        client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    @Provides
    @Singleton
    fun provideExchangeServiceApi(
        retrofit: Retrofit
    ): ExchangeServiceApi = retrofit.create(ExchangeServiceApi::class.java)

}