package com.clara.timeticket.di

import android.content.Context
import android.content.SharedPreferences
import com.clara.timeticket.utils.PreferenceHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object PreferenceModule {
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(PreferenceHelper.PREFERENCES_NAME, Context.MODE_PRIVATE)
    }
}