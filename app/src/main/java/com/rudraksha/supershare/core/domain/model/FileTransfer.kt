package com.rudraksha.supershare.core.domain.model

data class FileTransfer(
    val fileName: String,
    val totalSize: String,
    val transferredSize: String,
    val progress: Float, // between 0.0 and 1.0
    val inProgress: Boolean
)
