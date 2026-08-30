package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import com.example.model.Tournament
import com.example.model.UserProfile
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.BrandGold
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkCardBorderSubtle
import com.example.ui.theme.GoldenRewardGradient
import com.example.ui.theme.GoldenRewardGradientLight
import com.example.ui.theme.NeonIndigoGradient
import com.example.ui.theme.PrimaryGradient
import com.example.util.BengaliNumberFormatter
import com.example.util.ShareHelper

@Composable
fun TournamentsScreen(
    tournaments: List<Tournament>,
    userProfile: UserProfile,
    onJoinTournament: (Tournament) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTournamentForRules by remember { mutableStateOf<Tournament?>(null) }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = modifier
            .fillMaxSize()
            .testTag("tournaments_screen")
    ) {
        item {
            TournamentHeaderCard(
                userCoins = userProfile.coins
            )
        }

        item {
            Text(
                text = "চলমান ও আসন্ন টুর্নামেন্ট",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        items(tournaments, key = { it.id }) { tournament ->
            TournamentItemCard(
                tournament = tournament,
                canAfford = userProfile.coins >= tournament.entryFeeCoins,
                onJoinClick = {
                    if (userProfile.coins >= tournament.entryFeeCoins) {
                        onJoinTournament(tournament)
                    } else {
                        Toast.makeText(context, "পর্যাপ্ত কয়েন নেই! কুইজ খেলে কয়েন অর্জন করুন।", Toast.LENGTH_SHORT).show()
                    }
                },
                onRulesClick = { selectedTournamentForRules = tournament },
                onShareClick = { ShareHelper.shareTournament(context, tournament) }
            )
        }
    }

    selectedTournamentForRules?.let { tour ->
        AlertDialog(
            onDismissRequest = { selectedTournamentForRules = null },
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            title = {
                Text(
                    text = tour.titleBn,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Column {
                    Text(
                        text = "টুর্নামেন্টের নিয়মাবলী:",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = tour.rulesBn,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "প্রাইজ পুল বণ্টন: ১ম স্থান: ৫০% • ২য় স্থান: ৩০% • ৩য় স্থান: ২০%",
                        style = MaterialTheme.typography.labelSmall,
                        color = BrandGold,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { selectedTournamentForRules = null },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("বুঝেছি")
                }
            }
        )
    }
}

@Composable
fun TournamentHeaderCard(userCoins: Int) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, BrandGold.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(GoldenRewardGradientLight)
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "🏆 কুইজ চ্যাম্পিয়নশীপ",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "মেধাবীদের সাথে সরাসরি প্রতিযোগিতা করুন",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFCBD5E1)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = BrandGold.copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, BrandGold.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🪙", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = BengaliNumberFormatter.format(userCoins),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = BrandGold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TournamentItemCard(
    tournament: Tournament,
    canAfford: Boolean,
    onJoinClick: () -> Unit,
    onRulesClick: () -> Unit,
    onShareClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
        border = BorderStroke(1.dp, DarkCardBorderSubtle),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("tournament_item_${tournament.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (tournament.status == "active") BrandEmerald.copy(alpha = 0.18f) else BrandPrimary.copy(alpha = 0.18f)
                ) {
                    Text(
                        text = if (tournament.status == "active") "● লাইভ চলছে" else "আসন্ন টুর্নামেন্ট",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (tournament.status == "active") BrandEmerald else BrandPrimary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onRulesClick,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = "Rules", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    IconButton(
                        onClick = onShareClick,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(BrandPrimary.copy(alpha = 0.15f))
                            .testTag("share_tournament_${tournament.id}")
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = BrandPrimary, modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = tournament.titleBn,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = tournament.descriptionBn,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Details Badges (Prize Pool, Entry, Participants)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TournamentDetailBadge(
                    icon = Icons.Default.EmojiEvents,
                    label = "প্রাইজ পুল",
                    value = "${BengaliNumberFormatter.format(tournament.prizePoolCoins)} 🪙",
                    color = BrandGold
                )

                TournamentDetailBadge(
                    icon = Icons.Default.MonetizationOn,
                    label = "এন্ট্রি ফি",
                    value = "${BengaliNumberFormatter.format(tournament.entryFeeCoins)} 🪙",
                    color = BrandPrimary
                )

                TournamentDetailBadge(
                    icon = Icons.Default.Groups,
                    label = "অংশগ্রহণকারী",
                    value = "${BengaliNumberFormatter.format(tournament.totalParticipants)}+",
                    color = BrandEmerald
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onJoinClick,
                enabled = tournament.status == "active",
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandGold,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("join_tournament_btn_${tournament.id}")
            ) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (tournament.status == "active") "অংশগ্রহণ করুন (${BengaliNumberFormatter.format(tournament.entryFeeCoins)} কয়েন)" else "শীঘ্রই শুরু হচ্ছে",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun TournamentDetailBadge(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    color: Color
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(3.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(15.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
