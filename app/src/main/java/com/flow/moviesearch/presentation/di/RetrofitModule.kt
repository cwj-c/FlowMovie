package com.flow.moviesearch.presentation.di

import android.content.Context
import com.flow.moviesearch.BuildConfig
import com.flow.moviesearch.data.constant.MovieApiConstant
import com.flow.moviesearch.data.network.NaverClientInterceptor
import com.flow.moviesearch.data.network.NetworkConnectionInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    fun provideHttpLogInterceptor() = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    fun provideNetworkConnectionInterceptor(
        @ApplicationContext context: Context
    ): NetworkConnectionInterceptor = NetworkConnectionInterceptor(context)

    @Provides
    @Singleton
    fun provideNaverClientOkHttpClient(
        httpLogInterceptor: HttpLoggingInterceptor,
        networkConnectionInterceptor: NetworkConnectionInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(httpLogInterceptor)
        .addInterceptor(NaverClientInterceptor())
        .addInterceptor(networkConnectionInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideNaverSearchRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(MovieApiConstant.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

}

