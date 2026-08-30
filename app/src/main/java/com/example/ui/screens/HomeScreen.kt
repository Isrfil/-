package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppConfig
import com.example.model.LiveAnnouncement
import com.example.model.QuizCategory
import com.example.model.UserProfile
import com.example.ui.components.AdBannerComponent
import com.example.ui.components.CategoryCard
import com.example.ui.components.LiveAnnouncementTicker
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.BrandGold
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkCardBorderSubtle
import com.example.ui.theme.GoldenRewardGradient
import com.example.ui.theme.NeonIndigoGradient
import com.example.ui.theme.PrimaryGradient
import com.example.ui.theme.PrimaryGradientLight
import com.example.util.BengaliNumberFormatter
import com.example.util.ShareHelper

@Composable
fun HomeScreen(
    categories: List<QuizCategory>,
    announcements: List<LiveAnnouncement>,
    appConfig: AppConfig,
    userProfile: UserProfile,
    onCategoryClick: (QuizCategory) -> Unit,
    onQuickPlayClick: () -> Unit,
    onAnnouncementClick: (LiveAnnouncement) -> Unit,
    onClaimDailyReward: () -> Boolean,
    onNavigateToTournaments: () -> Unit,
    onAdReward: ((Int) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val canClaimDaily = (System.currentTimeMillis() - userProfile.lastRewardClaimEpoch) > 86400000L

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_grid")
    ) {
        // Hero Live Quiz Banner
        item(span = { GridItemSpan(2) }) {
            HeroBannerCard(
                onQuickPlayClick = onQuickPlayClick,
                modifier = Modifier.testTag("hero_banner_card")
            )
        }

        // In-App Ads Banner
        item(span = { GridItemSpan(2) }) {
            AdBannerComponent(
                onRewardEarned = onAdReward
            )
        }

        // Live Announcement Ticker
        if (announcements.isNotEmpty()) {
            item(span = { GridItemSpan(2) }) {
                LiveAnnouncementTicker(
                    announcements = announcements,
                    onAnnouncementClick = onAnnouncementClick
                )
            }
        }

        // Daily Mission / Goal Card (from Immersive UI Spec)
        item(span = { GridItemSpan(2) }) {
            DailyMissionProgressCard(
                quizzesPlayedToday = (userProfile.totalGamesPlayed % 5).coerceAtLeast(1),
                targetQuizzes = 5,
                bonusXp = 500,
                onCardClick = onQuickPlayClick
            )
        }

        // Daily Check-in / Claim Reward Card
        item(span = { GridItemSpan(2) }) {
            DailyRewardClaimCard(
                canClaim = canClaimDaily,
                rewardCoins = appConfig.dailyLoginRewardCoins,
                rewardXp = appConfig.dailyLoginRewardXp,
                currentStreak = userProfile.currentStreakDays,
                onClaimClick = { onClaimDailyReward() }
            )
        }

        // Categories Header
        item(span = { GridItemSpan(2) }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "কুইজ ক্যাটাগরি",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = "${BengaliNumberFormatter.format(categories.size)}টি বিভাগ",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Categories Grid Items (Styled with rounded-3xl and tinted glow)
        items(categories, key = { it.id }) { category ->
            CategoryCard(
                category = category,
                onClick = { onCategoryClick(category) }
            )
        }

        // Invite Friends Referral Banner (from Immersive UI Spec)
        item(span = { GridItemSpan(2) }) {
            InviteFriendsCard(
                referralCode = userProfile.referralCode,
                onShareClick = { ShareHelper.shareApp(context, userProfile.referralCode) }
            )
        }

        // Tournament Promo Card at the bottom
        item(span = { GridItemSpan(2) }) {
            TournamentPromoCard(
                onJoinClick = onNavigateToTournaments
            )
        }

        item(span = { GridItemSpan(2) }) {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun HeroBannerCard(
    onQuickPlayClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = BorderStroke(1.dp, DarkCardBorderSubtle),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.quiz_hero_banner),
                contentDescription = "Quiz Hero Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Dark Immersive Vignette Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.55f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = BrandGold.copy(alpha = 0.95f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stars,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "লাইভ কুইজ শো",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "আজকের মেধা পরীক্ষা",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "১০টি দ্রুত প্রশ্নে কয়েন ও এক্সপি জিতুন!",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }

                    Button(
                        onClick = onQuickPlayClick,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                        modifier = Modifier.testTag("quick_play_hero_btn")
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "খেলুন", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun DailyMissionProgressCard(
    quizzesPlayedToday: Int,
    targetQuizzes: Int,
    bonusXp: Int,
    onCardClick: () -> Unit
) {
    val progress = (quizzesPlayedToday.toFloat() / targetQuizzes.toFloat()).coerceIn(0f, 1f)
    val percentage = (progress * 100).toInt()

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "আজকের লক্ষ্য (DAILY MISSION)",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = BrandEmerald.copy(alpha = 0.12f),
                border = BorderStroke(1.dp, BrandEmerald.copy(alpha = 0.25f))
            ) {
                Text(
                    text = "${BengaliNumberFormatter.format(percentage)}% সম্পন্ন",
                    style = MaterialTheme.typography.labelSmall,
                    color = BrandEmerald,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }

        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
            border = BorderStroke(1.dp, DarkCardBorderSubtle),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .clickable { onCardClick() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${BengaliNumberFormatter.format(targetQuizzes)}টি কুইজ খেলুন",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "+${BengaliNumberFormatter.format(bonusXp)} এক্সপি (XP) বোনাস",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = "${BengaliNumberFormatter.format(quizzesPlayedToday)}/${BengaliNumberFormatter.format(targetQuizzes)}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = BrandPrimary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Glowing Neon Progress Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape)
                        .background(Color(0x33334155))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction = progress)
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(NeonIndigoGradient)
                    )
                }
            }
        }
    }
}

