package com.gscapin.myspends.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.UUID

@Entity(tableName = "earns_tbl")
data class Earn(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val type: String,
    val amount: Double,
//    @ColumnInfo(name = "date", defaultValue = "(strftime('%s','now','localtime'))")
//    val date: Long? = null,
    val created: Date = Date.from(Instant.now())
)
