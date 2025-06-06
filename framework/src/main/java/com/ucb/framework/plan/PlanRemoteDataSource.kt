package com.ucb.framework.plan

import android.content.Context
import com.google.gson.Gson
import com.ucb.data.plan.IPlanRemoteDataSource
import com.ucb.domain.Plan
import com.ucb.framework.R
import com.ucb.framework.dto.PlanResponseDto
import com.ucb.framework.mappers.toDomain

class PlanRemoteDataSource (
    private val context: Context
) : IPlanRemoteDataSource {

    override suspend fun fetchPlans(): List<Plan> {
        val inputStream = context.resources.openRawResource(R.raw.plans_stub)
        val json = inputStream.bufferedReader().use { it.readText() }
        val response = Gson().fromJson(json, PlanResponseDto::class.java)
        return response.plans.map { it.toDomain() }
    }
}