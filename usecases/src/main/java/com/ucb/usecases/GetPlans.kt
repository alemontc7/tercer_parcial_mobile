package com.ucb.usecases

import com.ucb.data.PlanRepository
import com.ucb.domain.Plan

class GetPlans (
    val planRepository: PlanRepository
){
    suspend fun invoke(): List<Plan> {
        return planRepository.getPlans()
    }
}