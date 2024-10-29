package com.velikanova.ycuppainter.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "frames",
    foreignKeys = [
        ForeignKey(
            entity = Project::class,
            parentColumns = ["id"],
            childColumns = ["project_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        )
    ],
    indices = [
        Index(value = ["id"]),
        Index(value = ["project_id"])
    ]
)
data class Frame(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "project_id")
    val projectId: Int,

    @ColumnInfo(name = "ordinal")
    val ordinal: Int,

    // TODO в кач-ве дополнительной функциональности
//    @ColumnInfo(name = "is_visible")
//    val isVisible: Boolean = true
)
