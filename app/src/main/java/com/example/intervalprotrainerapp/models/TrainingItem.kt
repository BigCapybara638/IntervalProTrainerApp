package com.example.intervalprotrainerapp.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrainingItem(
    val id: Int = 0,
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