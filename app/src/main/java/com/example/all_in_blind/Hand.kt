package com.example.all_in_blind

class Hand(private val highCards: IntArray, val hand: String): Comparable<Hand>{

    private val hands = listOf("High card", "Pair", "Two pair", "Three of a kind", "Straight", "Flush",
        "Full house", "Four of a kind", "Straight flush", "Royal flush")
    override fun compareTo(other: Hand): Int {
        if(hands.indexOf(this.hand) != hands.indexOf(other.hand))
            return hands.indexOf(this.hand) - hands.indexOf(other.hand)
        else {
            if(highCards.size != other.highCards.size)
                throw IllegalAccessError ("The hands are $hand and ${other.hand} and sizes are " +
                        "${highCards.size} and ${other.highCards.size}")
            if(highCards.size != 5 || other.highCards.size != 5)
                throw IllegalAccessError("The hands are $hand and ${other.hand} and hands are not of size 5")
            for(i in highCards.indices)
                if(highCards[i] > other.highCards[i])
                    return 1
                else if(other.highCards[i] > highCards[i])
                    return -1
            return 0
        }
    }
}