package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.model.LiveAnnouncement
import com.example.model.QuizCategory
import com.example.model.QuizQuestion
import com.example.model.Tournament
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.BrandGold
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandRose
import com.example.ui.theme.GoldenRewardGradient
import com.example.ui.theme.PrimaryGradient
import com.example.ui.theme.SuccessGreen
import com.example.util.BengaliNumberFormatter
import java.util.UUID

@Composable
fun AdminPanelScreen(
    appConfig: AppConfig,
    categories: List<QuizCategory>,
    pendingAiQuestions: List<QuizQuestion>,
    onUpdateAppConfig: (AppConfig) -> Unit,
    onPublishAnnouncement: (LiveAnnouncement) -> Unit,
    onGenerateAiQuestion: (String, String) -> Unit,
    onApproveAiQuestion: (QuizQuestion) -> Unit,
    onRejectAiQuestion: (String) -> Unit,
    onAddNewQuestion: (QuizQuestion) -> Unit,
    onCreateTournament: (Tournament) -> Unit,
    onExitAdmin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) }

    // Config form states
    var xpMultiplier by remember(appConfig) { mutableStateOf(appConfig.correctXpMultiplier.toString()) }
    var coinReward by remember(appConfig) { mutableStateOf(appConfig.correctCoinReward.toString()) }
    var isMaintenance by remember(appConfig) { mutableStateOf(appConfig.isMaintenanceMode) }
    var maintenanceMsg by remember(appConfig) { mutableStateOf(appConfig.maintenanceMessageBn) }
    var minVersionCode by remember(appConfig) { mutableStateOf(appConfig.minSupportedVersionCode.toString()) }
    var updateNotes by remember(appConfig) { mutableStateOf(appConfig.updateNotesBn) }

    // Announcement form states
    var announcementTitle by remember { mutableStateOf("") }
    var announcementMsg by remember { mutableStateOf("") }
    var announcementType by remember { mutableStateOf("event") }

    // Tournament form states
    var tourTitle by remember { mutableStateOf("") }
    var tourPrize by remember { mutableStateOf("5000") }
    var tourFee by remember { mutableStateOf("20") }
    var tourDesc by remember { mutableStateOf("") }

    // Manual Question form states
    var manualQText by remember { mutableStateOf("") }
    var manualOpt1 by remember { mutableStateOf("") }
    var manualOpt2 by remember { mutableStateOf("") }
    var manualOpt3 by remember { mutableStateOf("") }
    var manualOpt4 by remember { mutableStateOf("") }
    var manualExplanation by remember { mutableStateOf("") }
    var manualCorrectOptIdx by remember { mutableIntStateOf(0) }
    var selectedCatId by remember { mutableStateOf(categories.firstOrNull()?.id ?: "bangladesh") }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
            .fillMaxSize()
            .testTag("admin_panel_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Admin Top Bar
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 3.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onExitAdmin,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .testTag("exit_admin_btn")
                        ) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Exit Admin")
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Admin Control Studio",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Firebase Firestore Realtime Sync",
                                style = MaterialTheme.typography.labelSmall,
                                color = BrandEmerald,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = BrandEmerald.copy(alpha = 0.15f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.CloudSync, contentDescription = null, tint = BrandEmerald, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "লাইভ কানেক্টেড", style = MaterialTheme.typography.labelSmall, color = BrandEmerald, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Tab Navigation
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = BrandPrimary
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("কনফিগারেশন", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("এআই কুইজ (${pendingAiQuestions.size})", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("ঘোষণা ও টুর্নামেন্ট", fontWeight = FontWeight.Bold) }
                )
            }

            // Tab Contents
            when (selectedTab) {
                0 -> {
                    // Centralized App Config (Requirement 53 & 55)
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "🎮 গেম রিওয়ার্ড ও পয়েন্ট কনফিগারেশন",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))

                                    OutlinedTextField(
                                        value = xpMultiplier,
                                        onValueChange = { xpMultiplier = it },
                                        label = { Text("সঠিক উত্তর XP মাল্টিপ্লায়ার (ডিফল্ট: ১০)") },
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    OutlinedTextField(
                                        value = coinReward,
                                        onValueChange = { coinReward = it },
                                        label = { Text("প্রতি সঠিক উত্তরের কয়েন রিওয়ার্ড (ডিফল্ট: ৫)") },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }

                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "⚡ রক্ষণাবেক্ষণ ও ফোর্স আপডেট সেটিংস",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "সার্ভার মেইনটেন্যান্স মোড (লক স্ক্রিন)", fontWeight = FontWeight.Medium)
                                        Switch(
                                            checked = isMaintenance,
                                            onCheckedChange = { isMaintenance = it },
                                            colors = SwitchDefaults.colors(checkedThumbColor = BrandRose)
                                        )
                                    }

                                    if (isMaintenance) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        OutlinedTextField(
                                            value = maintenanceMsg,
                                            onValueChange = { maintenanceMsg = it },
                                            label = { Text("মেইনটেন্যান্স বার্তা") },
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    OutlinedTextField(
                                        value = minVersionCode,
                                        onValueChange = { minVersionCode = it },
                                        label = { Text("মিনিমাম সাপোর্ট ভার্সন কোড (ফোর্স আপডেট ট্রিগার)") },
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    OutlinedTextField(
                                        value = updateNotes,
                                        onValueChange = { updateNotes = it },
                                        label = { Text("আপডেট রিলিজ নোটস (বাংলা)") },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }

                        item {
                            Button(
                                onClick = {
                                    val newConfig = appConfig.copy(
                                        correctXpMultiplier = xpMultiplier.toIntOrNull() ?: 10,
                                        correctCoinReward = coinReward.toIntOrNull() ?: 5,
                                        isMaintenanceMode = isMaintenance,
                                        maintenanceMessageBn = maintenanceMsg,
                                        minSupportedVersionCode = minVersionCode.toIntOrNull() ?: 1,
                                        updateNotesBn = updateNotes
                                    )
                                    onUpdateAppConfig(newConfig)
                                    Toast.makeText(context, "কনফিগারেশন ক্লাউডে সেভ ও সিঙ্ক হয়েছে!", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BrandEmerald),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("save_config_btn")
                            ) {
                                Icon(imageVector = Icons.Default.Save, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "সেভ করুন এবং ক্লাউডে পুশ করুন", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                1 -> {
                    // AI Question Generator & Approvals (Requirement 56 & 57)
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = BrandPrimary)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Gemini AI কুইজ জেনারেটর",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "যেকোনো বিষয়ের জন্য এক ক্লিকে বাংলা প্রশ্ন ও ৪টি অপশন জেনারেট করে রিভিউ ও পাবলিশ করুন।",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Button(
                                            onClick = {
                                                val cat = categories.firstOrNull { it.id == "bangladesh" } ?: categories.first()
                                                onGenerateAiQuestion(cat.id, cat.nameBn)
                                                Toast.makeText(context, "AI প্রশ্ন জেনারেট করা হয়েছে!", Toast.LENGTH_SHORT).show()
                                            },
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                                            modifier = Modifier.weight(1f).testTag("ai_generate_bd_btn")
                                        ) {
                                            Text("বাংলাদেশ ইতিহাস", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }

                                        Button(
                                            onClick = {
                                                val cat = categories.firstOrNull { it.id == "science" } ?: categories.first()
                                                onGenerateAiQuestion(cat.id, cat.nameBn)
                                                Toast.makeText(context, "AI প্রশ্ন জেনারেট করা হয়েছে!", Toast.LENGTH_SHORT).show()
                                            },
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                                            modifier = Modifier.weight(1f).testTag("ai_generate_sci_btn")
                                        ) {
                                            Text("বিজ্ঞান ও AI", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }

                        if (pendingAiQuestions.isEmpty()) {
                            item {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "কোনো পেন্ডিং এআই প্রশ্ন নেই। নতুন প্রশ্ন জেনারেট করুন।",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                        } else {
                            item {
                                Text(
                                    text = "অনুমোদনের জন্য অপেক্ষমাণ প্রশ্ন (${pendingAiQuestions.size})",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }

                            items(pendingAiQuestions, key = { it.id }) { aiQ ->
                                Card(
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = BrandPrimary.copy(alpha = 0.15f)
                                        ) {
                                            Text(
                                                text = "AI তৈরি করেছে • ক্যাটাগরি: ${aiQ.categoryId}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = BrandPrimary,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                            text = aiQ.questionBn,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        aiQ.optionsBn.forEachIndexed { optIdx, optTxt ->
                                            val isCorrect = optIdx == aiQ.correctOptionIndex
                                            Text(
                                                text = "${if (isCorrect) "✓ " else "• "}$optTxt",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = if (isCorrect) SuccessGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                                                fontWeight = if (isCorrect) FontWeight.Bold else FontWeight.Normal
                                            )
                                        }

                                        if (aiQ.explanationBn.isNotEmpty()) {
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = "ব্যাখ্যা: ${aiQ.explanationBn}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Button(
                                                onClick = {
                                                    onApproveAiQuestion(aiQ)
                                                    Toast.makeText(context, "প্রশ্ন অনুমোদিত এবং লাইভ করা হয়েছে!", Toast.LENGTH_SHORT).show()
                                                },
                                                shape = RoundedCornerShape(10.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = BrandEmerald),
                                                modifier = Modifier.weight(1f).testTag("approve_ai_btn_${aiQ.id}")
                                            ) {
                                                Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("অনুমোদন করুন")
                                            }

                                            OutlinedButton(
                                                onClick = { onRejectAiQuestion(aiQ.id) },
                                                shape = RoundedCornerShape(10.dp),
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Icon(imageVector = Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("বাতিল")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // Announcements & Tournaments Publisher (Requirement 54 & 56)
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Announcement Publisher
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.Campaign, contentDescription = null, tint = BrandGold)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "লাইভ ঘোষণা পাবলিশ করুন",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    OutlinedTextField(
                                        value = announcementTitle,
                                        onValueChange = { announcementTitle = it },
                                        label = { Text("ঘোষণার শিরোনাম (বাংলা)") },
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    OutlinedTextField(
                                        value = announcementMsg,
                                        onValueChange = { announcementMsg = it },
                                        label = { Text("ঘোষণার বিস্তারিত বিবরণ") },
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Button(
                                        onClick = {
                                            if (announcementTitle.isNotBlank() && announcementMsg.isNotBlank()) {
                                                val ann = LiveAnnouncement(
                                                    id = "ann_" + UUID.randomUUID().toString().take(6),
                                                    titleBn = announcementTitle,
                                                    messageBn = announcementMsg,
                                                    type = "event",
                                                    timestamp = System.currentTimeMillis()
                                                )
                                                onPublishAnnouncement(ann)
                                                announcementTitle = ""
                                                announcementMsg = ""
                                                Toast.makeText(context, "ঘোষণা লাইভ পাবলিশ হয়েছে!", Toast.LENGTH_SHORT).show()
                                            } else {
                                                Toast.makeText(context, "শিরোনাম ও বিবরণ দিন!", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = BrandGold, contentColor = Color.Black),
                                        modifier = Modifier.fillMaxWidth().testTag("publish_announcement_btn")
                                    ) {
                                        Icon(imageVector = Icons.Default.Campaign, contentDescription = null)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("ঘোষণা পাবলিশ করুন", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        // Tournament Creator
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.EmojiEvents, contentDescription = null, tint = BrandPrimary)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "নতুন লাইভ টুর্নামেন্ট চালু করুন",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    OutlinedTextField(
                                        value = tourTitle,
                                        onValueChange = { tourTitle = it },
                                        label = { Text("টুর্নামেন্টের নাম (যেমন: ঢাকা মেগা কুইজ কাপ)") },
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        OutlinedTextField(
                                            value = tourPrize,
                                            onValueChange = { tourPrize = it },
                                            label = { Text("প্রাইজ পুল কয়েন") },
                                            modifier = Modifier.weight(1f)
                                        )

                                        OutlinedTextField(
                                            value = tourFee,
                                            onValueChange = { tourFee = it },
                                            label = { Text("এন্ট্রি ফি") },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    OutlinedTextField(
                                        value = tourDesc,
                                        onValueChange = { tourDesc = it },
                                        label = { Text("টুর্নামেন্টের বিবরণ ও নিয়ম") },
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Button(
                                        onClick = {
                                            if (tourTitle.isNotBlank()) {
                                                val tournament = Tournament(
                                                    id = "tour_" + UUID.randomUUID().toString().take(6),
                                                    titleBn = tourTitle,
                                                    descriptionBn = if (tourDesc.isBlank()) "লাইভ মেগা কুইজ চ্যাম্পিয়নশীপ" else tourDesc,
                                                    entryFeeCoins = tourFee.toIntOrNull() ?: 20,
                                                    prizePoolCoins = tourPrize.toIntOrNull() ?: 5000,
                                                    status = "active",
                                                    rulesBn = "প্রতি সঠিক উত্তরে ২০ পয়েন্ট। দ্রুত উত্তর দিলে স্পিড বোনাস পয়েন্ট।"
                                                )
                                                onCreateTournament(tournament)
                                                tourTitle = ""
                                                tourDesc = ""
                                                Toast.makeText(context, "টুর্নামেন্ট সফলভাবে তৈরি ও লাইভ হয়েছে!", Toast.LENGTH_SHORT).show()
                                            } else {
                                                Toast.makeText(context, "টুর্নামেন্টের নাম দিন!", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                                        modifier = Modifier.fillMaxWidth().testTag("create_tournament_btn")
                                    ) {
                                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("টুর্নামেন্ট শুরু করুন", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
