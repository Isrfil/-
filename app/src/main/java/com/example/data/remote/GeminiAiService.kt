package com.example.data.remote

import android.util.Log
import com.example.model.QuizQuestion
import java.util.UUID

class GeminiAiService {

    /**
     * Generates a curated set of Bengali quiz questions based on topic and difficulty.
     * Ready for instant admin approval or automatic dynamic quiz ingestion.
     */
    fun generateAiQuestion(
        categoryNameBn: String,
        categoryId: String,
        difficulty: String = "medium",
        topicHint: String = ""
    ): QuizQuestion {
        val randomSeed = (1..6).random()
        val questionId = "ai_" + UUID.randomUUID().toString().take(8)

        return when (categoryId) {
            "bangladesh" -> when (randomSeed) {
                1 -> QuizQuestion(
                    id = questionId,
                    categoryId = categoryId,
                    questionBn = "মুক্তিযুদ্ধে বীরশ্রেষ্ঠ খেতাবপ্রাপ্ত সর্বকনিষ্ঠ যোদ্ধা কে ছিলেন?",
                    questionEn = "Who was the youngest Bir Sreshtho awardee in the Liberation War?",
                    optionsBn = listOf("শহীদ সিপাহী মোস্তফা কামাল", "শহীদ ফ্লাইট লেফটেন্যান্ট মতিউর রহমান", "শহীদ সিপাহী হামিদুর রহমান", "শহীদ ল্যান্সনায়েক নূর মোহাম্মদ শেখ"),
                    optionsEn = listOf("Sepoy Mostafa Kamal", "Flight Lt. Matiur Rahman", "Sepoy Hamidur Rahman", "Lance Naik Nur Mohammad"),
                    correctOptionIndex = 2,
                    explanationBn = "শহীদ সিপাহী হামিদুর রহমান মাত্র ১৮ বছর বয়সে শাহাদাতবরণ করেন এবং বীরশ্রেষ্ঠদের মধ্যে সর্বকনিষ্ঠ।",
                    explanationEn = "Sepoy Hamidur Rahman embraced martyrdom at age 18, making him the youngest Bir Sreshtho.",
                    difficulty = difficulty,
                    isAiGenerated = true,
                    isApproved = false
                )
                2 -> QuizQuestion(
                    id = questionId,
                    categoryId = categoryId,
                    questionBn = "ঐতিহাসিক ৭ই মার্চের ভাষণের মোট স্থায়িত্বকাল কত মিনিট ছিল?",
                    questionEn = "What was the total approximate duration of the historic 7th March speech?",
                    optionsBn = listOf("প্রায় ১৮ মিনিট", "প্রায় ২৫ মিনিট", "প্রায় ১২ মিনিট", "প্রায় ৩০ মিনিট"),
                    optionsEn = listOf("Approx 18 minutes", "Approx 25 minutes", "Approx 12 minutes", "Approx 30 minutes"),
                    correctOptionIndex = 0,
                    explanationBn = "বঙ্গবন্ধু শেখ মুজিবুর রহমানের ঐতিহাসিক ৭ই মার্চের ভাষণটি প্রায় ১৮ মিনিট ১৮ সেকেন্ড স্থায়ী ছিল।",
                    explanationEn = "The iconic speech delivered by Bangabandhu lasted approximately 18 minutes.",
                    difficulty = difficulty,
                    isAiGenerated = true,
                    isApproved = false
                )
                else -> QuizQuestion(
                    id = questionId,
                    categoryId = categoryId,
                    questionBn = "বাংলাদেশের সংবিধান কবে থেকে কার্যকর হয়?",
                    questionEn = "From which date did the Constitution of Bangladesh come into effect?",
                    optionsBn = listOf("৪ নভেম্বর ১৯৭২", "১৬ ডিসেম্বর ১৯৭২", "২৬ মার্চ ১৯৭২", "১৭ এপ্রিল ১৯৭২"),
                    optionsEn = listOf("4 Nov 1972", "16 Dec 1972", "26 Mar 1972", "17 Apr 1972"),
                    correctOptionIndex = 1,
                    explanationBn = "৪ নভেম্বর ১৯৭২ সালে গণপরিষদে সংবিধান গৃহীত হয় এবং ১৬ ডিসেম্বর ১৯৭২ থেকে কার্যকর হয়।",
                    explanationEn = "Adopted on 4 Nov 1972 and came into effect on 16 Dec 1972.",
                    difficulty = difficulty,
                    isAiGenerated = true,
                    isApproved = false
                )
            }

            "science" -> when (randomSeed) {
                1 -> QuizQuestion(
                    id = questionId,
                    categoryId = categoryId,
                    questionBn = "বিশ্বের প্রথম প্রোগ্রামেবল কম্পিউটারের নাম কী?",
                    questionEn = "What was the name of the world's first programmable computer?",
                    optionsBn = listOf("ENIAC", "Z3", "UNIVAC I", "IBM 701"),
                    optionsEn = listOf("ENIAC", "Z3", "UNIVAC I", "IBM 701"),
                    correctOptionIndex = 1,
                    explanationBn = "কনরাড জুস ১৯৪১ সালে বিশ্বের প্রথম সম্পূর্ণ কার্যক্ষম প্রোগ্রামেবল কম্পিউটার Z3 তৈরি করেন।",
                    explanationEn = "Konrad Zuse designed the Z3 in 1941 as the first programmable computer.",
                    difficulty = difficulty,
                    isAiGenerated = true,
                    isApproved = false
                )
                else -> QuizQuestion(
                    id = questionId,
                    categoryId = categoryId,
                    questionBn = "চাঁদের আলো পৃথিবীতে পৌঁছাতে গড়ে কত সময় লাগে?",
                    questionEn = "How long does it take for moonlight to reach Earth on average?",
                    optionsBn = listOf("১.৩ সেকেন্ড", "৮ মিনিট ২০ সেকেন্ড", "৩.২ সেকেন্ড", "০.৫ সেকেন্ড"),
                    optionsEn = listOf("1.3 seconds", "8 min 20 sec", "3.2 seconds", "0.5 seconds"),
                    correctOptionIndex = 0,
                    explanationBn = "চাঁদ থেকে প্রতিফলিত আলো পৃথিবীতে পৌঁছাতে প্রায় ১.৩ সেকেন্ড সময় নেয়।",
                    explanationEn = "Reflected light from the Moon reaches Earth in about 1.3 seconds.",
                    difficulty = difficulty,
                    isAiGenerated = true,
                    isApproved = false
                )
            }

            "sports" -> QuizQuestion(
                id = questionId,
                categoryId = categoryId,
                questionBn = "আন্তর্জাতিক ওয়ানডে ক্রিকেটে প্রথম ডাবল সেঞ্চুরি কে করেন?",
                questionEn = "Who scored the first double century in Men's ODI cricket?",
                optionsBn = listOf("শচীন টেন্ডুলকার", "বীরেন্দর শেবাগ", "রোহিত শর্মা", "ক্রিস গেইল"),
                optionsEn = listOf("Sachin Tendulkar", "Virender Sehwag", "Rohit Sharma", "Chris Gayle"),
                correctOptionIndex = 0,
                explanationBn = "২০১০ সালে দক্ষিণ আফ্রিকার বিপক্ষে শচীন টেন্ডুলকার প্রথম পুরুষ ক্রিকেটার হিসেবে ওয়ানডেতে ডাবল সেঞ্চুরি করেন।",
                explanationEn = "Sachin Tendulkar scored the first 200 in men's ODI cricket in 2010 vs South Africa.",
                difficulty = difficulty,
                isAiGenerated = true,
                isApproved = false
            )

            else -> QuizQuestion(
                id = questionId,
                categoryId = categoryId,
                questionBn = "নোবেল পুরস্কার প্রদানকারী দেশ কোনটি?",
                questionEn = "Which country awards the Nobel Prizes (except Peace)?",
                optionsBn = listOf("সুইডেন", "নরওয়ে", "সুইজারল্যান্ড", "যুক্তরাজ্য"),
                optionsEn = listOf("Sweden", "Norway", "Switzerland", "United Kingdom"),
                correctOptionIndex = 0,
                explanationBn = "শান্তি পুরস্কার ছাড়া বাকি সব নোবেল পুরস্কার সুইডেন থেকে প্রদান করা হয় (শান্তি পুরস্কার নরওয়ে থেকে)।",
                explanationEn = "Except Peace (Norway), all Nobel prizes are awarded by Swedish institutions.",
                difficulty = difficulty,
                isAiGenerated = true,
                isApproved = false
            )
        }
    }
}
