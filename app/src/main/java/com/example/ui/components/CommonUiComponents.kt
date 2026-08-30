package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SportsCricket
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.QuizCategory
import com.example.model.UserProfile
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.BrandGold
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandRose
import com.example.ui.theme.BrandSecondary
import com.example.ui.theme.BrandTertiary
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkCardBorderSubtle
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.GoldenRewardGradient
import com.example.ui.theme.NeonIndigoGradient
import com.example.ui.theme.PrimaryGradient
import com.example.ui.theme.SuccessGreen
import com.example.util.BengaliNumberFormatter

@Composable
fun AppHeaderProfileBar(
    userProfile: UserProfile,
    onProfileClick: () -> Unit,
    onShareAppClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        tonalElevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .testTag("app_header_bar")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User Avatar & Greeting
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(18.dp))
                    .clickable { onProfileClick() }
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(PrimaryGradient)
                        .border(1.5.dp, Color(0x33FFFFFF), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userProfile.displayName.take(1),
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "স্বাগতম 👋",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                    Text(
                        text = userProfile.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Stat Badges (Coins + Gems/XP) & Share Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Glass Stats Pill
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    border = BorderStroke(1.dp, DarkCardBorderSubtle)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Coins
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🪙", fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = BengaliNumberFormatter.format(userProfile.coins),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = BrandGold
                            )
                        }

                        // Divider
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .width(1.dp)
                                .height(14.dp)
                                .background(Color(0x33FFFFFF))
                        )

                        // Streak / XP
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "💎", fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = BengaliNumberFormatter.format(userProfile.xp),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = BrandPrimary
                            )
                        }
                    }
                }

                // Share Button
                IconButton(
                    onClick = onShareAppClick,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                        .border(1.dp, DarkCardBorderSubtle, RoundedCornerShape(14.dp))
                        .testTag("share_app_header_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share App",
                        tint = BrandPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: QuizCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gradientColors = rememberGradientForCategory(category.id)
    val accentColor = gradientColors.firstOrNull() ?: BrandPrimary

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = accentColor.copy(alpha = 0.12f)
        ),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.25f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .testTag("category_card_${category.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Icon + Arrow Action Indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Brush.linearGradient(gradientColors)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getCategoryIcon(category.iconName),
                        contentDescription = category.nameBn,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Subtle Navigation Pill Indicator
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column {
                Text(
                    text = category.nameBn,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = "${BengaliNumberFormatter.format(category.questionCount)}+ নতুন কুইজ",
                    style = MaterialTheme.typography.bodySmall,
                    color = accentColor.copy(alpha = 0.9f),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun AnimatedCountdownTimer(
    totalSeconds: Int,
    remainingSeconds: Int,
    modifier: Modifier = Modifier
) {
    val progress = if (totalSeconds > 0) remainingSeconds.toFloat() / totalSeconds else 0f
    val color by animateColorAsState(
        targetValue = when {
            remainingSeconds > 8 -> SuccessGreen
            remainingSeconds > 4 -> BrandGold
            else -> ErrorRed
        },
        label = "timer_color"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (remainingSeconds <= 4) 1.15f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(64.dp)
            .scale(scale)
    ) {
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.size(64.dp),
            color = color,
            trackColor = color.copy(alpha = 0.2f),
            strokeWidth = 6.dp,
        )
        Text(
            text = BengaliNumberFormatter.format(remainingSeconds),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = color
        )
    }
}

@Composable
fun LifelineButton(
    title: String,
    icon: ImageVector,
    isUsed: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (isUsed) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f) else BrandPrimary.copy(alpha = 0.15f),
        border = if (!isUsed) BorderStroke(1.dp, BrandPrimary.copy(alpha = 0.4f)) else BorderStroke(1.dp, DarkCardBorderSubtle),
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(enabled = !isUsed) { onClick() }
            .testTag("lifeline_${title}")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isUsed) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f) else BrandPrimary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = if (isUsed) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f) else BrandPrimary
            )
        }
    }
}

fun getCategoryIcon(name: String): ImageVector {
    return when (name.lowercase()) {
        "flag" -> Icons.Default.Flag
        "globe" -> Icons.Default.Public
        "book" -> Icons.Default.Book
        "science" -> Icons.Default.Science
        "sports" -> Icons.Default.SportsCricket
        "mosque" -> Icons.Default.Mosque
        "calculate" -> Icons.Default.Calculate
        else -> Icons.Default.Quiz
    }
}

fun rememberGradientForCategory(categoryId: String): List<Color> {
    return when (categoryId) {
        "bangladesh" -> listOf(Color(0xFF059669), Color(0xFF10B981))
        "general_knowledge" -> listOf(Color(0xFF4F46E5), Color(0xFF6366F1))
        "literature" -> listOf(Color(0xFFD97706), Color(0xFFF59E0B))
        "science" -> listOf(Color(0xFF0284C7), Color(0xFF38BDF8))
        "sports" -> listOf(Color(0xFFE11D48), Color(0xFFFB7185))
        "islamic" -> listOf(Color(0xFF0D9488), Color(0xFF2DD4BF))
        "math_puzzle" -> listOf(Color(0xFF9333EA), Color(0xFFC084FC))
        else -> listOf(BrandPrimary, BrandSecondary)
    }
}

