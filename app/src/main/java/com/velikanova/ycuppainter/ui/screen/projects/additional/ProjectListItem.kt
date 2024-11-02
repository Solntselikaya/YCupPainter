package com.velikanova.ycuppainter.ui.screen.projects.additional

import java.time.ZonedDateTime

data class ProjectListItem(
    val id: Int = 0,
    val name: String = "",
//    val lastFrame: TODO
    val createdTs: ZonedDateTime = ZonedDateTime.now()
)
