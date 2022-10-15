package com.gscapin.myspends.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar
import java.util.UUID

@Entity(tableName = "earns_tbl")
data class Earn(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val type: String,
    val amount: Double,
    val month: Int = Calendar.getInstance().get(Calendar.MONTH) + 1
//    @ColumnInfo(name = "date", defaultValue = "(strftime('%s','now','localtime'))")
//    val date: Long? = null,
    //val created: Date = Date.from(Instant.now())
)
