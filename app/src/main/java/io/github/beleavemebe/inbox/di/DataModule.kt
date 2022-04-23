package io.github.beleavemebe.inbox.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.github.beleavemebe.inbox.core.repository.ChecklistRepository
import io.github.beleavemebe.inbox.core.repository.TaskRepository
import io.github.beleavemebe.inbox.data.db.Converters
import io.github.beleavemebe.inbox.data.db.TaskDatabase
import io.github.beleavemebe.inbox.data.db.TaskDatabase.Companion.DATABASE_NAME
import io.github.beleavemebe.inbox.data.repository.TaskRepositoryImpl
import javax.inject.Singleton

@Module
class DataModule {
    @Provides
    @Reusable
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideTaskDatabase(
        context: Context,
        converters: Converters,
    ): TaskDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            TaskDatabase::class.java,
            DATABASE_NAME
        )
            .addTypeConverter(converters)
            .build()
    }

    @Provides
    @Reusable
    fun provideTaskRepositoryImpl(
        database: TaskDatabase,
    ): TaskRepositoryImpl {
        return TaskRepositoryImpl(database)
    }
}
