package com.example.intervalprotrainerapp.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrainingItem(
    val id: Long = 0,
    val name: String = "",
    val intervalWork: Int = 10,
    val intervalRelax: Int = 10,
    val cycles: Int = 2,
    val color: Int = 0,
) : Parcelable {
    override fun describeContents(): Int {
        return 0
    }
}