package com.mywork.loadigouser.util.extensions.strings

import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import com.mywork.loadigouser.MainApplication
import com.mywork.loadigouser.R
import com.mywork.loadigouser.util.FormatManager
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


fun String.formatToDouble(): Double? {
    return this.toEnglishNumbers()
        .replace("،", "")
        .replace(",", "")
        .replace("٫", "").toDoubleOrNull()
}

fun String.toArabicNumbers(): String {
    var result = this
    result = result.replace("0", "٠")
    result = result.replace("1", "١")
    result = result.replace("2", "٢")
    result = result.replace("3", "٣")
    result = result.replace("4", "٤")
    result = result.replace("5", "٥")
    result = result.replace("6", "٦")
    result = result.replace("7", "٧")
    result = result.replace("8", "٨")
    result = result.replace("9", "٩")
    return result
}

fun String.toEnglishNumbers(): String {
    var result = this
    result = result.replace("٠", "0")
    result = result.replace("١", "1")
    result = result.replace("٢", "2")
    result = result.replace("٣", "3")
    result = result.replace("٤", "4")
    result = result.replace("٥", "5")
    result = result.replace("٦", "6")
    result = result.replace("٧", "7")
    result = result.replace("٨", "8")
    result = result.replace("٩", "9")
    return result
}

fun String.isProbablyArabic(): Boolean {
    var i = 0
    while (i < this.length) {
        val c = this.codePointAt(i)
        if (c in 0x0600..0x06E0) return true
        i += Character.charCount(c)
    }
    return false
}

fun String.isAllArabic(): Boolean {
    var i = 0
    while (i < this.length) {
        val c = this.codePointAt(i)
        if ((c in 0x0600..0x06E0).not() && c != 0x0020) {
            return false
        }
        i += Character.charCount(c)
    }
    return true
}

fun String.formatToInt(): Int {
    return this.replace(",", "").toInt()
}

fun String.removeComa(): String {
    return this.replace(",", "")
}





  fun Double.convertDoubleToCurrencyEnglish(): String {
    return NumberFormat.getInstance(Locale("en", "EG")).format(this)

}

fun Double.convertDoubleToCurrencyArabic(): String {
    return NumberFormat.getInstance(Locale("ar", "EG")).format(this)
}


fun String.removeEgyptianCode(): String {
    var phone = this
    if (phone.startsWith("002") || phone.startsWith("+20")) {
        phone = phone.removeRange(0, 3)
    }
    if (phone.startsWith("02") || phone.startsWith("20")) {
        phone = phone.removeRange(0, 2)
    }
    if (phone.startsWith("2")) {
        phone = phone.removeRange(0, 1)
    }

    return phone
}

//fun toOfferPercentage(number: Double): String {
//    val locale = Locale.getDefault()
//    val context = MainApplication.instance.applicationContext
//    val save = context.getStringByLocale(R.string.offer_save, locale)
//    return "$save ${DecimalFormat("0.##").apply { this.minimumFractionDigits = 0 }.format(number)}%"
//}

fun toProductPercentage(number: Int): String {
    val locale = Locale.getDefault()
    val fm = FormatManager(locale)
    return if (locale != Locale.ENGLISH) "%${fm.formatNumber(number)}" else "${
        fm.formatNumber(
            number
        )
    }%"
}


fun String.fromHTML(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }
}


fun String.containsSpecialChar(): Boolean {
    val specialCharactersString = "!@#$%&*()'+,-./:;<=>?[]^_`{|}"
    for (i in this.indices) {
        val ch = this[i]
        if (specialCharactersString.contains(ch.toString())) {
            return true
        } else if (i == this.length - 1){
            return false
        }
    }
    return false
  }




fun String.validatePassword(): Boolean {
   if (this.length != 4){
       return false
   }else if (this.isSequentialNumbers()){
     return false
   } else {
       for (i in 0 until this.length - 1) {
           for (j in i + 1 until this.length) {
               if (this.toCharArray()[i] == this.toCharArray()[j]) {
                   return false
               }
           }
       }
       return true
   }

}


fun String.isSequentialNumbers(): Boolean {
    for (i in 0 until (this.length - 3) + 1) {
        if (this.toCharArray()[i] == this.toCharArray()[i+2] - 2) {
            return true
        }
    }
    return false
}






object StringExt