package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {

    // Questions
    @Query("SELECT * FROM questions WHERE isApproved = 1")
    fun getAllQuestions(): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE categoryId = :categoryId AND isApproved = 1")
    fun getQuestionsByCategory(categoryId: String): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE isApproved = 1 ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomQuestions(limit: Int): List<QuestionEntity>

    @Query("SELECT * FROM questions WHERE categoryId = :categoryId AND isApproved = 1 ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomQuestionsByCategory(categoryId: String, limit: Int): List<QuestionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuestionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: QuestionEntity)

    @Query("DELETE FROM questions WHERE id = :id")
    suspend fun deleteQuestion(id: String)

    @Query("SELECT COUNT(*) FROM questions")
    suspend fun getQuestionsCount(): Int

    // Categories
    @Query("SELECT * FROM categories WHERE isActive = 1 ORDER BY displayOrder ASC")
    fun getActiveCategories(): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    // Missions
    @Query("SELECT * FROM missions")
    fun getAllMissions(): Flow<List<MissionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMissions(missions: List<MissionEntity>)

    @Update
    suspend fun updateMission(mission: MissionEntity)

    @Query("UPDATE missions SET currentProgress = :progress, isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateMissionProgress(id: String, progress: Int, isCompleted: Boolean)

    @Query("UPDATE missions SET isClaimed = 1 WHERE id = :id")
    suspend fun claimMission(id: String)

    // Achievements
    @Query("SELECT * FROM achievements")
    fun getAllAchievements(): Flow<List<AchievementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievements(achievements: List<AchievementEntity>)

    @Update
    suspend fun updateAchievement(achievement: AchievementEntity)

    @Query("UPDATE achievements SET isUnlocked = 1, unlockedDate = :date WHERE id = :id")
    suspend fun unlockAchievement(id: String, date: String)
}
