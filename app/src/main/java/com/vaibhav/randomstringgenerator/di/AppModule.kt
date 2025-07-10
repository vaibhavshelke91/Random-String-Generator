package com.vaibhav.randomstringgenerator.di

import android.content.Context
import com.vaibhav.randomstringgenerator.data.provider.StringProvider
import com.vaibhav.randomstringgenerator.data.repository.StringRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideStringProviders(@ApplicationContext context: Context):StringProvider{
        return StringProvider(context)
    }

    @Provides
    fun provideStringRepository(stringProvider: StringProvider):StringRepository{
        return StringRepository(stringProvider)
    }
}