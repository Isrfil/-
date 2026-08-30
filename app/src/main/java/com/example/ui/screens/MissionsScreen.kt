package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Achievement
import com.example.model.Mission
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.BrandGold
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandRose
import com.example.ui.theme.DarkCardBorderSubtle
import com.example.ui.theme.GoldenRewardGradient
import com.example.ui.theme.NeonIndigoGradient
import com.example.ui.theme.PrimaryGradient
import com.example.util.BengaliNumberFormatter
import com.example.util.ShareHelper

@Composable
fun MissionsScreen(
    missions: List<Mission>,
    achievements: List<Achievement>,
    onClaimMission: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("missions_screen")
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = BrandPrimary,
            indicator = { tabPositions ->
                if (selectedTab < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = BrandPrimary,
                        height = 3.dp
                    )
                }
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Text(
                        "মিশনসমূহ",
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 0) BrandPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Text(
                        "অর্জন ও ব্যাজ",
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 1) BrandPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )
        }

        if (selectedTab == 0) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "দৈনিক ও সাপ্তাহিক মিশন",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                items(missions, key = { it.id }) { mission ->
                    MissionItemCard(
                        mission = mission,
                        onClaimClick = { onClaimMission(mission.id) }
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "আপনার আনলককৃত ব্যাজ ও মেডেল",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                items(achievements, key = { it.id }) { achievement ->
                    AchievementItemCard(
                        achievement = achievement,
                        onShareClick = { ShareHelper.shareAchievement(context, achievement) }
                    )
                }
            }
        }
    }
}

@Composable
fun MissionItemCard(
    mission: Mission,
    onClaimClick: () -> Unit
) {
    val progressFraction = if (mission.targetCount > 0) {
        (mission.currentProgress.toFloat() / mission.targetCount).coerceIn(0f, 1f)
    } else 0f

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
        border = BorderStroke(1.dp, DarkCardBorderSubtle),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("mission_card_${mission.id}")
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
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (mission.type == "daily") BrandPrimary.copy(alpha = 0.15f) else BrandGold.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = if (mission.type == "daily") "দৈনিক মিশন" else "সাপ্তাহিক মিশন",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (mission.type == "daily") BrandPrimary else BrandGold,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "+${BengaliNumberFormatter.format(mission.rewardCoins)} 🪙",
                        style = MaterialTheme.typography.labelMedium,
                        color = BrandGold,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "+${BengaliNumberFormatter.format(mission.rewardXp)} XP",
                        style = MaterialTheme.typography.labelMedium,
                        color = BrandPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = mission.titleBn,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = mission.descriptionBn,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "অগ্রগতি: ${BengaliNumberFormatter.format(mission.currentProgress)} / ${BengaliNumberFormatter.format(mission.targetCount)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(8.dp)
                            .clip(CircleShape)
                            .background(Color(0x33334155))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction = progressFraction)
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(if (mission.isCompleted) BrandEmerald else BrandPrimary)
                        )
                    }
                }

                if (mission.isClaimed) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = BrandEmerald.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "ক্লেইমড ✓",
                            style = MaterialTheme.typography.labelSmall,
                            color = BrandEmerald,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                } else {
                    Button(
                        onClick = onClaimClick,
                        enabled = mission.isCompleted,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BrandGold,
                            contentColor = Color.Black
                        ),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("claim_mission_btn_${mission.id}")
                    ) {
                        Text(
                            text = if (mission.isCompleted) "ক্লেইম করুন" else "চলছে",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AchievementItemCard(
    achievement: Achievement,
    onShareClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (achievement.isUnlocked) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
        ),
        border = BorderStroke(1.dp, if (achievement.isUnlocked) BrandGold.copy(alpha = 0.3f) else DarkCardBorderSubtle),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("achievement_card_${achievement.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (achievement.isUnlocked) GoldenRewardGradient else androidx.compose.ui.graphics.SolidColor(Color.Gray.copy(alpha = 0.2f))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (achievement.isUnlocked) getAchievementIcon(achievement.iconName) else Icons.Default.Lock,
                    contentDescription = null,
                    tint = if (achievement.isUnlocked) Color.White else Color.Gray,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = achievement.titleBn,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (achievement.isUnlocked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = achievement.descriptionBn,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (achievement.isUnlocked && achievement.unlockedDate.isNotEmpty()) {
                    Text(
                        text = "আনলক করা হয়েছে: ${achievement.unlockedDate}",
                        style = MaterialTheme.typography.labelSmall,
                        color = BrandEmerald,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            if (achievement.isUnlocked) {
                IconButton(
                    onClick = onShareClick,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(BrandPrimary.copy(alpha = 0.15f))
                        .testTag("share_achievement_${achievement.id}")
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = BrandPrimary, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

fun getAchievementIcon(name: String): ImageVector {
    return when (name) {
        "local_fire_department" -> Icons.Default.LocalFireDepartment
        "military_tech" -> Icons.Default.MilitaryTech
        "workspace_premium" -> Icons.Default.WorkspacePremium
        "group_add" -> Icons.Default.GroupAdd
        else -> Icons.Default.EmojiEvents
    }
}
