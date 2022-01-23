package net.tandem.component.di

import android.app.Application
import net.tandem.data.database.AppDatabase
import net.tandem.data.network.ApiClient
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.tandem.data.database.dao.CommunityDao


@EntryPoint
@InstallIn(SingletonComponent::class)
interface CoreModuleDependencies {

    fun provideApplication(): Application

    fun provideApiClient(): ApiClient

    fun provideDatabase(): AppDatabase

    fun provideCommunityDao(): CommunityDao

}