@Composable
fun DailyRewardClaimCard(
    canClaim: Boolean,
    rewardCoins: Int,
    rewardXp: Int,
    currentStreak: Int,
    onClaimClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
        border = BorderStroke(1.dp, DarkCardBorderSubtle),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("daily_reward_claim_card")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (canClaim) BrandGold.copy(alpha = 0.2f) else BrandEmerald.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (canClaim) Icons.Default.CardGiftcard else Icons.Default.Check,
                        contentDescription = "Daily Reward",
                        tint = if (canClaim) BrandGold else BrandEmerald,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = if (canClaim) "দৈনিক লগইন রিওয়ার্ড" else "আজকের রিওয়ার্ড গ্রহণ করেছেন",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "+${BengaliNumberFormatter.format(rewardCoins)} কয়েন • +${BengaliNumberFormatter.format(rewardXp)} এক্সপি",
                        style = MaterialTheme.typography.bodySmall,
                        color = BrandGold,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Button(
                onClick = onClaimClick,
                enabled = canClaim,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandGold,
                    contentColor = Color.Black
                ),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                modifier = Modifier.testTag("claim_daily_reward_btn")
            ) {
                Text(
                    text = if (canClaim) "ক্লেইম করুন" else "সম্পন্ন",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun InviteFriendsCard(
    referralCode: String,
    onShareClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(PrimaryGradient)
            .clickable { onShareClick() }
            .testTag("invite_friends_banner_card")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🎁", fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "বন্ধুদের ইনভাইট করুন",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "প্রতি রেফারেলে ১০০ কয়েন বোনাস!",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.85f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Button(
                onClick = onShareClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = BrandPrimary
                ),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "শেয়ার",
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun TournamentPromoCard(
    onJoinClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
        border = BorderStroke(1.dp, DarkCardBorderSubtle),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onJoinClick() }
            .testTag("tournament_promo_card")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PrimaryGradientLight)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(GoldenRewardGradient),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Tournament",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "লাইভ মেগা টুর্নামেন্ট",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "৫,০০০+ কয়েন প্রাইজ পুল • এখনই খেলুন",
                        style = MaterialTheme.typography.bodySmall,
                        color = BrandGold,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                tint = BrandPrimary
            )
        }
    }
}

