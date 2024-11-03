package com.velikanova.ycuppainter.data.model.dto

import androidx.room.ColumnInfo
import java.time.ZonedDateTime

data class ProjectShortDto(
    val id: Int,
    val name: String,

    @ColumnInfo(name = "updated_ts")
    val updatedTs: ZonedDateTime
)
