package com.gscapin.myspends.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date
import java.util.UUID

@Entity(tableName = "spends_tbl")
data class Spend(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val type: String,
    val amount: Double,
    val description: String,
    val month: Int = Calendar.getInstance().get(Calendar.MONTH) + 1
)
