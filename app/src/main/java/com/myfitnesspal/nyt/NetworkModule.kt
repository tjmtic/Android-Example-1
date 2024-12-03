package com.myfitnesspal.nyt;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

val loggingInterceptor = HttpLoggingInterceptor().apply {
level =HttpLoggingInterceptor.Level.BODY
    }

@Provides
@Singleton
fun provideHttpClient():OkHttpClient {
    return OkHttpClient.Builder()
            .readTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
}

@Provides
@Singleton
fun provideBaseRetrofitInstance(okHttpClient:OkHttpClient):Retrofit {
    return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}

    @Provides
    @Singleton
    fun provideArticleApi(retrofit: Retrofit): ArticleApi {
        return retrofit.create(ArticleApi::class.java)
    }


    @Provides
    @Singleton
    fun provideArticleRepository(articleApi: ArticleApi): ArticleRepository {
        return ArticleRepositoryImpl(articleApi)
    }
}