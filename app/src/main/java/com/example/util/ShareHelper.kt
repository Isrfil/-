package com.example.util

import android.content.Context
import android.content.Intent
import com.example.model.Achievement
import com.example.model.QuizResult
import com.example.model.Tournament

object ShareHelper {

    const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=com.companyQuizapp"
    const val PREVIEW_APP_URL = "https://ais-pre-ozbzx5hxnzaqumt65knsht-863632656523.asia-southeast1.run.app"

    fun getReferralLink(referralCode: String): String {
        return "$PLAY_STORE_URL&referrer=ref_$referralCode"
    }

    fun shareApp(context: Context, referralCode: String = "QUIZ789") {
        val appLink = getReferralLink(referralCode)
        val text = "🏆 সেরা বাংলা কুইজ অ্যাপ Bangla Quiz খেলুন এবং জ্ঞান বৃদ্ধির সাথে সাথে জিতে নিন আকর্ষণীয় পুরস্কার ও কয়েন!\n\n" +
                "আমার রেফারেল কোড: $referralCode (ব্যবহার করলেই পাবেন ৫০ বোনাস কয়েন!)\n\n" +
                "এখনই ডাউনলোড করুন:\n$appLink\n" +
                "(ওয়েব প্রিভিউ লিঙ্ক: $PREVIEW_APP_URL)"

        sendShareIntent(context, "Bangla Quiz অ্যাপ শেয়ার করুন", text)
    }

    fun inviteFriends(context: Context, referralCode: String) {
        val appLink = getReferralLink(referralCode)
        val text = "👋 বন্ধু, আমি Bangla Quiz অ্যাপে কুইজ খেলছি এবং জ্ঞানচর্চার সাথে কয়েন জিতছি!\n\n" +
                "তুমি কি আমাকে চ্যালেঞ্জ করতে চাও? জয়েন করার সময় রেফারেল কোড $referralCode ব্যবহার করে ফ্রিতে ৫০ কয়েন বোনাস নাও!\n\n" +
                "ডাউনলোড লিঙ্ক:\n$appLink"

        sendShareIntent(context, "বন্ধুদের আমন্ত্রণ জানান", text)
    }

    fun shareQuizResult(context: Context, result: QuizResult, categoryName: String) {
        val text = "🎯 আমি Bangla Quiz অ্যাপে \"$categoryName\" কুইজে ${result.totalQuestions} এর মধ্যে ${result.correctAnswers} পেয়েছি (${result.accuracyPercentage}% সঠিক)! 🏆 মোট স্কোর: ${result.score}!\n\n" +
                "তুমি কি আমার স্কোর ছাড়িয়ে যেতে পারবে? এখনই কুইজ খেলো:\n$PLAY_STORE_URL"

        sendShareIntent(context, "কুইজ ফলাফল শেয়ার করুন", text)
    }

    fun shareTournament(context: Context, tournament: Tournament) {
        val text = "⚔️ Bangla Quiz-এর লাইভ মেগা টুর্নামেন্ট \"${tournament.titleBn}\"-এ যোগ দিন!\n\n" +
                "💰 প্রাইজ পুল: ${tournament.prizePoolCoins} কয়েন\n" +
                "🎯 অংশগ্রহণ ফি: ${tournament.entryFeeCoins} কয়েন\n\n" +
                "এখনই অ্যাপ ওপেন করে অংশ নিন:\n$PLAY_STORE_URL"

        sendShareIntent(context, "টুর্নামেন্ট শেয়ার করুন", text)
    }

    fun shareAchievement(context: Context, achievement: Achievement) {
        val text = "🎖️ আমি Bangla Quiz অ্যাপে \"${achievement.titleBn}\" ব্যাজ আনলক করেছি!\n\n" +
                "\"${achievement.descriptionBn}\"\n\n" +
                "আপনিও কুইজ খেলে অর্জন আনলক করুন:\n$PLAY_STORE_URL"

        sendShareIntent(context, "অর্জন শেয়ার করুন", text)
    }

    private fun sendShareIntent(context: Context, chooserTitle: String, content: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Bangla Quiz - কুইজ বাংলা")
            putExtra(Intent.EXTRA_TEXT, content)
        }
        context.startActivity(Intent.createChooser(intent, chooserTitle))
    }
}
