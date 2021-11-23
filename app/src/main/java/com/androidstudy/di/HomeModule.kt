package com.androidstudy.di

import android.content.Context
import com.androidstudy.repository.CharacterRepository
import com.androidstudy.repository.CharacterRepositoryImpl
import com.androidstudy.service.CharacterService
import com.androidstudy.service.domain.CharacterDB
import com.androidstudy.service.local.CharacterDAO
import com.androidstudy.service.local.CharacterRemoteKeyDAO
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    abstract fun providesCharacterRepository(repository: CharacterRepositoryImpl): CharacterRepository
}

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = CharacterDB.getDatabase(appContext)

    @Provides
    fun provideCharacterDao(database: CharacterDB): CharacterDAO = database.characterDao()

    @Provides
    fun provideCharacterRemoteKeysDao(database: CharacterDB): CharacterRemoteKeyDAO = database.characterRemoteKeyDao()

    @Provides
    fun providesHomeService(okHttpClient: OkHttpClient): CharacterService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CharacterService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder().apply {
            interceptors().add(httpLoggingInterceptor)
        }.build()

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private const val BASE_URL = "https://rickandmortyapi.com/api/" // TODO PLEASE move to buildVariants in build.gradle
}
