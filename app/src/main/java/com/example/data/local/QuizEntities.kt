package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.Achievement
import com.example.model.Mission
import com.example.model.QuizCategory
import com.example.model.QuizQuestion

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey val id: String,
    val categoryId: String,
    val questionBn: String,
    val questionEn: String,
    val optionsBnJson: String, // stored as pipe-separated or delimited
    val optionsEnJson: String,
    val correctOptionIndex: Int,
    val explanationBn: String,
    val explanationEn: String,
    val difficulty: String,
    val points: Int,
    val timeLimitSeconds: Int,
    val isApproved: Boolean,
    val isAiGenerated: Boolean
) {
    fun toDomain(): QuizQuestion {
        return QuizQuestion(
            id = id,
            categoryId = categoryId,
            questionBn = questionBn,
            questionEn = questionEn,
            optionsBn = if (optionsBnJson.isEmpty()) emptyList() else optionsBnJson.split("|||"),
            optionsEn = if (optionsEnJson.isEmpty()) emptyList() else optionsEnJson.split("|||"),
            correctOptionIndex = correctOptionIndex,
            explanationBn = explanationBn,
            explanationEn = explanationEn,
            difficulty = difficulty,
            points = points,
            timeLimitSeconds = timeLimitSeconds,
            isApproved = isApproved,
            isAiGenerated = isAiGenerated
        )
    }

    companion object {
        fun fromDomain(domain: QuizQuestion): QuestionEntity {
            return QuestionEntity(
                id = domain.id,
                categoryId = domain.categoryId,
                questionBn = domain.questionBn,
                questionEn = domain.questionEn,
                optionsBnJson = domain.optionsBn.joinToString("|||"),
                optionsEnJson = domain.optionsEn.joinToString("|||"),
                correctOptionIndex = domain.correctOptionIndex,
                explanationBn = domain.explanationBn,
                explanationEn = domain.explanationEn,
                difficulty = domain.difficulty,
                points = domain.points,
                timeLimitSeconds = domain.timeLimitSeconds,
                isApproved = domain.isApproved,
                isAiGenerated = domain.isAiGenerated
            )
        }
    }
}

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: String,
    val nameBn: String,
    val nameEn: String,
    val descriptionBn: String,
    val iconName: String,
    val primaryColorHex: String,
    val secondaryColorHex: String,
    val questionCount: Int,
    val displayOrder: Int,
    val isFeatured: Boolean,
    val isActive: Boolean
) {
    fun toDomain(): QuizCategory = QuizCategory(
        id = id,
        nameBn = nameBn,
        nameEn = nameEn,
        descriptionBn = descriptionBn,
        iconName = iconName,
        primaryColorHex = primaryColorHex,
        secondaryColorHex = secondaryColorHex,
        questionCount = questionCount,
        displayOrder = displayOrder,
        isFeatured = isFeatured,
        isActive = isActive
    )

    companion object {
        fun fromDomain(cat: QuizCategory): CategoryEntity = CategoryEntity(
            id = cat.id,
            nameBn = cat.nameBn,
            nameEn = cat.nameEn,
            descriptionBn = cat.descriptionBn,
            iconName = cat.iconName,
            primaryColorHex = cat.primaryColorHex,
            secondaryColorHex = cat.secondaryColorHex,
            questionCount = cat.questionCount,
            displayOrder = cat.displayOrder,
            isFeatured = cat.isFeatured,
            isActive = cat.isActive
        )
    }
}

@Entity(tableName = "missions")
data class MissionEntity(
    @PrimaryKey val id: String,
    val titleBn: String,
    val descriptionBn: String,
    val type: String,
    val targetCount: Int,
    val currentProgress: Int,
    val rewardCoins: Int,
    val rewardXp: Int,
    val isClaimed: Boolean,
    val isCompleted: Boolean
) {
    fun toDomain(): Mission = Mission(
        id = id,
        titleBn = titleBn,
        descriptionBn = descriptionBn,
        type = type,
        targetCount = targetCount,
        currentProgress = currentProgress,
        rewardCoins = rewardCoins,
        rewardXp = rewardXp,
        isClaimed = isClaimed,
        isCompleted = isCompleted
    )

    companion object {
        fun fromDomain(m: Mission): MissionEntity = MissionEntity(
            id = m.id,
            titleBn = m.titleBn,
            descriptionBn = m.descriptionBn,
            type = m.type,
            targetCount = m.targetCount,
            currentProgress = m.currentProgress,
            rewardCoins = m.rewardCoins,
            rewardXp = m.rewardXp,
            isClaimed = m.isClaimed,
            isCompleted = m.isCompleted
        )
    }
}

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey val id: String,
    val titleBn: String,
    val descriptionBn: String,
    val iconName: String,
    val requiredCount: Int,
    val currentCount: Int,
    val isUnlocked: Boolean,
    val rewardCoins: Int,
    val unlockedDate: String
) {
    fun toDomain(): Achievement = Achievement(
        id = id,
        titleBn = titleBn,
        descriptionBn = descriptionBn,
        iconName = iconName,
        requiredCount = requiredCount,
        currentCount = currentCount,
        isUnlocked = isUnlocked,
        rewardCoins = rewardCoins,
        unlockedDate = unlockedDate
    )

    companion object {
        fun fromDomain(a: Achievement): AchievementEntity = AchievementEntity(
            id = a.id,
            titleBn = a.titleBn,
            descriptionBn = a.descriptionBn,
            iconName = a.iconName,
            requiredCount = a.requiredCount,
            currentCount = a.currentCount,
            isUnlocked = a.isUnlocked,
            rewardCoins = a.rewardCoins,
            unlockedDate = a.unlockedDate
        )
    }
}
