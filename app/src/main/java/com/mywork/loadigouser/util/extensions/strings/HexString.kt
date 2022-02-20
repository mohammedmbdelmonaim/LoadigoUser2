package com.mywork.loadigouser.util.extensions.strings

object HexString {

    private const val HEX_CHARS = "0123456789ABCDEF"
    private val HEX_CHARS_ARRAY = HEX_CHARS.toCharArray()

    @JvmStatic
    fun convertStringToHex(str: String): String {
        val chars = str.toCharArray()
        val hex = StringBuffer()
        for (i in chars.indices) {
            hex.append(Integer.toHexString(chars[i].toInt()))
        }
        return hex.toString()
    }

    @JvmStatic
    fun convertHexToString(hex: String): String {
        val sb = StringBuilder()
        val temp = StringBuilder()
        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        var i = 0
        while (i < hex.length - 1) {
            // grab the hex in pairs
            val output = hex.substring(i, i + 2)
            // convert hex to decimal
            val decimal = output.toInt(16)
            // convert the decimal to character
            sb.append(decimal.toChar())
            temp.append(decimal)
            i += 2
        }
        println("Decimal : $temp")
        return sb.toString()
    }

    fun hexStringToByteArray(data: String): ByteArray {
        val result = ByteArray(data.length / 2)
        for (i in data.indices step 2) {
            val firstIndex = HEX_CHARS.indexOf(data[i]);
            val secondIndex = HEX_CHARS.indexOf(data[i + 1]);
            val octet = firstIndex.shl(4).or(secondIndex)
            result[i.shr(1)] = octet.toByte()
        }
        return result
    }

    fun byteArrayToHexString(byteArray: ByteArray): String {
        val result = StringBuffer()
        byteArray.forEach {
            val octet = it.toInt()
            val firstIndex = (octet and 0xF0).ushr(4)
            val secondIndex = octet and 0x0F
            result.append(HEX_CHARS_ARRAY[firstIndex])
            result.append(HEX_CHARS_ARRAY[secondIndex])
        }
        return result.toString()
    }
}