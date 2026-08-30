package com.example.data.remote

import android.util.Log
import com.example.model.AppConfig
import com.example.model.LiveAnnouncement
import com.example.model.Mission
import com.example.model.QuizCategory
import com.example.model.QuizQuestion
import com.example.model.Tournament
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreManager {

    private val firestore: FirebaseFirestore? by lazy {
        try {
            FirebaseFirestore.getInstance()
        } catch (e: Exception) {
            Log.e("FirestoreManager", "Firebase not initialized: ${e.message}")
            null
        }
    }

    // Real-time listener for App Config
    fun observeAppConfig(): Flow<AppConfig> = callbackFlow {
        val fs = firestore
        if (fs == null) {
            trySend(InitialData.defaultAppConfig)
            awaitClose { }
            return@callbackFlow
        }

        val listener = fs.collection("system_config").document("app_config")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w("FirestoreManager", "Config listen failed", error)
                    trySend(InitialData.defaultAppConfig)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val config = AppConfig(
                        minSupportedVersionCode = snapshot.getLong("minSupportedVersionCode")?.toInt() ?: 1,
                        latestVersionCode = snapshot.getLong("latestVersionCode")?.toInt() ?: 1,
                        latestVersionName = snapshot.getString("latestVersionName") ?: "1.0",
                        forceUpdateUrl = snapshot.getString("forceUpdateUrl") ?: InitialData.defaultAppConfig.forceUpdateUrl,
                        updateNotesBn = snapshot.getString("updateNotesBn") ?: InitialData.defaultAppConfig.updateNotesBn,
                        isMaintenanceMode = snapshot.getBoolean("isMaintenanceMode") ?: false,
                        maintenanceMessageBn = snapshot.getString("maintenanceMessageBn") ?: InitialData.defaultAppConfig.maintenanceMessageBn,
                        correctXpMultiplier = snapshot.getLong("correctXpMultiplier")?.toInt() ?: 10,
                        correctCoinReward = snapshot.getLong("correctCoinReward")?.toInt() ?: 5,
                        dailyLoginRewardCoins = snapshot.getLong("dailyLoginRewardCoins")?.toInt() ?: 50,
                        dailyLoginRewardXp = snapshot.getLong("dailyLoginRewardXp")?.toInt() ?: 100,
                        referralRewardCoins = snapshot.getLong("referralRewardCoins")?.toInt() ?: 100,
                        refereeRewardCoins = snapshot.getLong("refereeRewardCoins")?.toInt() ?: 50,
                        adFrequencyMinutes = snapshot.getLong("adFrequencyMinutes")?.toInt() ?: 5,
                        isBannerAdEnabled = snapshot.getBoolean("isBannerAdEnabled") ?: true,
                        isInterstitialAdEnabled = snapshot.getBoolean("isInterstitialAdEnabled") ?: true,
                        announcementBannerBn = snapshot.getString("announcementBannerBn") ?: InitialData.defaultAppConfig.announcementBannerBn,
                        announcementBannerActive = snapshot.getBoolean("announcementBannerActive") ?: true
                    )
                    trySend(config)
                } else {
                    trySend(InitialData.defaultAppConfig)
                }
            }

        awaitClose { listener.remove() }
    }

    // Real-time listener for Announcements
    fun observeAnnouncements(): Flow<List<LiveAnnouncement>> = callbackFlow {
        val fs = firestore
        if (fs == null) {
            trySend(InitialData.defaultAnnouncements)
            awaitClose { }
            return@callbackFlow
        }

        val listener = fs.collection("announcements")
            .whereEqualTo("isActive", true)
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.w("FirestoreManager", "Announcements listen failed", error)
                    trySend(InitialData.defaultAnnouncements)
                    return@addSnapshotListener
                }

                if (snapshots != null && !snapshots.isEmpty) {
                    val list = snapshots.documents.mapNotNull { doc ->
                        LiveAnnouncement(
                            id = doc.id,
                            titleBn = doc.getString("titleBn") ?: "",
                            messageBn = doc.getString("messageBn") ?: "",
                            type = doc.getString("type") ?: "event",
                            timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis(),
                            actionUrl = doc.getString("actionUrl") ?: "",
                            actionLabelBn = doc.getString("actionLabelBn") ?: "দেখুন",
                            isPinned = doc.getBoolean("isPinned") ?: false,
                            isActive = doc.getBoolean("isActive") ?: true
                        )
                    }.sortedByDescending { it.timestamp }
                    trySend(list)
                } else {
                    trySend(InitialData.defaultAnnouncements)
                }
            }

        awaitClose { listener.remove() }
    }

    // Real-time listener for Tournaments
    fun observeTournaments(): Flow<List<Tournament>> = callbackFlow {
        val fs = firestore
        if (fs == null) {
            trySend(InitialData.defaultTournaments)
            awaitClose { }
            return@callbackFlow
        }

        val listener = fs.collection("tournaments")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    trySend(InitialData.defaultTournaments)
                    return@addSnapshotListener
                }

                if (snapshots != null && !snapshots.isEmpty) {
                    val list = snapshots.documents.mapNotNull { doc ->
                        Tournament(
                            id = doc.id,
                            titleBn = doc.getString("titleBn") ?: "",
                            descriptionBn = doc.getString("descriptionBn") ?: "",
                            categoryId = doc.getString("categoryId") ?: "general",
                            entryFeeCoins = doc.getLong("entryFeeCoins")?.toInt() ?: 20,
                            prizePoolCoins = doc.getLong("prizePoolCoins")?.toInt() ?: 5000,
                            totalParticipants = doc.getLong("totalParticipants")?.toInt() ?: 100,
                            startTimeEpoch = doc.getLong("startTimeEpoch") ?: System.currentTimeMillis(),
                            endTimeEpoch = doc.getLong("endTimeEpoch") ?: (System.currentTimeMillis() + 86400000),
                            status = doc.getString("status") ?: "active",
                            questionsCount = doc.getLong("questionsCount")?.toInt() ?: 10,
                            rulesBn = doc.getString("rulesBn") ?: ""
                        )
                    }
                    trySend(list)
                } else {
                    trySend(InitialData.defaultTournaments)
                }
            }

        awaitClose { listener.remove() }
    }

    // Admin updates: push App Config
    suspend fun saveAppConfig(config: AppConfig): Boolean {
        return try {
            firestore?.collection("system_config")?.document("app_config")
                ?.set(config, SetOptions.merge())?.await()
            true
        } catch (e: Exception) {
            Log.e("FirestoreManager", "Failed to save config: ${e.message}")
            false
        }
    }

    // Admin updates: publish Announcement
    suspend fun publishAnnouncement(announcement: LiveAnnouncement): Boolean {
        return try {
            val docRef = if (announcement.id.isEmpty()) {
                firestore?.collection("announcements")?.document()
            } else {
                firestore?.collection("announcements")?.document(announcement.id)
            }
            docRef?.set(announcement.copy(id = docRef.id))?.await()
            true
        } catch (e: Exception) {
            Log.e("FirestoreManager", "Failed to publish announcement: ${e.message}")
            false
        }
    }

    // Admin updates: create/update Question
    suspend fun saveQuestion(question: QuizQuestion): Boolean {
        return try {
            val docRef = if (question.id.isEmpty()) {
                firestore?.collection("questions")?.document()
            } else {
                firestore?.collection("questions")?.document(question.id)
            }
            docRef?.set(question.copy(id = docRef.id))?.await()
            true
        } catch (e: Exception) {
            Log.e("FirestoreManager", "Failed to save question: ${e.message}")
            false
        }
    }

    // Admin updates: create Tournament
    suspend fun saveTournament(tournament: Tournament): Boolean {
        return try {
            val docRef = if (tournament.id.isEmpty()) {
                firestore?.collection("tournaments")?.document()
            } else {
                firestore?.collection("tournaments")?.document(tournament.id)
            }
            docRef?.set(tournament.copy(id = docRef.id))?.await()
            true
        } catch (e: Exception) {
            Log.e("FirestoreManager", "Failed to save tournament: ${e.message}")
            false
        }
    }
}
