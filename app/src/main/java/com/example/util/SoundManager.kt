package com.example.util

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.media.ToneGenerator
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sin

/**
 * Sound Manager for Quiz Interactions:
 * - Correct answer chime
 * - Wrong answer buzzer
 * - Button click
 * - Timer tick
 * - Celebration fanfare
 * - Lifeline sound
 */
object SoundManager {
    private const val TAG = "SoundManager"
    var isSoundEnabled = true

    private var toneGenerator: ToneGenerator? = try {
        ToneGenerator(AudioManager.STREAM_MUSIC, 80)
    } catch (e: Exception) {
        null
    }

    fun playClick() {
        if (!isSoundEnabled) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 35)
        } catch (e: Exception) {
            Log.e(TAG, "Error playing click", e)
        }
    }

    fun playCorrect() {
        if (!isSoundEnabled) return
        CoroutineScope(Dispatchers.Default).launch {
            try {
                // Harmonic 2-tone chime (587Hz -> 880Hz)
                playSynthesizedTone(frequencies = doubleArrayOf(587.33, 880.0), durationMs = 180)
            } catch (e: Exception) {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 200)
            }
        }
    }

    fun playWrong() {
        if (!isSoundEnabled) return
        CoroutineScope(Dispatchers.Default).launch {
            try {
                // Low buzzer (220Hz -> 180Hz)
                playSynthesizedTone(frequencies = doubleArrayOf(220.0, 180.0), durationMs = 250)
            } catch (e: Exception) {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_NACK, 250)
            }
        }
    }

    fun playTimerTick() {
        if (!isSoundEnabled) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_PROMPT, 30)
        } catch (e: Exception) {
            Log.e(TAG, "Error playing tick", e)
        }
    }

    fun playLifeline() {
        if (!isSoundEnabled) return
        CoroutineScope(Dispatchers.Default).launch {
            try {
                playSynthesizedTone(frequencies = doubleArrayOf(440.0, 659.25), durationMs = 150)
            } catch (e: Exception) {
                toneGenerator?.startTone(ToneGenerator.TONE_CDMA_KEYPAD_VOLUME_KEY_LITE, 100)
            }
        }
    }

    fun playCelebration() {
        if (!isSoundEnabled) return
        CoroutineScope(Dispatchers.Default).launch {
            try {
                // 4-note celebration fanfare
                playSynthesizedTone(frequencies = doubleArrayOf(523.25, 659.25, 783.99, 1046.50), durationMs = 400)
            } catch (e: Exception) {
                toneGenerator?.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 400)
            }
        }
    }

    private fun playSynthesizedTone(frequencies: DoubleArray, durationMs: Int) {
        val sampleRate = 44100
        val totalSamples = (sampleRate * (durationMs / 1000.0)).toInt()
        val generatedSnd = ByteArray(2 * totalSamples)
        val samplesPerTone = totalSamples / frequencies.size

        var sampleIndex = 0
        for (freq in frequencies) {
            for (i in 0 until samplesPerTone) {
                val dVal = i.toDouble() / sampleRate
                val envelope = sin(Math.PI * i / samplesPerTone)
                val sample = (sin(2.0 * Math.PI * freq * dVal) * envelope * 32767).toInt().toShort()

                generatedSnd[sampleIndex++] = (sample.toInt() and 0x00ff).toByte()
                generatedSnd[sampleIndex++] = (sample.toInt() and 0xff00 ushr 8).toByte()
            }
        }

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        val audioFormat = AudioFormat.Builder()
            .setSampleRate(sampleRate)
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
            .build()

        val audioTrack = AudioTrack(
            audioAttributes,
            audioFormat,
            generatedSnd.size,
            AudioTrack.MODE_STATIC,
            AudioManager.AUDIO_SESSION_ID_GENERATE
        )

        audioTrack.write(generatedSnd, 0, generatedSnd.size)
        audioTrack.play()
    }
}
