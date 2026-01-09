package com.example.intervalprotrainerapp.models

data class TrainingItem(
    val id: Int = 0,
    val name: String,
    val intervalWork: Int,
    val intervalRelax: Int,
    val cycles: Int,
    val color: Int = 0,
)