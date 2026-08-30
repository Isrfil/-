package com.example.data.remote

import com.example.model.Achievement
import com.example.model.AppConfig
import com.example.model.LeaderboardUser
import com.example.model.LiveAnnouncement
import com.example.model.Mission
import com.example.model.QuizCategory
import com.example.model.QuizQuestion
import com.example.model.Tournament

object InitialData {

    val defaultAppConfig = AppConfig(
        minSupportedVersionCode = 1,
        latestVersionCode = 1,
        latestVersionName = "1.0",
        forceUpdateUrl = "https://play.google.com/store/apps/details?id=com.companyQuizapp",
        updateNotesBn = "✨ নতুন টুর্নামেন্ট ফিচার, দ্রুতগতির পারফরম্যান্স এবং বাগ ফিক্স করা হয়েছে!",
        isMaintenanceMode = false,
        maintenanceMessageBn = "আমাদের সার্ভার রক্ষণাবেক্ষণ চলছে। অনুগ্রহ করে কিছুক্ষণ পর আবার চেষ্টা করুন।",
        correctXpMultiplier = 10,
        correctCoinReward = 5,
        dailyLoginRewardCoins = 50,
        dailyLoginRewardXp = 100,
        referralRewardCoins = 100,
        refereeRewardCoins = 50,
        adFrequencyMinutes = 5,
        isBannerAdEnabled = true,
        isInterstitialAdEnabled = true,
        announcementBannerBn = "🔥 মেগা কুইজ টুর্নামেন্ট শুরু হয়েছে! এখনই অংশগ্রহণ করুন এবং ৫,০০০ কয়েন জিতুন!",
        announcementBannerActive = true
    )

    val defaultCategories = listOf(
        QuizCategory(
            id = "bangladesh",
            nameBn = "বাংলাদেশ ও মুক্তিযুদ্ধ",
            nameEn = "Bangladesh & Liberation War",
            descriptionBn = "আমাদের গৌরবময় ইতিহাস, মুক্তিযুদ্ধ ও ঐতিহ্য",
            iconName = "flag",
            primaryColorHex = "#059669",
            secondaryColorHex = "#10B981",
            questionCount = 15,
            displayOrder = 1,
            isFeatured = true
        ),
        QuizCategory(
            id = "general_knowledge",
            nameBn = "সাধারণ জ্ঞান ও বিশ্ব",
            nameEn = "General Knowledge & World",
            descriptionBn = "দেশ-বিদেশের অজানা তথ্য ও কারেন্ট অ্যাফেয়ার্স",
            iconName = "globe",
            primaryColorHex = "#4F46E5",
            secondaryColorHex = "#6366F1",
            questionCount = 15,
            displayOrder = 2,
            isFeatured = true
        ),
        QuizCategory(
            id = "literature",
            nameBn = "সাহিত্য ও সংস্কৃতি",
            nameEn = "Literature & Culture",
            descriptionBn = "রবীন্দ্রনাথ, নজরুল এবং সমৃদ্ধ বাংলা সাহিত্য",
            iconName = "book",
            primaryColorHex = "#D97706",
            secondaryColorHex = "#F59E0B",
            questionCount = 12,
            displayOrder = 3,
            isFeatured = false
        ),
        QuizCategory(
            id = "science",
            nameBn = "বিজ্ঞান ও প্রযুক্তি",
            nameEn = "Science & Technology",
            descriptionBn = "মহাকাশ, কম্পিউটার, আবিষ্কার ও আধুনিক প্রযুক্তি",
            iconName = "science",
            primaryColorHex = "#0284C7",
            secondaryColorHex = "#38BDF8",
            questionCount = 14,
            displayOrder = 4,
            isFeatured = true
        ),
        QuizCategory(
            id = "sports",
            nameBn = "খেলাধুলা ও ক্রিকেট",
            nameEn = "Sports & Cricket",
            descriptionBn = "ক্রিকেট বিশ্বকাপ, ফুটবল ও আন্তর্জাতিক ক্রীড়া",
            iconName = "sports",
            primaryColorHex = "#E11D48",
            secondaryColorHex = "#FB7185",
            questionCount = 12,
            displayOrder = 5,
            isFeatured = false
        ),
        QuizCategory(
            id = "islamic",
            nameBn = "ইসলামিক স্টাডিজ",
            nameEn = "Islamic Studies",
            descriptionBn = "পবিত্র কুরআন, হাদিস ও ইসলামের সোনালী ইতিহাস",
            iconName = "mosque",
            primaryColorHex = "#0D9488",
            secondaryColorHex = "#2DD4BF",
            questionCount = 12,
            displayOrder = 6,
            isFeatured = false
        ),
        QuizCategory(
            id = "math_puzzle",
            nameBn = "গণিত ও বুদ্ধির ধাঁধা",
            nameEn = "Math & Brain Puzzles",
            descriptionBn = "মজার লজিক, গণিত এবং আইকিউ চ্যালেঞ্জ",
            iconName = "calculate",
            primaryColorHex = "#9333EA",
            secondaryColorHex = "#C084FC",
            questionCount = 10,
            displayOrder = 7,
            isFeatured = false
        )
    )

