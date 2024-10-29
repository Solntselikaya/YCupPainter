package com.velikanova.ycuppainter.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.velikanova.ycuppainter.data.dao.DrawingDao
import com.velikanova.ycuppainter.data.dao.FrameDao
import com.velikanova.ycuppainter.data.dao.ProjectDao
import com.velikanova.ycuppainter.data.model.Drawing
import com.velikanova.ycuppainter.data.model.Frame
import com.velikanova.ycuppainter.data.model.Project
import kotlinx.coroutines.CoroutineScope

@Database(
    version = 1,
    entities = [
        Project::class,
        Frame::class,
        Drawing::class
    ],
    exportSchema = true

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
