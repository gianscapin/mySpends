package com.gscapin.myspends.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "earns_tbl")
data class Earn(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val type: String,
    val amount: Double)
