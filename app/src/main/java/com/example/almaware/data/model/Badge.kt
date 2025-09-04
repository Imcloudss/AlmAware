package com.example.almaware.data.model

// Data class per i badge basata sulla struttura JSON
data class Badge(
    val badgeName: String = "",
    val sdgRef: Int = 0,
    val points: Int = 0,
    val subtitle: String = "",
    val description: String = "",
    val validate: String = "",
    val unvalidate: String = "",
    val bubble: String = "",
    val icon: String = "",
    val type: String = "",
    val howCheck: String = "",
    val done: String = "",
    val notDone: String = "",

    // Campi specifici per MultiCheckbox/Timer
    val targetCount: Int? = null,
    val maxDurationMinutes: Int? = null,

    // Campi specifici per DailyTracker
    val dailyTarget: Double? = null,
    val durationDays: Int? = null,

    // Campi specifici per WeeklyCheckbox
    val timesPerWeek: Int? = null,
    val durationWeeks: Int? = null,

    // Campi specifici per Quiz
    val passingScore: Int? = null,
    val questions: List<QuizQuestion> = emptyList()
)

// Data class per le domande dei quiz
data class QuizQuestion(
    val question: String = "",
    val answer1: String = "",
    val answer2: String = "",
    val answer3: String = "",
    val answer4: String = "",
    val correctAnswer: Int = 1 // 1-4, indica quale risposta è corretta
)