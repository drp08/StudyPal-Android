package io.github.drp08.studypal.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.drp08.studypal.data.FriendRepositoryImpl
import io.github.drp08.studypal.data.SchedulingRepositoryImpl
import io.github.drp08.studypal.data.UserRepositoryImpl
import io.github.drp08.studypal.domain.FriendRepository
import io.github.drp08.studypal.domain.SchedulingRepository
import io.github.drp08.studypal.domain.UserRepository
import io.github.drp08.studypal.domain.scheduler.RandomiseScheduler
import io.github.drp08.studypal.domain.scheduler.Scheduler
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSchedulingRepository(impl: SchedulingRepositoryImpl): SchedulingRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindFriendRepository(impl: FriendRepositoryImpl): FriendRepository

    @Binds
    @Singleton
    abstract fun bindScheduler(impl: Scheduler): RandomiseScheduler
}