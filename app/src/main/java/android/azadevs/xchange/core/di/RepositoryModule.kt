package android.azadevs.xchange.core.di

import android.azadevs.xchange.data.repository.ProdMainRepository
import android.azadevs.xchange.domain.repository.MainRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by : Azamat Kalmurzaev
 * 03/10/24
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMainRepository(
        mainRepositoryImpl: ProdMainRepository
    ): MainRepository


}