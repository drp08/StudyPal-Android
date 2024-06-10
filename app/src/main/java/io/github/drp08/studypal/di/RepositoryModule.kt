package io.github.drp08.studypal.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.drp08.studypal.data.SchedulingRepositoryImpl
import io.github.drp08.studypal.domain.SchedulingRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSchedulingRepository(impl: SchedulingRepositoryImpl): SchedulingRepository
}