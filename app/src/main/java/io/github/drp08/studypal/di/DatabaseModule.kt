package io.github.drp08.studypal.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.drp08.studypal.db.ClientDatabase
import io.github.drp08.studypal.db.daos.SessionDao
import io.github.drp08.studypal.db.daos.SubjectDao
import io.github.drp08.studypal.db.daos.TopicDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideRoomInstance(
        @ApplicationContext applicationContext: Context
    ): ClientDatabase {
        return Room.databaseBuilder(
            applicationContext,
            ClientDatabase::class.java,
            "clientDb"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideSubjectDao(db: ClientDatabase): SubjectDao {
        return db.subjectDao
    }

    @Provides
    @Singleton
    fun provideSessionDao(db: ClientDatabase): SessionDao {
        return db.sessionDao
    }

    @Provides
    @Singleton
    fun provideTopicDao(db: ClientDatabase): TopicDao {
        return db.topicDao
    }
}