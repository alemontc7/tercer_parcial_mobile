package com.ucb.data

import com.ucb.data.plan.IPlanRemoteDataSource
import com.ucb.domain.Plan

class PlanRepository(
    val remoteDataSource: IPlanRemoteDataSource
) {

    suspend fun getPlans(): List<Plan> {
        return this.remoteDataSource.fetchPlans()
    }
}