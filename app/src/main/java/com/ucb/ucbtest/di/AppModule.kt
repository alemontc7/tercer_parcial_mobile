package com.ucb.ucbtest.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.ucb.data.PlanRepository
import com.ucb.data.plan.IPlanRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.ucb.framework.plan.PlanRemoteDataSource
import com.ucb.usecases.GetPlans

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePlanRemoteDataSource(
        @ApplicationContext context: Context
    ): IPlanRemoteDataSource = PlanRemoteDataSource(context)

    @Provides
    @Singleton
    fun providePlanRepository(
        remoteDataSource: IPlanRemoteDataSource
    ): PlanRepository = PlanRepository(remoteDataSource)

    @Provides
    @Singleton
    fun provideGetPlansUseCase(
        planRepository: PlanRepository
    ): GetPlans = GetPlans(planRepository)

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
}