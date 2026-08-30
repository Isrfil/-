package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.QuizRepository
import com.example.model.Achievement
import com.example.model.AppConfig
import com.example.model.LiveAnnouncement
import com.example.model.Mission
import com.example.model.QuestionReviewItem
import com.example.model.QuizCategory
import com.example.model.QuizQuestion
import com.example.model.QuizResult
import com.example.model.Tournament
import com.example.model.UserProfile
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = QuizRepository(application.applicationContext, viewModelScope)

    // Repository Flows
    val userProfile: StateFlow<UserProfile> = repository.userProfile
    val appConfig: StateFlow<AppConfig> = repository.appConfig
    val announcements: StateFlow<List<LiveAnnouncement>> = repository.announcements
    val tournaments: StateFlow<List<Tournament>> = repository.tournaments
    val pendingAiQuestions: StateFlow<List<QuizQuestion>> = repository.pendingAiQuestions

    val categories: StateFlow<List<QuizCategory>> = repository.getCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val missions: StateFlow<List<Mission>> = repository.getMissions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val achievements: StateFlow<List<Achievement>> = repository.getAchievements()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Quiz Play State
    private val _isPlayingQuiz = MutableStateFlow(false)
    val isPlayingQuiz: StateFlow<Boolean> = _isPlayingQuiz.asStateFlow()

    private val _activeCategory = MutableStateFlow<QuizCategory?>(null)
    val activeCategory: StateFlow<QuizCategory?> = _activeCategory.asStateFlow()

    private val _currentQuestions = MutableStateFlow<List<QuizQuestion>>(emptyList())
    val currentQuestions: StateFlow<List<QuizQuestion>> = _currentQuestions.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _selectedOptionIndex = MutableStateFlow(-1)
    val selectedOptionIndex: StateFlow<Int> = _selectedOptionIndex.asStateFlow()

    private val _isAnswerRevealed = MutableStateFlow(false)
    val isAnswerRevealed: StateFlow<Boolean> = _isAnswerRevealed.asStateFlow()

    private val _remainingTimeSeconds = MutableStateFlow(15)
    val remainingTimeSeconds: StateFlow<Int> = _remainingTimeSeconds.asStateFlow()

    private val _currentScore = MutableStateFlow(0)
    val currentScore: StateFlow<Int> = _currentScore.asStateFlow()

    private val _correctCount = MutableStateFlow(0)
    val correctCount: StateFlow<Int> = _correctCount.asStateFlow()

    private val _wrongCount = MutableStateFlow(0)
    val wrongCount: StateFlow<Int> = _wrongCount.asStateFlow()

    private val _streakCount = MutableStateFlow(0)
    val streakCount: StateFlow<Int> = _streakCount.asStateFlow()

    private val _is5050Used = MutableStateFlow(false)
    val is5050Used: StateFlow<Boolean> = _is5050Used.asStateFlow()

    private val _isSkipUsed = MutableStateFlow(false)
    val isSkipUsed: StateFlow<Boolean> = _isSkipUsed.asStateFlow()

    private val _isHintUsed = MutableStateFlow(false)
    val isHintUsed: StateFlow<Boolean> = _isHintUsed.asStateFlow()

    private val _hiddenOptionIndices = MutableStateFlow<Set<Int>>(emptySet())
    val hiddenOptionIndices: StateFlow<Set<Int>> = _hiddenOptionIndices.asStateFlow()

    private val _reviewItems = mutableListOf<QuestionReviewItem>()

    private val _lastQuizResult = MutableStateFlow<QuizResult?>(null)
    val lastQuizResult: StateFlow<QuizResult?> = _lastQuizResult.asStateFlow()

    // Dialog & UI State
    private val _showDailyRewardDialog = MutableStateFlow(false)
    val showDailyRewardDialog: StateFlow<Boolean> = _showDailyRewardDialog.asStateFlow()

    private val _showAdminLoginDialog = MutableStateFlow(false)
    val showAdminLoginDialog: StateFlow<Boolean> = _showAdminLoginDialog.asStateFlow()

    private val _isAdminMode = MutableStateFlow(false)
    val isAdminMode: StateFlow<Boolean> = _isAdminMode.asStateFlow()

    private val _selectedAnnouncement = MutableStateFlow<LiveAnnouncement?>(null)
    val selectedAnnouncement: StateFlow<LiveAnnouncement?> = _selectedAnnouncement.asStateFlow()

    private var timerJob: Job? = null
    private var quizStartTimeEpoch: Long = 0L

    fun startQuiz(category: QuizCategory, questionLimit: Int = 5) {
        viewModelScope.launch {
            val questions = repository.getQuestionsForQuiz(category.id, questionLimit)
            if (questions.isNotEmpty()) {
                _activeCategory.value = category
                _currentQuestions.value = questions
                _currentQuestionIndex.value = 0
                _selectedOptionIndex.value = -1
                _isAnswerRevealed.value = false
                _currentScore.value = 0
                _correctCount.value = 0
                _wrongCount.value = 0
                _streakCount.value = 0
                _is5050Used.value = false
                _isSkipUsed.value = false
                _isHintUsed.value = false
                _hiddenOptionIndices.value = emptySet()
                _reviewItems.clear()
                _isPlayingQuiz.value = true
                quizStartTimeEpoch = System.currentTimeMillis()

                startQuestionTimer(questions[0].timeLimitSeconds)
            }
        }
    }

    private fun startQuestionTimer(duration: Int) {
        timerJob?.cancel()
        _remainingTimeSeconds.value = duration
        timerJob = viewModelScope.launch {
            while (_remainingTimeSeconds.value > 0 && !_isAnswerRevealed.value) {
                delay(1000)
                _remainingTimeSeconds.value -= 1
            }
            if (_remainingTimeSeconds.value <= 0 && !_isAnswerRevealed.value) {
                // Time up! Auto reveal as wrong/skipped
                handleTimeUp()
            }
        }
    }

    private fun handleTimeUp() {
        _isAnswerRevealed.value = true
        _wrongCount.value += 1
        _streakCount.value = 0

        val currentQ = _currentQuestions.value.getOrNull(_currentQuestionIndex.value)
        if (currentQ != null) {
            _reviewItems.add(
                QuestionReviewItem(
                    questionBn = currentQ.questionBn,
                    optionsBn = currentQ.optionsBn,
                    selectedOptionIndex = -1,
                    correctOptionIndex = currentQ.correctOptionIndex,
                    explanationBn = currentQ.explanationBn
                )
            )
        }
    }

    fun selectOption(optionIndex: Int) {
        if (_isAnswerRevealed.value) return

        timerJob?.cancel()
        _selectedOptionIndex.value = optionIndex
        _isAnswerRevealed.value = true

        val currentQ = _currentQuestions.value.getOrNull(_currentQuestionIndex.value) ?: return
        val isCorrect = optionIndex == currentQ.correctOptionIndex

        if (isCorrect) {
            val speedBonus = _remainingTimeSeconds.value * 2
            val pointsEarned = currentQ.points + speedBonus
            _currentScore.value += pointsEarned
            _correctCount.value += 1
            _streakCount.value += 1
        } else {
            _wrongCount.value += 1
            _streakCount.value = 0
        }

        _reviewItems.add(
            QuestionReviewItem(
                questionBn = currentQ.questionBn,
                optionsBn = currentQ.optionsBn,
                selectedOptionIndex = optionIndex,
                correctOptionIndex = currentQ.correctOptionIndex,
                explanationBn = currentQ.explanationBn
            )
        )
    }

    fun nextQuestion() {
        val nextIdx = _currentQuestionIndex.value + 1
        val questions = _currentQuestions.value

        if (nextIdx < questions.size) {
            _currentQuestionIndex.value = nextIdx
            _selectedOptionIndex.value = -1
            _isAnswerRevealed.value = false
            _hiddenOptionIndices.value = emptySet()
            startQuestionTimer(questions[nextIdx].timeLimitSeconds)
        } else {
            finishQuiz()
        }
    }

    private fun finishQuiz() {
        timerJob?.cancel()
        val totalTime = ((System.currentTimeMillis() - quizStartTimeEpoch) / 1000).toInt()
        val totalQ = _currentQuestions.value.size
        val correct = _correctCount.value
        val config = appConfig.value

        val earnedXp = correct * config.correctXpMultiplier + (_currentScore.value / 2)
        val earnedCoins = correct * config.correctCoinReward

        val accuracy = if (totalQ > 0) ((correct.toFloat() / totalQ) * 100).toInt() else 0

        val result = QuizResult(
            categoryNameBn = _activeCategory.value?.nameBn ?: "কুইজ",
            totalQuestions = totalQ,
            correctAnswers = correct,
            wrongAnswers = _wrongCount.value,
            skippedAnswers = totalQ - correct - _wrongCount.value,
            score = _currentScore.value,
            earnedCoins = earnedCoins,
            earnedXp = earnedXp,
            timeTakenSeconds = totalTime,
            accuracyPercentage = accuracy,
            questionsAnswered = _reviewItems.toList()
        )

        _lastQuizResult.value = result
        _isPlayingQuiz.value = false

        viewModelScope.launch {
            repository.recordQuizFinished(result)
        }
    }

    fun exitQuiz() {
        timerJob?.cancel()
        _isPlayingQuiz.value = false
        _lastQuizResult.value = null
    }

    // Lifelines
    fun use5050Lifeline() {
        if (_is5050Used.value || _isAnswerRevealed.value) return
        val currentQ = _currentQuestions.value.getOrNull(_currentQuestionIndex.value) ?: return

        val wrongIndices = (0 until currentQ.optionsBn.size).filter { it != currentQ.correctOptionIndex }
        val toHide = wrongIndices.shuffled().take(2).toSet()

        _hiddenOptionIndices.value = toHide
        _is5050Used.value = true
    }

    fun useSkipLifeline() {
        if (_isSkipUsed.value || _isAnswerRevealed.value) return
        _isSkipUsed.value = true
        nextQuestion()
    }

    fun useHintLifeline() {
        if (_isHintUsed.value) return
        _isHintUsed.value = true
    }

    // User Profile Actions
    fun claimDailyReward(): Boolean {
        return repository.claimDailyReward()
    }

    fun applyReferralCode(code: String): Boolean {
        return repository.applyReferralCode(code)
    }

    fun claimMission(missionId: String) {
        viewModelScope.launch {
            repository.claimMissionReward(missionId)
        }
    }

    fun joinTournament(tournament: Tournament, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.joinTournament(tournament)
            onResult(success)
        }
    }

    // Settings Toggles
    fun toggleSound() = repository.toggleSound()
    fun toggleVibration() = repository.toggleVibration()
    fun toggleLanguage() = repository.toggleLanguage()
    fun toggleDarkMode() = repository.toggleDarkMode()

    // Admin Panel Controls
    fun setAdminMode(enabled: Boolean) {
        _isAdminMode.value = enabled
    }

    fun generateAiQuestionForApproval(categoryId: String, categoryNameBn: String) {
        repository.generateAiQuestionForApproval(categoryId, categoryNameBn)
    }

    fun approveAiQuestion(question: QuizQuestion) {
        viewModelScope.launch {
            repository.approveAiQuestion(question)
        }
    }

    fun rejectAiQuestion(questionId: String) {
        repository.rejectAiQuestion(questionId)
    }

    fun updateAppConfig(config: AppConfig) {
        viewModelScope.launch {
            repository.updateAppConfig(config)
        }
    }

    fun publishAnnouncement(announcement: LiveAnnouncement) {
        viewModelScope.launch {
            repository.publishAnnouncement(announcement)
        }
    }

    fun createTournament(tournament: Tournament) {
        viewModelScope.launch {
            repository.createTournament(tournament)
        }
    }

    fun addNewQuestion(question: QuizQuestion) {
        viewModelScope.launch {
            repository.addNewQuestion(question)
        }
    }

    fun deleteQuestion(questionId: String) {
        viewModelScope.launch {
            repository.deleteQuestion(questionId)
        }
    }

    fun openAnnouncement(announcement: LiveAnnouncement?) {
        _selectedAnnouncement.value = announcement
    }
}
