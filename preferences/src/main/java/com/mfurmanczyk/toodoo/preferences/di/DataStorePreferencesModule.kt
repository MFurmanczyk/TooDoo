package com.mfurmanczyk.toodoo.preferences.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.mfurmanczyk.toodoo.preferences.di.annotation.DataStorePreferences
import com.mfurmanczyk.toodoo.preferences.keys.PreferencesProperties
import com.mfurmanczyk.toodoo.preferences.repository.DataStorePreferencesRepository
import com.mfurmanczyk.toodoo.preferences.repository.PreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

val Context.dataStorePreferences : DataStore<Preferences> by preferencesDataStore(name = PreferencesProperties.NAME)

@InstallIn(SingletonComponent::class)
@Module
class DataStorePreferencesModule {

    @Provides
    fun provideDataStorePreferences(@ApplicationContext context: Context) : DataStore<Preferences> =
        context.dataStorePreferences

    @Provides
    @DataStorePreferences
    fun provideDataStorePreferencesRepository(dataStore: DataStore<Preferences>) : PreferencesRepository =
        DataStorePreferencesRepository(dataStore)
}