    val defaultQuestions = listOf(
        // Bangladesh
        QuizQuestion(
            id = "bd_1",
            categoryId = "bangladesh",
            questionBn = "বাংলাদেশের স্বাধীনতার ঘোষণাপত্র কবে আনুষ্ঠানিকভাবে জারি করা হয়?",
            questionEn = "When was the Proclamation of Independence of Bangladesh officially declared?",
            optionsBn = listOf("১০ এপ্রিল ১৯৭১", "২৬ মার্চ ১৯৭১", "১৭ এপ্রিল ১৯৭১", "১৬ ডিসেম্বর ১৯৭১"),
            optionsEn = listOf("10 April 1971", "26 March 1971", "17 April 1971", "16 December 1971"),
            correctOptionIndex = 0,
            explanationBn = "১০ এপ্রিল ১৯৭১ সালে মুজিবনগর সরকার আনুষ্ঠানিকভাবে স্বাধীনতার ঘোষণাপত্র গ্রহণ ও জারি করে।",
            explanationEn = "The Mujibnagar Government adopted and officially proclaimed independence on 10 April 1971.",
            difficulty = "medium",
            points = 10
        ),
        QuizQuestion(
            id = "bd_2",
            categoryId = "bangladesh",
            questionBn = "মুক্তিযুদ্ধকালে সমগ্র বাংলাদেশকে কয়টি সেক্টরে বিভক্ত করা হয়েছিল?",
            questionEn = "Into how many sectors was Bangladesh divided during the Liberation War?",
            optionsBn = listOf("৮টি", "১১টি", "৯টি", "১২টি"),
            optionsEn = listOf("8 sectors", "11 sectors", "9 sectors", "12 sectors"),
            correctOptionIndex = 1,
            explanationBn = "১৯৭১ সালের মুক্তিযুদ্ধে যুদ্ধ পরিচালনার সুবিধার্থে বাংলাদেশকে ১১টি সেক্টরে ভাগ করা হয়।",
            explanationEn = "Bangladesh was divided into 11 military sectors during the 1971 Liberation War.",
            difficulty = "easy",
            points = 10
        ),
        QuizQuestion(
            id = "bd_3",
            categoryId = "bangladesh",
            questionBn = "বাংলাদেশের জাতীয় স্মৃতিসৌধের স্থপতি কে?",
            questionEn = "Who is the architect of the National Martyrs' Memorial of Bangladesh?",
            optionsBn = listOf("মাইনুল হোসেন", "হামিদুর রহমান", "মাজহারুল ইসলাম", "তানভীর কবির"),
            optionsEn = listOf("Syed Mainul Hossain", "Hamidur Rahman", "Muzharul Islam", "Tanveer Kabir"),
            correctOptionIndex = 0,
            explanationBn = "সাভারে অবস্থিত জাতীয় স্মৃতিসৌধের প্রধান স্থপতি সৈয়দ মাইনুল হোসেন।",
            explanationEn = "Syed Mainul Hossain is the architect of the National Martyrs' Memorial located at Savar.",
            difficulty = "easy",
            points = 10
        ),
        QuizQuestion(
            id = "bd_4",
            categoryId = "bangladesh",
            questionBn = "পদ্মা সেতুর মোট দৈর্ঘ্য কত কিলোমিটার?",
            questionEn = "What is the total length of the Padma Bridge in kilometers?",
            optionsBn = listOf("৫.৮ কিমি", "৬.১৫ কিমি", "৬.৮ কিমি", "৭.২ কিমি"),
            optionsEn = listOf("5.8 km", "6.15 km", "6.8 km", "7.2 km"),
            correctOptionIndex = 1,
            explanationBn = "স্বপ্নের পদ্মা সেতুর মূল দৈর্ঘ্য ৬.১৫ কিলোমিটার এবং এতে ৪১টি স্প্যান রয়েছে।",
            explanationEn = "The main Padma Bridge has a length of 6.15 km with 41 spans.",
            difficulty = "easy",
            points = 10
        ),

        // General Knowledge
        QuizQuestion(
            id = "gk_1",
            categoryId = "general_knowledge",
            questionBn = "বিশ্বের দীর্ঘতম প্রাকৃতিক সমুদ্র সৈকত কোনটি?",
            questionEn = "Which is the longest natural sea beach in the world?",
            optionsBn = listOf("কক্সবাজার, বাংলাদেশ", "মায়ামি বিচ, যুক্তরাষ্ট্র", "কোপাকাবানা, ব্রাজিল", "বন্ডি বিচ, অস্ট্রেলিয়া"),
            optionsEn = listOf("Cox's Bazar, Bangladesh", "Miami Beach, USA", "Copacabana, Brazil", "Bondi Beach, Australia"),
            correctOptionIndex = 0,
            explanationBn = "বাংলাদেশের কক্সবাজার বিশ্বের দীর্ঘতম অবিচ্ছিন্ন প্রাকৃতিক বালুকাময় সমুদ্র সৈকত (প্রায় ১২০ কিমি)।",
            explanationEn = "Cox's Bazar in Bangladesh is the longest unbroken natural sand beach in the world (~120 km).",
            difficulty = "easy",
            points = 10
        ),
        QuizQuestion(
            id = "gk_2",
            categoryId = "general_knowledge",
            questionBn = "কোন মহাদেশে কোনো সক্রিয় আগ্নেয়গিরি নেই?",
            questionEn = "Which continent has no active volcanoes on its mainland?",
            optionsBn = listOf("আফ্রিকা", "ইউরোপ", "অস্ট্রেলিয়া", "দক্ষিণ আমেরিকা"),
            optionsEn = listOf("Africa", "Europe", "Australia", "South America"),
            correctOptionIndex = 2,
            explanationBn = "অস্ট্রেলিয়া মহাদেশের মূল ভূখণ্ডে কোনো সক্রিয় আগ্নেয়গিরি নেই।",
            explanationEn = "Australia's mainland is the only continent without active volcanoes.",
            difficulty = "hard",
            points = 15
        ),
        QuizQuestion(
            id = "gk_3",
            categoryId = "general_knowledge",
            questionBn = "সূর্যোদয়ের দেশ বলা হয় কোন দেশকে?",
            questionEn = "Which country is called the Land of the Rising Sun?",
            optionsBn = listOf("চীন", "জাপান", "নরওয়ে", "নিউজিল্যান্ড"),
            optionsEn = listOf("China", "Japan", "Norway", "New Zealand"),
            correctOptionIndex = 1,
            explanationBn = "জাপানকে পূর্ব গোলার্ধের অবস্থান ও নামের কারণে 'সূর্যোদয়ের দেশ' (Nippon) বলা হয়।",
            explanationEn = "Japan is known as Nippon or Nihon, meaning origin of the sun.",
            difficulty = "easy",
            points = 10
        ),

        // Literature
        QuizQuestion(
            id = "lit_1",
            categoryId = "literature",
            questionBn = "রবীন্দ্রনাথ ঠাকুর কোন কাব্যগ্রন্থের জন্য নোবেল পুরস্কার পেয়েছিলেন?",
            questionEn = "For which poetry collection did Rabindranath Tagore win the Nobel Prize?",
            optionsBn = listOf("সোনার তরী", "গীতাঞ্জলি", "বলাকা", "খেয়া"),
            optionsEn = listOf("Sonar Tori", "Gitanjali", "Balaka", "Kheya"),
            correctOptionIndex = 1,
            explanationBn = "১৯১৩ সালে গীতাঞ্জলি কাব্যগ্রন্থের ইংরেজি অনুবাদের জন্য তিনি সাহিত্যে নোবেল পান।",
            explanationEn = "He won the Nobel Prize in Literature in 1913 for Gitanjali (Song Offerings).",
            difficulty = "easy",
            points = 10
        ),
        QuizQuestion(
            id = "lit_2",
            categoryId = "literature",
            questionBn = "কাজী নজরুল ইসলামের প্রথম প্রকাশিত কবিতার নাম কী?",
            questionEn = "What is the name of Kazi Nazrul Islam's first published poem?",
            optionsBn = listOf("বিদ্রোহী", "মুক্তি", "প্রলয়োল্লাস", "কান্ডারী হুঁশিয়ার"),
            optionsEn = listOf("Bidrohi", "Mukti", "Proloyollas", "Kandari Hushiyar"),
            correctOptionIndex = 1,
            explanationBn = "১৯১৯ সালে বঙ্গীয় মুসলমান সাহিত্য পত্রিকায় নজরুলের প্রথম কবিতা 'মুক্তি' প্রকাশিত হয়।",
            explanationEn = "His first published poem 'Mukti' was published in 1919 in Bangiya Musalman Sahitya Samiti.",
            difficulty = "medium",
            points = 10
        ),

        // Science & Tech
        QuizQuestion(
            id = "sci_1",
            categoryId = "science",
            questionBn = "কৃত্রিম বুদ্ধিমত্তার (AI) জনক হিসেবে কাকে বিবেচনা করা হয়?",
            questionEn = "Who is widely recognized as the father of Artificial Intelligence?",
            optionsBn = listOf("অ্যালান টুরিং", "জন ম্যাকার্থি", "বিল গেটস", "টিম বার্নার্স-লি"),
            optionsEn = listOf("Alan Turing", "John McCarthy", "Bill Gates", "Tim Berners-Lee"),
            correctOptionIndex = 1,
            explanationBn = "জন ম্যাকার্থি ১৯৫৬ সালে 'Artificial Intelligence' শব্দটি প্রবর্তন করেন এবং এআই গবেষণার ভিত্তি গড়েন।",
            explanationEn = "John McCarthy coined the term Artificial Intelligence in 1956.",
            difficulty = "medium",
            points = 10
        ),
        QuizQuestion(
            id = "sci_2",
            categoryId = "science",
            questionBn = "মানবদেহের সবচেয়ে বড় অঙ্গ কোনটি?",
            questionEn = "What is the largest organ in the human body?",
            optionsBn = listOf("যকৃৎ (Liver)", "ত্বক (Skin)", "মস্তিষ্ক (Brain)", "ফুসফুস (Lungs)"),
            optionsEn = listOf("Liver", "Skin", "Brain", "Lungs"),
            correctOptionIndex = 1,
            explanationBn = "মানবদেহের ত্বক (Skin) বাহ্যিক ও অভ্যন্তরীণ আয়তন মিলিয়ে সর্ববৃহৎ অঙ্গ।",
            explanationEn = "The skin is the largest organ of the human body.",
            difficulty = "easy",
            points = 10
        ),

        // Sports
        QuizQuestion(
            id = "sp_1",
            categoryId = "sports",
            questionBn = "বাংলাদেশ ক্রিকেট দল টেস্ট স্ট্যাটাস লাভ করে কত সালে?",
            questionEn = "In which year did the Bangladesh cricket team receive official Test status?",
            optionsBn = listOf("১৯৯৭ সালে", "১৯৯৯ সালে", "২০০০ সালে", "২০০২ সালে"),
            optionsEn = listOf("In 1997", "In 1999", "In 2000", "In 2002"),
            correctOptionIndex = 2,
            explanationBn = "২৬ জুন ২০০০ সালে আইসিসি বাংলাদেশকে দশম টেস্ট খেলুড়ে দেশ হিসেবে স্বীকৃতি দেয়।",
            explanationEn = "ICC granted full Test membership to Bangladesh on June 26, 2000.",
            difficulty = "medium",
            points = 10
        ),
        QuizQuestion(
            id = "sp_2",
            categoryId = "sports",
            questionBn = "২০২২ ফিফা ফুটবল বিশ্বকাপ বিজয়ী দেশ কোনটি?",
            questionEn = "Which country won the 2022 FIFA World Cup in Qatar?",
            optionsBn = listOf("ফ্রান্স", "ব্রাজিল", "আর্জেন্টিনা", "ক্রোয়েশিয়া"),
            optionsEn = listOf("France", "Brazil", "Argentina", "Croatia"),
            correctOptionIndex = 2,
            explanationBn = "কাতারে অনুষ্ঠিত ২০২২ ফিফা বিশ্বকাপে লিওনেল মেসির নেতৃত্বে আর্জেন্টিনা শিরোপা জেতে।",
            explanationEn = "Argentina led by Lionel Messi won the 2022 FIFA World Cup in Qatar.",
            difficulty = "easy",
            points = 10
        ),

        // Islamic Studies
        QuizQuestion(
            id = "isl_1",
            categoryId = "islamic",
            questionBn = "পবিত্র কুরআনে মোট কয়টি সূরা রয়েছে?",
            questionEn = "How many Surahs are there in the Holy Quran?",
            optionsBn = listOf("১১০টি", "১১৪টি", "১২০টি", "১১৬টি"),
            optionsEn = listOf("110 Surahs", "114 Surahs", "120 Surahs", "116 Surahs"),
            correctOptionIndex = 1,
            explanationBn = "পবিত্র আল-কুরআনে মোট ১১৪টি সূরা ও ৩০টি পারা রয়েছে।",
            explanationEn = "There are 114 Surahs and 30 Juz in the Holy Quran.",
            difficulty = "easy",
            points = 10
        ),

        // Math & Logic
        QuizQuestion(
            id = "math_1",
            categoryId = "math_puzzle",
            questionBn = "ধারার পরবর্তী সংখ্যাটি কত? ২, ৬, ১২, ২০, ৩০, ?",
            questionEn = "What is the next number in the sequence? 2, 6, 12, 20, 30, ?",
            optionsBn = listOf("৪০", "৪২", "৪৪", "৪৮"),
            optionsEn = listOf("40", "42", "44", "48"),
            correctOptionIndex = 1,
            explanationBn = "পার্থক্য বাড়ছে: +৪, +৬, +৮, +১০, +১২। সুতরাং ৩০ + ১২ = ৪২।",
            explanationEn = "Pattern adds consecutive even numbers: +4, +6, +8, +10, +12. 30 + 12 = 42.",
            difficulty = "medium",
            points = 15
        )
    )

