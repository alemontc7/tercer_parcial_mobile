package com.ucb.framework.mappers

import com.ucb.domain.Feature
import com.ucb.domain.Plan
import com.ucb.framework.dto.FeatureDto
import com.ucb.framework.dto.PlanDto

fun PlanDto.toDomain(): Plan = Plan(
    name        = name,
    priceBefore = priceBefore,
    priceNow    = priceNow,
    bandwidth   = bandwidth,
    features    = features.map { it.toDomain() }
)
fun FeatureDto.toDomain(): Feature = Feature(
    description = description
)