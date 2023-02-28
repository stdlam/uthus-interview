package com.practices.alebeer.helper

import java.text.SimpleDateFormat
import java.util.*

object DateHelper {
    private const val SALE_OFF_FORMAT = "HH:mm:ss dd-MM"

    fun getSaleDateByMillis(saleMillis: Long): String {
        val saleCal = Calendar.getInstance().apply {
            timeInMillis = saleMillis
        }
        val sdf = SimpleDateFormat(SALE_OFF_FORMAT, Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        return try {
            sdf.format(saleCal.time)
        } catch (ex: Exception) {
            ""
        }
    }
}