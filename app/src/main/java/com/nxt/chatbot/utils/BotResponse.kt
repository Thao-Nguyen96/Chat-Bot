package com.nxt.chatbot.utils

import com.nxt.chatbot.utils.Constants.OPEN_GOOGLE
import com.nxt.chatbot.utils.Constants.OPEN_SEARCH

object BotResponse {

    fun basicResponse(_message: String): String {
        val random = (0..2).random()
        val message = _message.toLowerCase()
        //trả về 1 chuỗi tùy thuộc vào thông báo
        return when {
            //hello
            message.contains("hello") -> {
                when (random) {
                    0 -> "Hello these!"
                    1 -> "sup"
                    2 -> "buongirono!"
                    else -> "error"
                }
            }

            //how are you
            message.contains("how are you") -> {
                when (random) {
                    0 -> "I'm doing fine, thank for asking!"
                    1 -> "I'm hungry"
                    2 -> "pretty good! how about you?"
                    else -> "error"
                }
            }

            message.contains("flip") && message.contains("coin") -> {
                val r = (0..1).random()
                val result = if (r == 0) "heads" else "tails"
                "I flipped a coin and it landed on $result"
            }


            //solves math
            message.contains("solve") -> {
                //subsstringafter: tách lấy chuỗi sau chữ solve
                val equation: String = message.substringAfter("solve")
                return try {
                    val answer = SolveMath.solveMath(equation ?: "0")
                    answer.toString()
                } catch (e: Exception) {
                    "sorry, I can't solve that!"
                }
            }

            //get the current time
            message.contains("time") && message.contains("?") -> {
                Time.timeStamp()
            }


            //open google
            message.contains("open") && message.contains("google") -> {
                OPEN_GOOGLE
            }
            message.contains("search") -> {
                OPEN_SEARCH
            }

            else -> {
                when (random) {
                    0 -> "I don't understand!"
                    1 -> "IDK"
                    2 -> "Try asking me something different!"
                    else -> "error"
                }
            }
        }
    }
}