    val defaultAnnouncements = listOf(
        LiveAnnouncement(
            id = "ann_1",
            titleBn = "🎉 মেগা উইকলি কুইজ টুর্নামেন্ট লাইভ!",
            messageBn = "চলতি সপ্তাহের বিশাল প্রাইজ পুল ৫,০০০ কয়েন এবং স্পেশাল গোল্ডেন ট্রফি! এখনই টুর্নামেন্ট ট্যাবে যান এবং অংশ নিন।",
            type = "tournament",
            timestamp = System.currentTimeMillis() - 1000 * 60 * 30,
            isPinned = true
        ),
        LiveAnnouncement(
            id = "ann_2",
            titleBn = "🎁 ডেইলি রিওয়ার্ড ইভেন্ট দ্বিগুণ কয়েন!",
            messageBn = "আজকের সব কুইজে সঠিক উত্তরের জন্য পাবেন দ্বিগুণ এক্সপি ও কয়েন রিওয়ার্ড!",
            type = "reward",
            timestamp = System.currentTimeMillis() - 1000 * 60 * 120,
            isPinned = false
        ),
        LiveAnnouncement(
            id = "ann_3",
            titleBn = "📚 বিজ্ঞান ও প্রযুক্তি ক্যাটাগরিতে নতুন প্রশ্ন যোগ হয়েছে",
            messageBn = "সর্বশেষ এআই এবং মহাকাশ বিজ্ঞান সম্পর্কিত ১০০+ নতুন কুইজ এখন উন্মুক্ত।",
            type = "info",
            timestamp = System.currentTimeMillis() - 1000 * 60 * 360,
            isPinned = false
        )
    )

