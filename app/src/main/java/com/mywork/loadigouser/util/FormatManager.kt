package com.mywork.loadigouser.util

import android.util.Log
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

interface FormatManagerInterface {
    fun formatCurrency(currencyCode: String = Currency.getInstance(Locale.getDefault()).currencyCode, number: Double): String {
        val numberFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
        numberFormatter.maximumFractionDigits = 2
        numberFormatter.currency = Currency.getInstance(currencyCode)
        return numberFormatter.format(number)
    }
    fun formatNumber(number: Double): String
    fun formatNumber(number: Int): String
    fun formatPercentage(number: Double): String
    fun formatDate(fromFormat: String, toFormat: String, date: String): String
}

class FormatManager(
    private var local: Locale = Locale.getDefault()
) : FormatManagerInterface {
    override fun formatCurrency(currencyCode: String, number: Double): String {
        val myLocale = Locale.getDefault()
        this.local = myLocale
        val numberFormatter = NumberFormat.getCurrencyInstance(this.local)
        numberFormatter.maximumFractionDigits = 2
        if (currencyCode == "جنيه مصرى" || currencyCode == "ج.م." || currencyCode == "جنيه مصري" ||
                currencyCode == "جم") {
            numberFormatter.currency = Currency.getInstance("EGP")
        } else {
            numberFormatter.currency = Currency.getInstance(currencyCode)
        }
        return numberFormatter.format(number)
    }

    override fun formatNumber(number: Double): String {
        val myLocale = Locale.getDefault()
        this.local = myLocale
        val numberFormatter = NumberFormat.getNumberInstance(local)
        numberFormatter.maximumFractionDigits = 2
        numberFormatter.minimumFractionDigits = 2
       return numberFormatter.format(number)
    }

    override fun formatNumber(number: Int): String {
        val numberFormatter = NumberFormat.getNumberInstance(local)
        return numberFormatter.format(number)
    }

    override fun formatPercentage(number: Double): String {
        val numberFormatter = NumberFormat.getPercentInstance(this.local)
        numberFormatter.minimumFractionDigits = 2
        return numberFormatter.format(number)
    }

    override fun formatDate(fromFormat: String, toFormat: String, date: String): String {
        val wantToParseDate = SimpleDateFormat(fromFormat, this.local).parse(date)
        return if (wantToParseDate != null) {
            SimpleDateFormat(toFormat, this.local).format(wantToParseDate)
        } else {
            ""
        }
    }
}

fun formatToEnglishNumber(x: Double): String = String.format(Locale.ENGLISH, "%.2f", x)
fun formatToArabicNumber(x: Double): String = String.format(Locale("ar", "EG"), "%.2f", x)
