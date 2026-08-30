package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.QuizCategory
import com.example.model.QuizQuestion
import com.example.ui.components.AnimatedCountdownTimer
import com.example.ui.components.LifelineButton
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.BrandGold
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandRose
import com.example.ui.theme.DarkCardBorderSubtle
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.GoldenRewardGradient
import com.example.ui.theme.NeonIndigoGradient
import com.example.ui.theme.PrimaryGradient
import com.example.ui.theme.SuccessGreen
import com.example.util.BengaliNumberFormatter

@Composable
fun QuizPlayScreen(
    category: QuizCategory?,
    questions: List<QuizQuestion>,
    currentQuestionIndex: Int,
    remainingTime: Int,
    selectedOptionIndex: Int,
    isAnswerRevealed: Boolean,
    score: Int,
    streak: Int,
    is5050Used: Boolean,
    isSkipUsed: Boolean,
    isHintUsed: Boolean,
    hiddenOptionIndices: Set<Int>,
    onOptionSelected: (Int) -> Unit,
    onNextQuestion: () -> Unit,
    onUse5050: () -> Unit,
    onUseSkip: () -> Unit,
    onUseHint: () -> Unit,
    onExitQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentQuestion = questions.getOrNull(currentQuestionIndex) ?: return
    val totalQuestions = questions.size
    val progress = (currentQuestionIndex + 1).toFloat() / totalQuestions

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
            .fillMaxSize()
            .testTag("quiz_play_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar: Exit, Category Title, Question Counter
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onExitQuiz,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                            .border(1.dp, DarkCardBorderSubtle, RoundedCornerShape(12.dp))
                            .testTag("quiz_exit_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Exit Quiz",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = category?.nameBn ?: "কুইজ",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = BrandPrimary.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, BrandPrimary.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = "${BengaliNumberFormatter.format(currentQuestionIndex + 1)} / ${BengaliNumberFormatter.format(totalQuestions)}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = BrandPrimary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = BrandPrimary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Timer and Score Streak Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Score & Streak Pill
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        border = BorderStroke(1.dp, DarkCardBorderSubtle)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "🪙", fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${BengaliNumberFormatter.format(score)} পয়েন্ট",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = BrandGold
                            )
                        }
                    }

                    if (streak > 1) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = BrandRose.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, BrandRose.copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = "🔥 ${BengaliNumberFormatter.format(streak)} ধারা!",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = BrandRose,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                // Animated Countdown Ring
                AnimatedCountdownTimer(
                    totalSeconds = currentQuestion.timeLimitSeconds,
                    remainingSeconds = remainingTime
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Question Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                border = BorderStroke(1.dp, DarkCardBorderSubtle),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("quiz_question_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = currentQuestion.questionBn,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )

                    if (isHintUsed && currentQuestion.explanationBn.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = BrandGold.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, BrandGold.copy(alpha = 0.3f))
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(imageVector = Icons.Default.Lightbulb, contentDescription = null, tint = BrandGold, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "ইঙ্গিত: ${currentQuestion.explanationBn.take(40)}...",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = BrandGold,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Options List
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                currentQuestion.optionsBn.forEachIndexed { index, optionText ->
                    val isHidden = hiddenOptionIndices.contains(index)
                    val isSelected = selectedOptionIndex == index
                    val isCorrect = index == currentQuestion.correctOptionIndex

                    if (!isHidden) {
                        OptionItemCard(
                            optionIndex = index,
                            optionText = optionText,
                            isSelected = isSelected,
                            isAnswerRevealed = isAnswerRevealed,
                            isCorrect = isCorrect,
                            onClick = { onOptionSelected(index) }
                        )
                    }
                }
            }

            // Explanation & Next Button
            AnimatedVisibility(
                visible = isAnswerRevealed,
                enter = fadeIn() + slideInVertically { it / 2 },
                exit = fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    if (currentQuestion.explanationBn.isNotEmpty()) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedOptionIndex == currentQuestion.correctOptionIndex)
                                    SuccessGreen.copy(alpha = 0.12f)
                                else
                                    ErrorRed.copy(alpha = 0.12f)
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (selectedOptionIndex == currentQuestion.correctOptionIndex) SuccessGreen.copy(alpha = 0.3f) else ErrorRed.copy(alpha = 0.3f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = if (selectedOptionIndex == currentQuestion.correctOptionIndex)
                                        Icons.Default.CheckCircle
                                    else
                                        Icons.Default.HelpOutline,
                                    contentDescription = null,
                                    tint = if (selectedOptionIndex == currentQuestion.correctOptionIndex) SuccessGreen else ErrorRed,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = currentQuestion.explanationBn,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onNextQuestion,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("quiz_next_btn")
                    ) {
                        Text(
                            text = if (currentQuestionIndex + 1 < totalQuestions) "পরবর্তী প্রশ্ন" else "ফলাফল দেখুন",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
                    }
                }
            }

            // Lifelines Row
            if (!isAnswerRevealed) {
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    LifelineButton(
                        title = "৫০ : ৫০",
                        icon = Icons.Default.RemoveCircleOutline,
                        isUsed = is5050Used,
                        onClick = onUse5050
                    )

                    LifelineButton(
                        title = "স্কিপ",
                        icon = Icons.Default.Redo,
                        isUsed = isSkipUsed,
                        onClick = onUseSkip
                    )

                    LifelineButton(
                        title = "ইঙ্গিত",
                        icon = Icons.Default.Lightbulb,
                        isUsed = isHintUsed,
                        onClick = onUseHint
                    )
                }
            }
        }
    }
}

@Composable
fun OptionItemCard(
    optionIndex: Int,
    optionText: String,
    isSelected: Boolean,
    isAnswerRevealed: Boolean,
    isCorrect: Boolean,
    onClick: () -> Unit
) {
    val optionPrefix = when (optionIndex) {
        0 -> "ক"
        1 -> "খ"
        2 -> "গ"
        else -> "ঘ"
    }

    val backgroundColor by animateColorAsState(
        targetValue = when {
            isAnswerRevealed && isCorrect -> SuccessGreen.copy(alpha = 0.2f)
            isAnswerRevealed && isSelected && !isCorrect -> ErrorRed.copy(alpha = 0.2f)
            isSelected -> BrandPrimary.copy(alpha = 0.18f)
            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        },
        label = "option_bg"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            isAnswerRevealed && isCorrect -> SuccessGreen
            isAnswerRevealed && isSelected && !isCorrect -> ErrorRed
            isSelected -> BrandPrimary
            else -> DarkCardBorderSubtle
        },
        label = "option_border"
    )

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable(enabled = !isAnswerRevealed) { onClick() }
            .testTag("quiz_option_$optionIndex")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        when {
                            isAnswerRevealed && isCorrect -> SuccessGreen
                            isAnswerRevealed && isSelected && !isCorrect -> ErrorRed
                            isSelected -> BrandPrimary
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = optionPrefix,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isSelected || (isAnswerRevealed && isCorrect) || (isAnswerRevealed && isSelected && !isCorrect)) Color.White else MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = optionText,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

            if (isAnswerRevealed && isCorrect) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Correct",
                    tint = SuccessGreen,
                    modifier = Modifier.size(24.dp)
                )
            } else if (isAnswerRevealed && isSelected && !isCorrect) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Wrong",
                    tint = ErrorRed,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