    val defaultTournaments = listOf(
        Tournament(
            id = "tour_1",
            titleBn = "🏆 বাংলাদেশ প্রিমিয়ার কুইজ লীগ",
            descriptionBn = "বাংলাদেশ ও বিশ্ব ইতিহাস নিয়ে সেরা মেধাবীদের লড়াই। সর্বোচ্চ স্কোরার পাবে ১০০০+ কয়েন!",
            categoryId = "bangladesh",
            entryFeeCoins = 20,
            prizePoolCoins = 5000,
            totalParticipants = 328,
            startTimeEpoch = System.currentTimeMillis() - 3600000,
            endTimeEpoch = System.currentTimeMillis() + 86400000 * 2,
            status = "active",
            questionsCount = 10,
            rulesBn = "১০টি প্রশ্ন। প্রতিটি প্রশ্নের জন্য ১৫ সেকেন্ড। দ্রুত সঠিক উত্তর দিলে স্পিড বোনাস পয়েন্ট।"
        ),
        Tournament(
            id = "tour_2",
            titleBn = "⚡ সুপার সায়েন্স & টেক চ্যালেঞ্জ",
            descriptionBn = "বিজ্ঞান, আবিষ্কার এবং কৃত্রিম বুদ্ধিমত্তা বিষয়ক মেগা টুর্নামেন্ট।",
            categoryId = "science",
            entryFeeCoins = 30,
            prizePoolCoins = 8000,
            totalParticipants = 215,
            startTimeEpoch = System.currentTimeMillis() - 7200000,
            endTimeEpoch = System.currentTimeMillis() + 86400000 * 4,
            status = "active",
            questionsCount = 15,
            rulesBn = "১৫টি প্রশ্ন। প্রতি সঠিক উত্তরে ২৫ পয়েন্ট। টাই হলে সময়ের ভিত্তিতে র‍্যাঙ্কিং নির্ধারিত হবে।"
        ),
        Tournament(
            id = "tour_3",
            titleBn = "🌟 আন্তর্জাতিক সাধারণ জ্ঞান গ্র্যান্ড ফিনালে",
            descriptionBn = "বিশ্ব রাজনীতি, ভূগোল ও সাম্প্রতিক ঘটনাপ্রবাহ নিয়ে টুর্নামেন্ট।",
            categoryId = "general_knowledge",
            entryFeeCoins = 50,
            prizePoolCoins = 15000,
            totalParticipants = 680,
            startTimeEpoch = System.currentTimeMillis() + 86400000,
            endTimeEpoch = System.currentTimeMillis() + 86400000 * 5,
            status = "upcoming",
            questionsCount = 20,
            rulesBn = "আসন্ন ইভেন্ট! নোটিফিকেশন অন রাখুন এবং কয়েন সংগ্রহ করে রাখুন।"
        )
    )

