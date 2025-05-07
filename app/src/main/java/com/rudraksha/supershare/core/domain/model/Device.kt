package com.rudraksha.supershare.core.domain.model

import java.util.UUID

data class Device(
    val name: String,
    val ipAddress: String,
    val id: String = UUID.randomUUID().toString(),
    val isConnected: Boolean = false
)
