package com.practices.alebeer.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.practices.alebeer.data.local.database.AppRoomTable
import com.practices.alebeer.data.model.local.ItemBeerLocal

@Entity(tableName = AppRoomTable.TABLE_SAMPLE)
data class SampleEntity(
    @field:PrimaryKey(autoGenerate = true) var sampleId: Int? = null,
    val remoteId: Int,
    val name: String,
    val price: String,
    val avaPath: String,
    val note: String
) {
    fun convertToBeerItem() = ItemBeerLocal(
        id = remoteId,
        localSampleId = sampleId,
        avatarPath = avaPath,
        avatarUrl = "",
        name = name,
        price = price,
        note = note,
        isFav = true,
        isFreezeNote = false,
        isSaving = false
    )
}
