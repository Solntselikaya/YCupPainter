package com.velikanova.ycuppainter.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.velikanova.ycuppainter.data.dao.DrawingDao
import com.velikanova.ycuppainter.data.dao.FrameDao
import com.velikanova.ycuppainter.data.dao.ProjectDao
import com.velikanova.ycuppainter.data.model.entity.DrawingEntity
import com.velikanova.ycuppainter.data.model.entity.FrameEntity
import com.velikanova.ycuppainter.data.model.entity.ProjectEntity
import kotlinx.coroutines.CoroutineScope

@Database(
    version = 1,
    entities = [
        ProjectEntity::class,
        FrameEntity::class,
        DrawingEntity::class
    ],
    exportSchema = true
)
@TypeConverters(
    value = [
        DbConverters::class
    ]
)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun frameDao(): FrameDao
    abstract fun drawingDao(): DrawingDao

    companion object {
        private const val DB_FILE = "painter_db"

        fun databaseBuilder(appContext: Context, scope: CoroutineScope): RoomDatabase {
            return Room
                .databaseBuilder(appContext, RoomDatabase::class.java, DB_FILE)
                .build()
        }
    }
}
