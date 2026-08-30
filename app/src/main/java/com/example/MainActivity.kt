package com.example

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AnnouncementDetailDialog
import com.example.ui.components.AppHeaderProfileBar
import com.example.ui.components.ForceUpdateDialog
import com.example.ui.components.MaintenanceNoticeScreen
import com.example.ui.components.OptionalUpdateDialog
import com.example.ui.navigation.Screen
import com.example.ui.navigation.bottomNavScreens
import com.example.ui.screens.AdminPanelScreen
import com.example.ui.screens.AuthDialog
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.MissionsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.QuizPlayScreen
import com.example.ui.screens.QuizResultScreen
import com.example.ui.screens.TournamentsScreen
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.QuizViewModel
import com.example.util.ShareHelper
import com.example.util.SoundManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: QuizViewModel = viewModel()
            val userProfile by viewModel.userProfile.collectAsState()

            MyApplicationTheme(darkTheme = userProfile.isDarkMode) {
                MainAppContainer(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContainer(viewModel: QuizViewModel) {
    val context = LocalContext.current
    val userProfile by viewModel.userProfile.collectAsState()
    val appConfig by viewModel.appConfig.collectAsState()
    val announcements by viewModel.announcements.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val tournaments by viewModel.tournaments.collectAsState()
    val missions by viewModel.missions.collectAsState()
    val achievements by viewModel.achievements.collectAsState()
    val pendingAiQuestions by viewModel.pendingAiQuestions.collectAsState()

    val isPlayingQuiz by viewModel.isPlayingQuiz.collectAsState()
    val lastQuizResult by viewModel.lastQuizResult.collectAsState()
    val isAdminMode by viewModel.isAdminMode.collectAsState()
    val selectedAnnouncement by viewModel.selectedAnnouncement.collectAsState()

    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    var hasDismissedOptionalUpdate by remember { mutableStateOf(false) }
    var showAuthDialog by remember { mutableStateOf(false) }

    SoundManager.isSoundEnabled = userProfile.isSoundEnabled

    val currentAppVersionCode = 2

    // Sign Up & Login Dialog
    if (showAuthDialog) {
        AuthDialog(
            userProfile = userProfile,
            onDismiss = { showAuthDialog = false },
            onAuthSuccess = { name, email ->
                viewModel.updateUserAuth(name, email)
                showAuthDialog = false
            }
        )
    }

    // Maintenance Mode Screen
    if (appConfig.isMaintenanceMode && !isAdminMode) {
        MaintenanceNoticeScreen(
            message = appConfig.maintenanceMessageBn,
            onRetryClick = { /* Real-time flow will auto-refresh */ }
        )
        return
    }

    // Force Update Dialog
    if (appConfig.minSupportedVersionCode > currentAppVersionCode) {
        ForceUpdateDialog(
            appConfig = appConfig,
            onUpdateClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(ShareHelper.PLAY_STORE_URL))
                context.startActivity(intent)
            }
        )
    }

    // Optional Update Dialog
    if (appConfig.latestVersionCode > currentAppVersionCode && !hasDismissedOptionalUpdate && appConfig.minSupportedVersionCode <= currentAppVersionCode) {
        OptionalUpdateDialog(
            appConfig = appConfig,
            onUpdateClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(ShareHelper.PLAY_STORE_URL))
                context.startActivity(intent)
            },
            onDismiss = { hasDismissedOptionalUpdate = true }
        )
    }

    // Announcement Details Dialog
    selectedAnnouncement?.let { ann ->
        AnnouncementDetailDialog(
            announcement = ann,
            onDismiss = { viewModel.openAnnouncement(null) }
        )
    }

    // Admin Control Panel Screen
    if (isAdminMode) {
        AdminPanelScreen(
            appConfig = appConfig,
            categories = categories,
            pendingAiQuestions = pendingAiQuestions,
            onUpdateAppConfig = { viewModel.updateAppConfig(it) },
            onPublishAnnouncement = { viewModel.publishAnnouncement(it) },
            onGenerateAiQuestion = { catId, catName -> viewModel.generateAiQuestionForApproval(catId, catName) },
            onApproveAiQuestion = { viewModel.approveAiQuestion(it) },
            onRejectAiQuestion = { viewModel.rejectAiQuestion(it) },
            onAddNewQuestion = { viewModel.addNewQuestion(it) },
            onCreateTournament = { viewModel.createTournament(it) },
            onExitAdmin = { viewModel.setAdminMode(false) }
        )
        return
    }

    // Active Quiz Play Screen
    if (isPlayingQuiz) {
        val activeCategory by viewModel.activeCategory.collectAsState()
        val currentQuestions by viewModel.currentQuestions.collectAsState()
        val currentQIndex by viewModel.currentQuestionIndex.collectAsState()
        val remainingTime by viewModel.remainingTimeSeconds.collectAsState()
        val selectedOptionIdx by viewModel.selectedOptionIndex.collectAsState()
        val isRevealed by viewModel.isAnswerRevealed.collectAsState()
        val score by viewModel.currentScore.collectAsState()
        val streak by viewModel.streakCount.collectAsState()
        val is5050Used by viewModel.is5050Used.collectAsState()
        val isSkipUsed by viewModel.isSkipUsed.collectAsState()
        val isHintUsed by viewModel.isHintUsed.collectAsState()
        val hiddenIndices by viewModel.hiddenOptionIndices.collectAsState()

        QuizPlayScreen(
            category = activeCategory,
            questions = currentQuestions,
            currentQuestionIndex = currentQIndex,
            remainingTime = remainingTime,
            selectedOptionIndex = selectedOptionIdx,
            isAnswerRevealed = isRevealed,
            score = score,
            streak = streak,
            is5050Used = is5050Used,
            isSkipUsed = isSkipUsed,
            isHintUsed = isHintUsed,
            hiddenOptionIndices = hiddenIndices,
            onOptionSelected = { viewModel.selectOption(it) },
            onNextQuestion = { viewModel.nextQuestion() },
            onUse5050 = { viewModel.use5050Lifeline() },
            onUseSkip = { viewModel.useSkipLifeline() },
            onUseHint = { viewModel.useHintLifeline() },
            onExitQuiz = { viewModel.exitQuiz() }
        )
        return
    }

    // Quiz Result Screen
    lastQuizResult?.let { result ->
        val activeCat = viewModel.activeCategory.collectAsState().value
        QuizResultScreen(
            result = result,
            onPlayAgain = {
                activeCat?.let { cat ->
                    viewModel.startQuiz(cat)
                } ?: run {
                    categories.firstOrNull()?.let { cat -> viewModel.startQuiz(cat) }
                }
            },
            onHomeClick = {
                viewModel.exitQuiz()
                currentScreen = Screen.Home
            }
        )
        return
    }

    // Main App Shell with Header Bar and Bottom Navigation
    Scaffold(
        topBar = {
            AppHeaderProfileBar(
                userProfile = userProfile,
                onProfileClick = { currentScreen = Screen.Profile },
                onShareAppClick = { ShareHelper.shareApp(context) }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                bottomNavScreens.forEach { screen ->
                    val isSelected = currentScreen == screen
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentScreen = screen },
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.titleBn
                            )
                        },
                        label = {
                            Text(
                                text = screen.titleBn,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BrandPrimary,
                            selectedTextColor = BrandPrimary,
                            indicatorColor = BrandPrimary.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_item_${screen.route}")
                    )
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen_navigation"
            ) { targetScreen ->
                when (targetScreen) {
                    Screen.Home -> HomeScreen(
                        categories = categories,
                        announcements = announcements,
                        appConfig = appConfig,
                        userProfile = userProfile,
                        onCategoryClick = { category -> viewModel.startQuiz(category) },
                        onQuickPlayClick = {
                            categories.firstOrNull()?.let { cat -> viewModel.startQuiz(cat) }
                        },
                        onAnnouncementClick = { ann -> viewModel.openAnnouncement(ann) },
                        onClaimDailyReward = { viewModel.claimDailyReward() },
                        onNavigateToTournaments = { currentScreen = Screen.Tournaments },
                        onAdReward = { viewModel.earnAdRewardCoins(it) }
                    )

                    Screen.Tournaments -> TournamentsScreen(
                        tournaments = tournaments,
                        userProfile = userProfile,
                        onJoinTournament = { tour ->
                            viewModel.joinTournament(tour) { success ->
                                if (success) {
                                    categories.firstOrNull()?.let { cat ->
                                        viewModel.startQuiz(cat)
                                    }
                                }
                            }
                        }
                    )

                    Screen.Missions -> MissionsScreen(
                        missions = missions,
                        achievements = achievements,
                        onClaimMission = { id -> viewModel.claimMission(id) }
                    )

                    Screen.Leaderboard -> LeaderboardScreen()

                    Screen.Profile -> ProfileScreen(
                        userProfile = userProfile,
                        appConfig = appConfig,
                        onToggleSound = { viewModel.toggleSound() },
                        onToggleVibration = { viewModel.toggleVibration() },
                        onToggleLanguage = { viewModel.toggleLanguage() },
                        onToggleDarkMode = { viewModel.toggleDarkMode() },
                        onApplyReferralCode = { code -> viewModel.applyReferralCode(code) },
                        onOpenAdminPanel = { viewModel.setAdminMode(true) },
                        onOpenAuthDialog = { showAuthDialog = true },
                        onAdReward = { viewModel.earnAdRewardCoins(it) }
                    )
                }
            }
        }
    }
}
