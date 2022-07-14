package io.github.beleavemebe.inbox.tasks.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import io.github.beleavemebe.inbox.tasks.data.db.Converters
import io.github.beleavemebe.inbox.tasks.data.db.TaskDatabase
import io.github.beleavemebe.inbox.tasks.data.db.TaskDatabase.Companion.DATABASE_NAME
import io.github.beleavemebe.inbox.tasks.data.repository.TaskRepositoryImpl

@Module
object DataModule {
    @Provides
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
    fun provideTaskRepositoryImpl(
        database: TaskDatabase,
    ): TaskRepositoryImpl {
        return TaskRepositoryImpl(database)
    }
}
