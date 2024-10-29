package com.velikanova.ycuppainter.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "drawings",
    foreignKeys = [
        ForeignKey(
            entity = Frame::class,
            parentColumns = ["id"],
            childColumns = ["frame_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        )
    ],
    indices = [
        Index(value = ["id"]),
        Index(value = ["frame_id"])
    ]
)
data class Drawing(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "frame_id")
    val frameId: Int,

    @ColumnInfo(name = "color_int")
    val colorInt: Int,

    @ColumnInfo(name = "stroke_width")
    val strokeWidth: Float,

    @ColumnInfo(name = "is_erasing")
    val isErasing: Boolean
)