package com.example.intervalprotrainerapp.data

import com.example.intervalprotrainerapp.models.TrainingItem

/** Иммитация базы данных. [list] - список всех полей*/
object DataBase {
    val list = listOf<TrainingItem>(
        TrainingItem(0, "Жим", 60, 300, 6),
        TrainingItem(1, "Жим", 60, 300, 3, 1),
        TrainingItem(2, "Тяга", 50, 10, 3, 2),
        TrainingItem(3, "Кардио", 50, 10, 3, 3),
        TrainingItem(4, "Кардио", 50, 40, 3, 4),
        TrainingItem(5, "Кардио", 50, 40, 3, 5),
        TrainingItem(6, "Кардио", 50, 40, 3, 6),
        TrainingItem(7, "Кардио", 50, 40, 3, 7),
        )
}