    val defaultMissions = listOf(
        Mission(
            id = "m_1",
            titleBn = "৩টি কুইজ সম্পন্ন করুন",
            descriptionBn = "যেকোনো ক্যাটাগরিতে ৩টি পূর্ণাঙ্গ কুইজ খেলুন",
            type = "daily",
            targetCount = 3,
            currentProgress = 1,
            rewardCoins = 30,
            rewardXp = 50
        ),
        Mission(
            id = "m_2",
            titleBn = "৮০% সঠিক উত্তরের স্কোর",
            descriptionBn = "একটি কুইজে কমপক্ষে ৮০% নির্ভুল স্কোর অর্জন করুন",
            type = "daily",
            targetCount = 1,
            currentProgress = 1,
            rewardCoins = 40,
            rewardXp = 75,
            isCompleted = true,
            isClaimed = false
        ),
        Mission(
            id = "m_3",
            titleBn = "বন্ধুকে ইনভাইট করুন",
            descriptionBn = "আপনার রেফারেল কোড দিয়ে ১ জন বন্ধুকে অ্যাপ শেয়ার করুন",
            type = "daily",
            targetCount = 1,
            currentProgress = 0,
            rewardCoins = 100,
            rewardXp = 150
        ),
        Mission(
            id = "m_4",
            titleBn = "সাপ্তাহিক টুর্নামেন্ট যোদ্ধা",
            descriptionBn = "চলতি সপ্তাহে কমপক্ষে ২টি টুর্নামেন্টে অংশ নিন",
            type = "weekly",
            targetCount = 2,
            currentProgress = 1,
            rewardCoins = 150,
            rewardXp = 300
        ),
        Mission(
            id = "m_5",
            titleBn = "বিজ্ঞান বিষয়ের মাস্টার",
            descriptionBn = "বিজ্ঞান ক্যাটাগরিতে ২০টি প্রশ্নের সঠিক উত্তর দিন",
            type = "weekly",
            targetCount = 20,
            currentProgress = 12,
            rewardCoins = 200,
            rewardXp = 400
        )
    )

