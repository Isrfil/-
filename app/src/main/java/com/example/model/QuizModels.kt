package com.example.model

data class QuizQuestion(
    val id: String = "",
    val categoryId: String = "",
    val questionBn: String = "",
    val questionEn: String = "",
    val optionsBn: List<String> = emptyList(),
    val optionsEn: List<String> = emptyList(),
    val correctOptionIndex: Int = 0,
    val explanationBn: String = "",
    val explanationEn: String = "",
    val difficulty: String = "medium", // easy, medium, hard
    val points: Int = 10,
    val timeLimitSeconds: Int = 20,
    val isApproved: Boolean = true,
    val isAiGenerated: Boolean = false,
    val tags: List<String> = emptyList()
)

data class QuizCategory(
    val id: String = "",
    val nameBn: String = "",
    val nameEn: String = "",
    val descriptionBn: String = "",
    val iconName: String = "quiz",
    val primaryColorHex: String = "#6366F1",
    val secondaryColorHex: String = "#8B5CF6",
    val questionCount: Int = 0,
    val displayOrder: Int = 0,
    val isFeatured: Boolean = false,
    val isActive: Boolean = true
)

data class AppConfig(
    val minSupportedVersionCode: Int = 1,
    val latestVersionCode: Int = 1,
    val latestVersionName: String = "1.0",
    val forceUpdateUrl: String = "https://play.google.com/store/apps/details?id=com.companyQuizapp",
    val updateNotesBn: String = "নতুন টুর্নামেন্ট ফিচার, দ্রুতগতির পারফরম্যান্স এবং বাগ ফিক্স করা হয়েছে!",
    val isMaintenanceMode: Boolean = false,
    val maintenanceMessageBn: String = "আমাদের সার্ভার রক্ষণাবেক্ষণ চলছে। অনুগ্রহ করে কিছুক্ষণ পর আবার চেষ্টা করুন।",
    val correctXpMultiplier: Int = 10,
    val correctCoinReward: Int = 5,
    val dailyLoginRewardCoins: Int = 50,
    val dailyLoginRewardXp: Int = 100,
    val referralRewardCoins: Int = 100,
    val refereeRewardCoins: Int = 50,
    val adFrequencyMinutes: Int = 5,
    val isBannerAdEnabled: Boolean = true,
    val isInterstitialAdEnabled: Boolean = true,
    val announcementBannerBn: String = "🔥 মেগা কুইজ টুর্নামেন্ট শুরু হয়েছে! এখনই অংশগ্রহণ করুন এবং ১০০০ কয়েন জিতুন!",
    val announcementBannerActive: Boolean = true
)

data class LiveAnnouncement(
    val id: String = "",
    val titleBn: String = "",
    val messageBn: String = "",
    val type: String = "event", // event, update, reward, tournament, maintenance, info
    val timestamp: Long = System.currentTimeMillis(),
    val actionUrl: String = "",
    val actionLabelBn: String = "দেখুন",
    val isPinned: Boolean = false,
    val isActive: Boolean = true
)

data class Tournament(
    val id: String = "",
    val titleBn: String = "",
    val descriptionBn: String = "",
    val categoryId: String = "general",
    val entryFeeCoins: Int = 20,
    val prizePoolCoins: Int = 5000,
    val totalParticipants: Int = 142,
    val startTimeEpoch: Long = System.currentTimeMillis() - 3600000,
    val endTimeEpoch: Long = System.currentTimeMillis() + 86400000 * 2,
    val status: String = "active", // upcoming, active, completed
    val questionsCount: Int = 10,
    val rulesBn: String = "প্রতি সঠিক উত্তরে ২০ পয়েন্ট। নেগেটিভ মার্ক নেই। দ্রুত উত্তর দিলে অতিরিক্ত স্পিড বোনাস পয়েন্ট পাওয়া যাবে।"
)

data class Mission(
    val id: String = "",
    val titleBn: String = "",
    val descriptionBn: String = "",
    val type: String = "daily", // daily, weekly
    val targetCount: Int = 3,
    val currentProgress: Int = 0,
    val rewardCoins: Int = 50,
    val rewardXp: Int = 100,
    val isClaimed: Boolean = false,
    val isCompleted: Boolean = false
)

data class Achievement(
    val id: String = "",
    val titleBn: String = "",
    val descriptionBn: String = "",
    val iconName: String = "trophy",
    val requiredCount: Int = 10,
    val currentCount: Int = 0,
    val isUnlocked: Boolean = false,
    val rewardCoins: Int = 100,
    val unlockedDate: String = ""
)

data class UserProfile(
    val userId: String = "user_default",
    val displayName: String = "কুইজ মাস্টার",
    val email: String = "israfilmia731@gmail.com",
    val avatarUrl: String = "",
    val referralCode: String = "QUIZ789",
    val referredBy: String = "",
    val coins: Int = 250,
    val xp: Int = 450,
    val level: Int = 3,
    val totalGamesPlayed: Int = 12,
    val totalWins: Int = 8,
    val correctAnswersCount: Int = 48,
    val currentStreakDays: Int = 4,
    val bestStreakDays: Int = 7,
    val lastRewardClaimEpoch: Long = 0L,
    val isSoundEnabled: Boolean = true,
    val isVibrationEnabled: Boolean = true,
    val isBengaliLanguage: Boolean = true,
    val isDarkMode: Boolean = true
)

data class QuizResult(
    val categoryNameBn: String = "",
    val totalQuestions: Int = 0,
    val correctAnswers: Int = 0,
    val wrongAnswers: Int = 0,
    val skippedAnswers: Int = 0,
    val score: Int = 0,
    val earnedCoins: Int = 0,
    val earnedXp: Int = 0,
    val timeTakenSeconds: Int = 0,
    val accuracyPercentage: Int = 0,
    val questionsAnswered: List<QuestionReviewItem> = emptyList()
)

data class QuestionReviewItem(
    val questionBn: String,
    val optionsBn: List<String>,
    val selectedOptionIndex: Int,
    val correctOptionIndex: Int,
    val explanationBn: String
)

data class LeaderboardUser(
    val rank: Int,
    val name: String,
    val score: Int,
    val xp: Int,
    val coins: Int,
    val avatarInitial: String,
    val isCurrentUser: Boolean = false
)
