package com.example.data.repository

import android.content.Context
import com.example.data.local.AchievementEntity
import com.example.data.local.CategoryEntity
import com.example.data.local.MissionEntity
import com.example.data.local.QuestionEntity
import com.example.data.local.QuizDatabase
import com.example.data.remote.FirestoreManager
import com.example.data.remote.GeminiAiService
import com.example.data.remote.InitialData
import com.example.model.Achievement
import com.example.model.AppConfig
import com.example.model.LiveAnnouncement
import com.example.model.Mission
import com.example.model.QuizCategory
import com.example.model.QuizQuestion
import com.example.model.QuizResult
import com.example.model.Tournament
import com.example.model.UserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class QuizRepository(
    private val context: Context,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {
    private val db = QuizDatabase.getDatabase(context)
    private val dao = db.quizDao()
    private val firestoreManager = FirestoreManager()
    val geminiAiService = GeminiAiService()

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _appConfig = MutableStateFlow(InitialData.defaultAppConfig)
    val appConfig: StateFlow<AppConfig> = _appConfig.asStateFlow()

    private val _announcements = MutableStateFlow<List<LiveAnnouncement>>(InitialData.defaultAnnouncements)
    val announcements: StateFlow<List<LiveAnnouncement>> = _announcements.asStateFlow()

    private val _tournaments = MutableStateFlow<List<Tournament>>(InitialData.defaultTournaments)
    val tournaments: StateFlow<List<Tournament>> = _tournaments.asStateFlow()

    private val _pendingAiQuestions = MutableStateFlow<List<QuizQuestion>>(emptyList())
    val pendingAiQuestions: StateFlow<List<QuizQuestion>> = _pendingAiQuestions.asStateFlow()

    init {
        scope.launch {
            seedDatabaseIfEmpty()
            listenToRemoteConfig()
            listenToRemoteAnnouncements()
            listenToRemoteTournaments()
        }
    }

    private suspend fun seedDatabaseIfEmpty() {
        val count = dao.getQuestionsCount()
        if (count == 0) {
            dao.insertCategories(InitialData.defaultCategories.map { CategoryEntity.fromDomain(it) })
            dao.insertQuestions(InitialData.defaultQuestions.map { QuestionEntity.fromDomain(it) })
            dao.insertMissions(InitialData.defaultMissions.map { MissionEntity.fromDomain(it) })
            dao.insertAchievements(InitialData.defaultAchievements.map { AchievementEntity.fromDomain(it) })
        }
    }

    private fun listenToRemoteConfig() {
        scope.launch {
            firestoreManager.observeAppConfig().collect { config ->
                _appConfig.value = config
            }
        }
    }

    private fun listenToRemoteAnnouncements() {
        scope.launch {
            firestoreManager.observeAnnouncements().collect { list ->
                _announcements.value = list
            }
        }
    }

    private fun listenToRemoteTournaments() {
        scope.launch {
            firestoreManager.observeTournaments().collect { list ->
                _tournaments.value = list
            }
        }
    }

    // Categories
    fun getCategories(): Flow<List<QuizCategory>> {
        return dao.getActiveCategories().map { list ->
            list.map { it.toDomain() }
        }
    }

    // Questions for a quiz session
    suspend fun getQuestionsForQuiz(categoryId: String, limit: Int = 5): List<QuizQuestion> {
        val entities = if (categoryId == "all" || categoryId.isEmpty()) {
            dao.getRandomQuestions(limit)
        } else {
            val list = dao.getRandomQuestionsByCategory(categoryId, limit)
            if (list.isEmpty()) dao.getRandomQuestions(limit) else list
        }
        return entities.map { it.toDomain() }
    }

    // Missions
    fun getMissions(): Flow<List<Mission>> {
        return dao.getAllMissions().map { list -> list.map { it.toDomain() } }
    }

    suspend fun claimMissionReward(missionId: String): Boolean {
        val missions = dao.getAllMissions().first()
        val mission = missions.find { it.id == missionId } ?: return false
        if (mission.isCompleted && !mission.isClaimed) {
            dao.claimMission(missionId)
            addCoinsAndXp(mission.rewardCoins, mission.rewardXp)
            return true
        }
        return false
    }

    // Achievements
    fun getAchievements(): Flow<List<Achievement>> {
        return dao.getAllAchievements().map { list -> list.map { it.toDomain() } }
    }

    // User Profile Actions
    fun addCoinsAndXp(coinsToAdd: Int, xpToAdd: Int) {
        val current = _userProfile.value
        val newXp = current.xp + xpToAdd
        val newLevel = (newXp / 200) + 1
        _userProfile.value = current.copy(
            coins = current.coins + coinsToAdd,
            xp = newXp,
            level = newLevel
        )
    }

    fun claimDailyReward(): Boolean {
        val current = _userProfile.value
        val now = System.currentTimeMillis()
        val oneDayMillis = 86400000L
        if (now - current.lastRewardClaimEpoch > oneDayMillis) {
            val config = _appConfig.value
            val newStreak = current.currentStreakDays + 1
            _userProfile.value = current.copy(
                coins = current.coins + config.dailyLoginRewardCoins,
                xp = current.xp + config.dailyLoginRewardXp,
                currentStreakDays = newStreak,
                bestStreakDays = maxOf(current.bestStreakDays, newStreak),
                lastRewardClaimEpoch = now
            )
            return true
        }
        return false
    }

    fun applyReferralCode(code: String): Boolean {
        val current = _userProfile.value
        if (current.referredBy.isEmpty() && code.isNotBlank() && code != current.referralCode) {
            val config = _appConfig.value
            _userProfile.value = current.copy(
                referredBy = code,
                coins = current.coins + config.refereeRewardCoins
            )
            return true
        }
        return false
    }

    suspend fun recordQuizFinished(result: QuizResult) {
        val current = _userProfile.value
        val isWin = result.accuracyPercentage >= 60
        val newGames = current.totalGamesPlayed + 1
        val newWins = if (isWin) current.totalWins + 1 else current.totalWins
        val newCorrect = current.correctAnswersCount + result.correctAnswers
        val newCoins = current.coins + result.earnedCoins
        val newXp = current.xp + result.earnedXp
        val newLevel = (newXp / 200) + 1

        _userProfile.value = current.copy(
            totalGamesPlayed = newGames,
            totalWins = newWins,
            correctAnswersCount = newCorrect,
            coins = newCoins,
            xp = newXp,
            level = newLevel
        )

        // Update progress for missions
        dao.updateMissionProgress("m_1", minOf(3, newGames), newGames >= 3)
        if (result.accuracyPercentage >= 80) {
            dao.updateMissionProgress("m_2", 1, true)
        }

        // Check achievements
        if (newCorrect >= 50) {
            val today = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
            dao.unlockAchievement("ach_quiz_50", today)
        }
    }

    // Toggle Settings
    fun toggleSound() {
        val current = _userProfile.value
        _userProfile.value = current.copy(isSoundEnabled = !current.isSoundEnabled)
    }

    fun toggleVibration() {
        val current = _userProfile.value
        _userProfile.value = current.copy(isVibrationEnabled = !current.isVibrationEnabled)
    }

    fun toggleLanguage() {
        val current = _userProfile.value
        _userProfile.value = current.copy(isBengaliLanguage = !current.isBengaliLanguage)
    }

    fun toggleDarkMode() {
        val current = _userProfile.value
        _userProfile.value = current.copy(isDarkMode = !current.isDarkMode)
    }

    // Admin & Dynamic System Methods
    suspend fun addNewQuestion(question: QuizQuestion) {
        dao.insertQuestion(QuestionEntity.fromDomain(question))
        firestoreManager.saveQuestion(question)
    }

    suspend fun deleteQuestion(questionId: String) {
        dao.deleteQuestion(questionId)
    }

    fun generateAiQuestionForApproval(categoryId: String, categoryNameBn: String) {
        val question = geminiAiService.generateAiQuestion(categoryNameBn, categoryId)
        val currentList = _pendingAiQuestions.value.toMutableList()
        currentList.add(0, question)
        _pendingAiQuestions.value = currentList
    }

    suspend fun approveAiQuestion(question: QuizQuestion) {
        val approved = question.copy(isApproved = true)
        dao.insertQuestion(QuestionEntity.fromDomain(approved))
        firestoreManager.saveQuestion(approved)
        _pendingAiQuestions.value = _pendingAiQuestions.value.filter { it.id != question.id }
    }

    fun rejectAiQuestion(questionId: String) {
        _pendingAiQuestions.value = _pendingAiQuestions.value.filter { it.id != questionId }
    }

    suspend fun updateAppConfig(config: AppConfig) {
        _appConfig.value = config
        firestoreManager.saveAppConfig(config)
    }

    suspend fun publishAnnouncement(announcement: LiveAnnouncement) {
        val current = _announcements.value.toMutableList()
        current.add(0, announcement)
        _announcements.value = current
        firestoreManager.publishAnnouncement(announcement)
    }

    suspend fun createTournament(tournament: Tournament) {
        val current = _tournaments.value.toMutableList()
        current.add(0, tournament)
        _tournaments.value = current
        firestoreManager.saveTournament(tournament)
    }

    suspend fun joinTournament(tournament: Tournament): Boolean {
        val current = _userProfile.value
        if (current.coins >= tournament.entryFeeCoins) {
            _userProfile.value = current.copy(coins = current.coins - tournament.entryFeeCoins)
            return true
        }
        return false
    }
}
