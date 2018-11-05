package com.example.mayank.kwizzapp

import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object Constants {

    val ACCOUNT_NAME = "KwizzApp"

    const val RC_SIGN_IN = 100
    const val RC_LEADERBOARD_UI = 9004
    val RC_ACHIEVEMENT_UI = 9003



    val MERCHANT_ID = "4873218"
    val MERCHANT_KEY = "HqRplS"
    val surl = "https://www.payumoney.com/mobileapp/payumoney/success.php"
    val furl = "https://www.payumoney.com/mobileapp/payumoney/failure.php"
    val URL = "http://alchemyeducation.org/payu/getHashCode.php"

    // Api
    const val API_BASE_URL = "http://www.alchemyeducation.org/"

    const val CONNECTION_TIMEOUT: Long = 60
    const val READ_TIMEOUT: Long = 60

    const val RIGHT_ANSWERS = "RightAnswers"
    const val WRONG_ANSWERS = "WrongAnswers"
    const val DROP_QUESTIONS = "DropQuestions"

    const val TRANSACTION_TYPE_DEBITED = "Debited"
    const val TRANSACTION_TYPE_CREDITED = "Credited"

    const val DISPLAY_FULL_DATE_FORMAT = "dd-MM-yyyy hh:mm:ss a"

    fun getFormatDate(date : Date): String? {
        val calendar = Calendar.getInstance()
        val date = calendar.time
        val formatter : java.text.DateFormat = SimpleDateFormat(DISPLAY_FULL_DATE_FORMAT)
        return formatter.format(date)
    }

}