package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Primary Branding & Accent Colors (Immersive UI)
val BrandPrimary = Color(0xFF6366F1) // Indigo
val BrandSecondary = Color(0xFF8B5CF6) // Violet / Purple
val BrandTertiary = Color(0xFFA855F7) // Radiant Purple
val BrandGold = Color(0xFFF59E0B) // Amber Golden
val BrandEmerald = Color(0xFF10B981) // Emerald Green
val BrandCyan = Color(0xFF06B6D4) // Cyan
val BrandRose = Color(0xFFF43F5E) // Bright Rose

// Immersive Dark Theme Palette (HTML Design Spec: #0F1115 & #14171D)
val DarkBackground = Color(0xFF0F1115) // Deep Obsidian Canvas
val DarkSurface = Color(0xFF14171D) // Surface Card Container
val DarkSurfaceVariant = Color(0xFF1E232E) // Slate Elevated Glass Card
val DarkSurfaceCard = Color(0xFF181C24)
val DarkCardBorder = Color(0x1AFFFFFF) // 10% Translucent White Glass Border
val DarkCardBorderSubtle = Color(0x0DFFFFFF) // 5% White Glass Border
val DarkTextPrimary = Color(0xFFF1F5F9) // Slate-100
val DarkTextSecondary = Color(0xFF94A3B8) // Slate-400
val DarkTextMuted = Color(0xFF64748B) // Slate-500

// Light Theme Fallback Palette
val LightBackground = Color(0xFFF8FAFC)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceVariant = Color(0xFFF1F5F9)
val LightSurfaceCard = Color(0xFFFFFFFF)
val LightCardBorder = Color(0xFFE2E8F0)
val LightTextPrimary = Color(0xFF0F172A)
val LightTextSecondary = Color(0xFF64748B)

// Status & Feedback Colors
val SuccessGreen = Color(0xFF10B981)
val ErrorRed = Color(0xFFEF4444)
val WarningYellow = Color(0xFFF59E0B)
val InfoBlue = Color(0xFF3B82F6)

// Immersive Gradient Brushes
val PrimaryGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF6366F1), Color(0xFF9333EA))
)

val PrimaryGradientLight = Brush.linearGradient(
    colors = listOf(Color(0x336366F1), Color(0x339333EA))
)

val NeonIndigoGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF6366F1), Color(0xFFA855F7))
)

val GoldenRewardGradient = Brush.linearGradient(
    colors = listOf(Color(0xFFF59E0B), Color(0xFFD97706), Color(0xFFB45309))
)

val GoldenRewardGradientLight = Brush.linearGradient(
    colors = listOf(Color(0x26F59E0B), Color(0x26D97706), Color(0x26B45309))
)

val EmeraldVictoryGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF10B981), Color(0xFF059669))
)

val RoseChallengeGradient = Brush.linearGradient(
    colors = listOf(Color(0xFFF43F5E), Color(0xFFE11D48))
)

val NeonCyberGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF06B6D4), Color(0xFF3B82F6))
)

val DarkHeaderGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF14171D), Color(0xFF0F1115))
)

