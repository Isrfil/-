package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LiveAnnouncement
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.BrandGold
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandRose

@Composable
fun LiveAnnouncementTicker(
    announcements: List<LiveAnnouncement>,
    onAnnouncementClick: (LiveAnnouncement) -> Unit,
    modifier: Modifier = Modifier
) {
    if (announcements.isEmpty()) return

    val topAnnouncement = announcements.firstOrNull() ?: return
    val accentColor = getAnnouncementColor(topAnnouncement.type)

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = accentColor.copy(alpha = 0.15f)),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onAnnouncementClick(topAnnouncement) }
            .testTag("live_announcement_ticker")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(accentColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getAnnouncementIcon(topAnnouncement.type),
                    contentDescription = "Announcement",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = getAnnouncementTypeLabelBn(topAnnouncement.type).uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = accentColor,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp,
                    fontSize = 10.sp
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = topAnnouncement.titleBn,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )

                Text(
                    text = topAnnouncement.messageBn,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.9f),
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun AnnouncementDetailDialog(
    announcement: LiveAnnouncement,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = getAnnouncementIcon(announcement.type),
                        contentDescription = null,
                        tint = getAnnouncementColor(announcement.type),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = announcement.titleBn,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        text = {
            Column {
                Text(
                    text = announcement.messageBn,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
            ) {
                Text("ঠিক আছে", fontWeight = FontWeight.Bold)
            }
        }
    )
}

fun getAnnouncementIcon(type: String): ImageVector {
    return when (type.lowercase()) {
        "tournament" -> Icons.Default.EmojiEvents
        "reward" -> Icons.Default.NotificationsActive
        "maintenance" -> Icons.Default.Warning
        "update" -> Icons.Default.Info
        else -> Icons.Default.Campaign
    }
}

fun getAnnouncementColor(type: String): Color {
    return when (type.lowercase()) {
        "tournament" -> BrandGold
        "reward" -> BrandEmerald
        "maintenance" -> BrandRose
        "update" -> BrandPrimary
        else -> BrandPrimary
    }
}

fun getAnnouncementTypeLabelBn(type: String): String {
    return when (type.lowercase()) {
        "tournament" -> "টুর্নামেন্ট"
        "reward" -> "পুরস্কার"
        "maintenance" -> "জরুরি নোটিশ"
        "update" -> "নতুন আপডেট"
        else -> "নতুন ঘোষণা"
    }
}

