package io.github.drp08.studypal.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.drp08.studypal.data.SchedulingRepositoryImpl
import io.github.drp08.studypal.data.UserRepositoryImpl
import io.github.drp08.studypal.domain.SchedulingRepository
import io.github.drp08.studypal.domain.UserRepository
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
}