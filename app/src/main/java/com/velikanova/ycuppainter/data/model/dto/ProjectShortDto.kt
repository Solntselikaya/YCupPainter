package com.velikanova.ycuppainter.data.model.dto

import java.time.ZonedDateTime

data class ProjectShortDto(
    val id: Int,
    val name: String,
    val updatedTs: ZonedDateTime
)
