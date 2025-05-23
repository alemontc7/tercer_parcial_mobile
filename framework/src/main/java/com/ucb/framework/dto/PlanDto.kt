package com.ucb.framework.dto


data class PlanDto(
    val name: String,
    val priceBefore: String,
    val priceNow: String,
    val bandwidth: String,
    val features: List<FeatureDto>
)