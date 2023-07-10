package com.example.all_in_blind

class Card(val nr: Int, val suit: String) {
    fun toId(): Int {
        return when (suit) {
            "Clubs" -> {
                when (nr) {
                    1 -> R.drawable.club_1
                    2 -> R.drawable.club_2
                    3 -> R.drawable.club_3
                    4 -> R.drawable.club_4
                    5 -> R.drawable.club_5
                    6 -> R.drawable.club_6
                    7 -> R.drawable.club_7
                    8 -> R.drawable.club_8
                    9 -> R.drawable.club_9
                    10 -> R.drawable.club_10
                    11 -> R.drawable.club_11
                    12 -> R.drawable.club_12
                    13 -> R.drawable.club_13
                    else -> R.drawable.card_back
                }
            }
            "Hearts" -> {
                when (nr) {
                    1 -> R.drawable.heart_1
                    2 -> R.drawable.heart_2
                    3 -> R.drawable.heart_3
                    4 -> R.drawable.heart_4
                    5 -> R.drawable.heart_5
                    6 -> R.drawable.heart_6
                    7 -> R.drawable.heart_7
                    8 -> R.drawable.heart_8
                    9 -> R.drawable.heart_9
                    10 -> R.drawable.heart_10
                    11 -> R.drawable.heart_11
                    12 -> R.drawable.heart_12
                    13 -> R.drawable.heart_13
                    else -> R.drawable.card_back
                }
            }
            "Diamonds" -> {
                when (nr) {
                    1 -> R.drawable.diamond_1
                    2 -> R.drawable.diamond_2
                    3 -> R.drawable.diamond_3
                    4 -> R.drawable.diamond_4
                    5 -> R.drawable.diamond_5
                    6 -> R.drawable.diamond_6
                    7 -> R.drawable.diamond_7
                    8 -> R.drawable.diamond_8
                    9 -> R.drawable.diamond_9
                    10 -> R.drawable.diamond_10
                    11 -> R.drawable.diamond_11
                    12 -> R.drawable.diamond_12
                    13 -> R.drawable.diamond_13
                    else -> R.drawable.card_back
                }
            }
            "Spades" -> {
                when (nr) {
                    1 -> R.drawable.spade_1
                    2 -> R.drawable.spade_2
                    3 -> R.drawable.spade_3
                    4 -> R.drawable.spade_4
                    5 -> R.drawable.spade_5
                    6 -> R.drawable.spade_6
                    7 -> R.drawable.spade_7
                    8 -> R.drawable.spade_8
                    9 -> R.drawable.spade_9
                    10 -> R.drawable.spade_10
                    11 -> R.drawable.spade_11
                    12 -> R.drawable.spade_12
                    13 -> R.drawable.spade_13
                    else -> R.drawable.card_back
                }
            }
            else -> R.drawable.card_back
        }
    }
}