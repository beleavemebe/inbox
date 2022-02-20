package io.github.beleavemebe.inbox.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.github.beleavemebe.inbox.core.repository.TaskRepository
import io.github.beleavemebe.inbox.data.db.TaskDatabase
import io.github.beleavemebe.inbox.data.db.TaskDatabase.Companion.DATABASE_NAME
import io.github.beleavemebe.inbox.data.repository.TaskRepositoryImpl
import javax.inject.Singleton

@Module
class DataModule {
    @Provides
    @Singleton
    fun provideTaskDatabase(context: Context): TaskDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            TaskDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    @Reusable
    fun provideTaskRepository(
        database: TaskDatabase,
    ): TaskRepository {
        return TaskRepositoryImpl(database)
    }
}