    val defaultAchievements = listOf(
        Achievement(
            id = "ach_first_win",
            titleBn = "প্রথম বিজয়ের স্বাদ",
            descriptionBn = "আপনার প্রথম কুইজ বিজয়ী হন",
            iconName = "emoji_events",
            requiredCount = 1,
            currentCount = 1,
            isUnlocked = true,
            rewardCoins = 50,
            unlockedDate = "আজ"
        ),
        Achievement(
            id = "ach_streak_3",
            titleBn = "ধারাবাহিক জ্ঞানসাধক",
            descriptionBn = "টানা ৩ দিন কুইজ অ্যাপে লগইন ও খেলুন",
            iconName = "local_fire_department",
            requiredCount = 3,
            currentCount = 3,
            isUnlocked = true,
            rewardCoins = 100,
            unlockedDate = "গতকাল"
        ),
        Achievement(
            id = "ach_quiz_50",
            titleBn = "কুইজ বিশারদ",
            descriptionBn = "৫০টি প্রশ্নের সঠিক উত্তর প্রদান করুন",
            iconName = "military_tech",
            requiredCount = 50,
            currentCount = 48,
            isUnlocked = false,
            rewardCoins = 250
        ),
        Achievement(
            id = "ach_tournament_champ",
            titleBn = "টুর্নামেন্ট চ্যাম্পিয়ন",
            descriptionBn = "যেকোনো লাইভ টুর্নামেন্টের টপ ৩ এ স্থান পান",
            iconName = "workspace_premium",
            requiredCount = 1,
            currentCount = 0,
            isUnlocked = false,
            rewardCoins = 500
        ),
        Achievement(
            id = "ach_inviter",
            titleBn = "জ্ঞান প্রচারক",
            descriptionBn = "৫ জন বন্ধুকে রেফারেল লিঙ্কের মাধ্যমে যুক্ত করুন",
            iconName = "group_add",
            requiredCount = 5,
            currentCount = 2,
            isUnlocked = false,
            rewardCoins = 300
        )
    )

