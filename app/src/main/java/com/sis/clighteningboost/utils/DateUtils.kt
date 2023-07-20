package com.sis.clighteningboost.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    @JvmStatic
    val currentDate: String
        get() {
            val format =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            return format.format(Date())
        }
}