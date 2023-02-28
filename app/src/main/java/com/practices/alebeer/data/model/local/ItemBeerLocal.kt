package com.practices.alebeer.data.model.local

import com.practices.alebeer.core.base.BaseModel
import com.practices.alebeer.data.local.database.entity.SampleEntity

data class ItemBeerLocal(
    val id: Int,
    var localSampleId: Int? = null,
    val avatarUrl: String,
    var avatarPath: String,
    val name: String,
    val price: String,
    var note: String,
    val isFav: Boolean,
    var isFreezeNote: Boolean,
    var isSaving: Boolean,
    var saleOffTime: Long? = null
) : BaseModel() {
    fun convertToEntity() = SampleEntity(
        sampleId = localSampleId,
        name = name,
        remoteId = id,
        price = price,
        avaPath = avatarPath,
        note = note
    )
}