    val defaultLeaderboard = listOf(
        LeaderboardUser(rank = 1, name = "তানভীর আহমেদ", score = 14500, xp = 2450, coins = 1250, avatarInitial = "ত"),
        LeaderboardUser(rank = 2, name = "নুসরাত জাহান", score = 13200, xp = 2100, coins = 980, avatarInitial = "ন"),
        LeaderboardUser(rank = 3, name = "সাকিব আল হাসান", score = 11800, xp = 1850, coins = 850, avatarInitial = "স"),
        LeaderboardUser(rank = 4, name = "ইশরাফিল মিয়া", score = 9450, xp = 1450, coins = 620, avatarInitial = "ই", isCurrentUser = true),
        LeaderboardUser(rank = 5, name = "ফারহানা ইসলাম", score = 8900, xp = 1300, coins = 540, avatarInitial = "ফ"),
        LeaderboardUser(rank = 6, name = "রাকিবুল হাসান", score = 7600, xp = 1100, coins = 490, avatarInitial = "র"),
        LeaderboardUser(rank = 7, name = "মেহেদী হাসান", score = 6500, xp = 950, coins = 410, avatarInitial = "ম"),
        LeaderboardUser(rank = 8, name = "আরিফা সুলতানা", score = 5800, xp = 800, coins = 350, avatarInitial = "আ")
    )
}
