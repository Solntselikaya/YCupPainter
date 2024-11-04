package com.velikanova.ycuppainter.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity(tableName = "project")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "last_used_color")
    val lastUsedColorInt: Int?,

    @ColumnInfo(name = "last_used_stroke_width")
    val lastUsedStrokeWidth: Float?,

    // TODO в кач-ве дополнительной функциональности
//    @ColumnInfo(name = "play_speed")
//    val playSpeed: Double,

    @ColumnInfo(name = "updated_ts")
    val updatedTs: ZonedDateTime
)
