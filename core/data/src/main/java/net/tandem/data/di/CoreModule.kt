package net.tandem.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.tandem.data.AppConfig
import net.tandem.data.database.AppDatabase
import net.tandem.data.database.dao.CommunityDao
import net.tandem.data.network.ApiClient
import net.tandem.data.network.RetrofitConfig
import javax.inject.Singleton

/**
 * provides class instances to be used in other modules
 * */
@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideApiClient(retrofitConfig: RetrofitConfig): ApiClient {
        retrofitConfig.initialize()
        return retrofitConfig.createService(ApiClient::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext, AppDatabase::class.java, AppConfig.DATABASE_NAME
        ).addCallback(object : RoomDatabase.Callback() {}).build()
    }

    @Provides
    @Singleton
    fun provideCommunityDao(database: AppDatabase): CommunityDao {
        return database.communityDao()
    }

}