package com.ucb.data.plan

import com.ucb.domain.Plan

interface IPlanRemoteDataSource {
    suspend fun fetchPlans(): List<Plan>
}