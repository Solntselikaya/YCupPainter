package com.velikanova.ycuppainter.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.velikanova.ycuppainter.data.model.dto.ProjectShortDto
import com.velikanova.ycuppainter.data.model.entity.ProjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT id, name, updated_ts FROM project")
    fun selectAllAsFlow(): Flow<List<ProjectShortDto>>

    @Upsert
    suspend fun upsert(project: ProjectEntity)
}