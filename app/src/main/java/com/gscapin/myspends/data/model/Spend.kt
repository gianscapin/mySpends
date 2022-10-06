package com.gscapin.myspends.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "spends_tbl")
data class Spend(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val type: String,
    val amount: Double,
    val description: String
)
