package com.example.util

object BengaliNumberFormatter {

    private val banglaDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')

    fun format(number: Number, isBengali: Boolean = true): String {
        return formatString(number.toString(), isBengali)
    }

    fun formatString(str: String, isBengali: Boolean = true): String {
        if (!isBengali) return str

        val builder = StringBuilder()
        for (char in str) {
            if (char in '0'..'9') {
                builder.append(banglaDigits[char - '0'])
            } else {
                builder.append(char)
            }
        }
        return builder.toString()
    }

    fun formatSeconds(seconds: Int, isBengali: Boolean = true): String {
        val mins = seconds / 60
        val secs = seconds % 60
        val str = String.format("%02d:%02d", mins, secs)
        return formatString(str, isBengali)
    }
}
