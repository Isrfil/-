package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppConfig
import com.example.model.UserProfile
import com.example.ui.components.AdBannerComponent
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.BrandGold
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandRose
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkCardBorderSubtle
import com.example.ui.theme.GoldenRewardGradient
import com.example.ui.theme.GoldenRewardGradientLight
import com.example.ui.theme.NeonIndigoGradient
import com.example.ui.theme.PrimaryGradient
import com.example.util.BengaliNumberFormatter
import com.example.util.ShareHelper

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    appConfig: AppConfig,
    onToggleSound: () -> Unit,
    onToggleVibration: () -> Unit,
    onToggleLanguage: () -> Unit,
    onToggleDarkMode: () -> Unit,
    onApplyReferralCode: (String) -> Boolean,
    onOpenAdminPanel: () -> Unit,
    onOpenAuthDialog: (() -> Unit)? = null,
    onAdReward: ((Int) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showReferralInputDialog by remember { mutableStateOf(false) }
    var enteredReferralCode by remember { mutableStateOf("") }
    var showAdminPinDialog by remember { mutableStateOf(false) }
    var enteredPin by remember { mutableStateOf("") }

    val nextLevelXp = userProfile.level * 200
    val currentLevelXp = (userProfile.level - 1) * 200
    val levelProgress = ((userProfile.xp - currentLevelXp).toFloat() / (nextLevelXp - currentLevelXp)).coerceIn(0f, 1f)

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = modifier
            .fillMaxSize()
            .testTag("profile_screen")
    ) {
        // User Profile Card
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                border = BorderStroke(1.dp, DarkCardBorderSubtle),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(PrimaryGradient),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = userProfile.displayName.take(1),
                                color = Color.White,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = userProfile.displayName,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = userProfile.email,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = BrandPrimary.copy(alpha = 0.15f),
                                border = BorderStroke(1.dp, BrandPrimary.copy(alpha = 0.3f))
                            ) {
                                Text(
                                    text = "লেভেল ${BengaliNumberFormatter.format(userProfile.level)} কুইজার",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = BrandPrimary,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        IconButton(
                            onClick = { onOpenAuthDialog?.invoke() },
                            modifier = Modifier.testTag("profile_login_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Login,
                                contentDescription = "Login / Switch Account",
                                tint = BrandPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // XP Progress
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "লেভেল ${BengaliNumberFormatter.format(userProfile.level + 1)} আনলক",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${BengaliNumberFormatter.format(userProfile.xp)} / ${BengaliNumberFormatter.format(nextLevelXp)} XP",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = BrandPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(CircleShape)
                                .background(Color(0x33334155))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(fraction = levelProgress)
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(NeonIndigoGradient)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Stats Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ProfileStatItem(
                            label = "মোট খেলা",
                            value = BengaliNumberFormatter.format(userProfile.totalGamesPlayed)
                        )
                        ProfileStatItem(
                            label = "বিজয়",
                            value = BengaliNumberFormatter.format(userProfile.totalWins)
                        )
                        ProfileStatItem(
                            label = "সঠিক উত্তর",
                            value = BengaliNumberFormatter.format(userProfile.correctAnswersCount)
                        )
                        ProfileStatItem(
                            label = "সর্বোচ্চ ধারা",
                            value = "${BengaliNumberFormatter.format(userProfile.bestStreakDays)} দিন"
                        )
                    }
                }
            }
        }

        // In-App Reward Ad Placement in Profile
        item {
            AdBannerComponent(
                onRewardEarned = onAdReward
            )
        }

        // Referral & Invite Friends Card (Requirements 52 & 58)
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                border = BorderStroke(1.dp, BrandGold.copy(alpha = 0.35f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("referral_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(GoldenRewardGradientLight)
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.CardGiftcard, contentDescription = null, tint = BrandGold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "রেফার করুন এবং কয়েন জিতুন",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = BrandGold.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, BrandGold.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = "+${BengaliNumberFormatter.format(appConfig.referralRewardCoins)} কয়েন",
                                style = MaterialTheme.typography.labelSmall,
                                color = BrandGold,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "বন্ধুকে আপনার রেফারেল লিঙ্ক শেয়ার করলেই আপনি পাবেন ১০০ কয়েন এবং আপনার বন্ধু পাবে ৫০ বোনাস কয়েন!",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFCBD5E1)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Referral Code Box
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1E2430))
                            .border(1.dp, BrandGold.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "আপনার রেফারেল কোড",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF94A3B8)
                            )
                            Text(
                                text = userProfile.referralCode,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = BrandGold
                            )
                        }

                        Row {
                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    clipboard.setPrimaryClip(ClipData.newPlainText("Referral Link", ShareHelper.getReferralLink(userProfile.referralCode)))
                                    Toast.makeText(context, "রেফারেল লিঙ্ক কপি করা হয়েছে!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(BrandPrimary.copy(alpha = 0.2f))
                                    .testTag("copy_referral_btn")
                            ) {
                                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = BrandPrimary, modifier = Modifier.size(18.dp))
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            IconButton(
                                onClick = { ShareHelper.inviteFriends(context, userProfile.referralCode) },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(BrandGold.copy(alpha = 0.2f))
                                    .testTag("invite_friends_btn")
                            ) {
                                Icon(imageVector = Icons.Default.Share, contentDescription = "Invite", tint = BrandGold, modifier = Modifier.size(18.dp))
                            }
                        }
                    }

                    if (userProfile.referredBy.isEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "বন্ধুর রেফারেল কোড আছে? এখানে লিখুন ❯",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = BrandPrimary,
                            modifier = Modifier
                                .clickable { showReferralInputDialog = true }
                                .padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Settings Section
        item {
            Text(
                text = "অ্যাপ সেটিংস",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                border = BorderStroke(1.dp, DarkCardBorderSubtle),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    SettingToggleRow(
                        icon = Icons.Default.DarkMode,
                        title = "ডার্ক মোড (Dark Theme)",
                        checked = userProfile.isDarkMode,
                        onCheckedChange = { onToggleDarkMode() }
                    )

                    SettingToggleRow(
                        icon = Icons.Default.Language,
                        title = "বাংলা ভাষা অগ্রাধিকার",
                        checked = userProfile.isBengaliLanguage,
                        onCheckedChange = { onToggleLanguage() }
                    )

                    SettingToggleRow(
                        icon = Icons.Default.VolumeUp,
                        title = "সাউন্ড এফেক্ট",
                        checked = userProfile.isSoundEnabled,
                        onCheckedChange = { onToggleSound() }
                    )

                    SettingToggleRow(
                        icon = Icons.Default.Vibration,
                        title = "স্পর্শ ভাইব্রেশন (Haptic)",
                        checked = userProfile.isVibrationEnabled,
                        onCheckedChange = { onToggleVibration() }
                    )
                }
            }
        }

        // App Info & Admin Panel Trigger
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                border = BorderStroke(1.dp, DarkCardBorderSubtle),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(ShareHelper.PLAY_STORE_URL))
                                context.startActivity(intent)
                            }
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Shop, contentDescription = null, tint = BrandPrimary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Play Store এ রেটিং দিন", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Bangla Quiz v1.0", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Admin Dashboard Button (Requirement 57)
                    Button(
                        onClick = { showAdminPinDialog = true },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary.copy(alpha = 0.15f), contentColor = BrandPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_panel_open_btn")
                    ) {
                        Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "অ্যাডমিন কন্ট্রোল প্যানেল (Admin Studio)", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // Referral Input Dialog
    if (showReferralInputDialog) {
        AlertDialog(
            onDismissRequest = { showReferralInputDialog = false },
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            title = { Text("রেফারেল কোড দিন", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
            text = {
                Column {
                    Text("বন্ধুর রেফারেল কোড লিখলে সাথে সাথে ৫০ কয়েন বোনাস পাবেন।", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = enteredReferralCode,
                        onValueChange = { enteredReferralCode = it.uppercase() },
                        label = { Text("কোড লিখুন (যেমন: QUIZ789)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val success = onApplyReferralCode(enteredReferralCode)
                        if (success) {
                            Toast.makeText(context, "অভিনন্দন! ৫০ কয়েন বোনাস পেয়েছেন!", Toast.LENGTH_LONG).show()
                            showReferralInputDialog = false
                        } else {
                            Toast.makeText(context, "ভুল বা অকার্যকর রেফারেল কোড!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("প্রয়োগ করুন")
                }
            },
            dismissButton = {
                Button(onClick = { showReferralInputDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = Color.Gray), shape = RoundedCornerShape(10.dp)) {
                    Text("বাতিল")
                }
            }
        )
    }

    // Admin PIN Dialog
    if (showAdminPinDialog) {
        AlertDialog(
            onDismissRequest = { showAdminPinDialog = false },
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = null, tint = BrandPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("অ্যাডমিন প্যানেল এক্সেস", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                }
            },
            text = {
                Column {
                    Text("ক্লাউড কনফিগারেশন, এআই কুইজ তৈরি ও লাইভ অ্যানাউন্সমেন্ট পরিচালনার জন্য পিন লিখুন বা সরাসরি প্রবেশ করুন (Default PIN: 1234)।", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = enteredPin,
                        onValueChange = { enteredPin = it },
                        label = { Text("পিন (Default: 1234)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (enteredPin.isEmpty() || enteredPin == "1234" || enteredPin == "admin") {
                            showAdminPinDialog = false
                            onOpenAdminPanel()
                        } else {
                            Toast.makeText(context, "সঠিক পিন দিন!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("প্রবেশ করুন")
                }
            },
            dismissButton = {
                Button(onClick = { showAdminPinDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = Color.Gray), shape = RoundedCornerShape(10.dp)) {
                    Text("বাতিল")
                }
            }
        )
    }
}

@Composable
fun SettingToggleRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = BrandPrimary, checkedTrackColor = BrandPrimary.copy(alpha = 0.4f))
        )
    }
}

@Composable
fun ProfileStatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
