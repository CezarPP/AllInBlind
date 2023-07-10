package com.example.all_in_blind

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.max

class GameViewModel : ViewModel() {

    private var _winsP1 = MutableLiveData(0)
    val winsP1: LiveData<Int> = _winsP1
    private var _winsP2 = MutableLiveData(0)
    val winsP2: LiveData<Int> = _winsP2

    private val deckAux: IntArray = (1..52).toList().toIntArray()
    val deck = Array(52) { Card(1, "Spades") }

    private val suits = listOf("Clubs", "Hearts", "Diamonds", "Spades")

    fun determineWinner(): Int {
        val handPlayer1: Hand = findHandPlayer(deck, deck[0], deck[1])
        val handPlayer2: Hand = findHandPlayer(deck, deck[2], deck[3])

        return when {
            handPlayer1 == handPlayer2 -> {
                Log.d("debug", "determineWinner: 0")
                0
            }
            handPlayer1 > handPlayer2 -> {
                _winsP1.value = _winsP1.value!!.inc()
                1
            }
            else -> {
                _winsP2.value = _winsP2.value!!.inc()
                2
            }
        }
    }

    fun resetScore() {
        _winsP1.value = 0
        _winsP2.value = 0
    }
    fun shuffleDeck() {
        deckAux.shuffle()
        for (x in deckAux.indices)
            deck[x] = numberToCard(deckAux[x])
    }

    fun findHandPlayer(deck: Array<Card>, card1: Card, card2: Card): Hand {
        // common cards are at 4..8 in the deck
        val hand: Array<Card> = arrayOf(
            *(deck.filterIndexed { index, _ -> index in 4..8 }.toTypedArray()),
            card1,
            card2
        )
        when {
            isRoyalFlush(hand) -> return Hand(IntArray(5) { 1 }, "Royal flush")
            isStraightFlush(hand) != 0 -> {
                val suitOfFlush: String =
                    hand.first { i -> hand.count { it.suit == i.suit } >= 5 }.suit
                val sameSuit: Array<Card> = hand.filter { it.suit == suitOfFlush }.toTypedArray()
                val highCard = straightHigh(sameSuit)
                return Hand(IntArray(5) { highCard }, "Straight flush")
            }
            isFlush(hand) -> {
                val highCards = mutableListOf<Int>()
                val suitOfFlush: String =
                    hand.first { i -> hand.count { it.suit == i.suit } >= 5 }.suit
                for (i in hand)
                    if (i.suit == suitOfFlush)
                        highCards.add(escapeAce(i.nr))
                highCards.sortDescending()
                return Hand(highCards.slice(0..4).toIntArray(), "Flush")
            }
            isFourOfAKind(hand) -> {
                val highCards = mutableListOf<Int>()
                var realHighCard = 0
                for (i in hand)
                    if (hand.count { it.nr == i.nr } == 4)
                        highCards.add(escapeAce(i.nr))
                    else realHighCard = max(realHighCard, escapeAce(i.nr))
                highCards.add(realHighCard)
                return Hand(highCards.toIntArray(), "Four of a kind")
            }
            isFullHouse(hand) -> {
                val threeHigh = mutableListOf<Int>()
                val twoHigh = mutableListOf<Int>()
                for (i in hand)
                    if (hand.count { it.nr == i.nr } == 3)
                        threeHigh.add(escapeAce(i.nr))
                    else if (hand.count { it.nr == i.nr } == 2)
                        twoHigh.add(escapeAce(i.nr))
                threeHigh.addAll(twoHigh)
                return Hand(threeHigh.toIntArray(), "Full house")
            }
            straightHigh(hand) != 0 -> {
                val high = straightHigh(hand)
                return Hand(IntArray(5) { high }, "Straight")
            }
            isThreeOfAKind(hand) -> {
                val highCards = mutableListOf<Int>()
                val realHighCards = mutableListOf<Int>()
                for (i in hand)
                    if (hand.count { it.nr == i.nr } != 3)
                        realHighCards.add(escapeAce(i.nr))
                    else highCards.add(escapeAce(i.nr))
                realHighCards.sortDescending()
                highCards.addAll(realHighCards.slice(0..1))
                return Hand(highCards.toIntArray(), "Three of a kind")
            }
            isTwoPair(hand) -> {
                val highCards = mutableListOf<Int>()
                val cardsInPair = mutableListOf<Int>()
                var realHighCard = 0
                for(card in hand)
                    if(hand.count {it.nr == card.nr} == 2)
                        cardsInPair.add(escapeAce(card.nr))
                cardsInPair.sortDescending()
                highCards.addAll(cardsInPair.slice(0..3))
                for(card in hand)
                    if(escapeAce(card.nr) != cardsInPair[0] && escapeAce(card.nr) != cardsInPair[2])
                        realHighCard = max(realHighCard, escapeAce(card.nr))
                highCards.add(realHighCard)
                return Hand(highCards.toIntArray(), "Two pair")
            }
            isPair(hand) -> {
                val highCards = mutableListOf<Int>()
                val realHighCards = mutableListOf<Int>()
                for (i in hand)
                    if (hand.count { it.nr == i.nr } == 2)
                        highCards.add(escapeAce(i.nr))
                    else realHighCards.add(escapeAce(i.nr))
                realHighCards.sortDescending()
                if (realHighCards.size >= 3)
                    highCards.addAll(realHighCards.slice(0..2))
                else highCards.addAll(realHighCards)
                return Hand(highCards.toIntArray(), "Pair")
            }
            else -> {
                val highCards = mutableListOf<Int>()
                for (i in hand)
                    highCards.add(escapeAce(i.nr))
                highCards.sortDescending()
                return Hand(highCards.slice(0..4).toIntArray(), "High card")
            }
        }
    }


    private fun escapeAce(nr: Int): Int {
        if (nr == 1)
            return 14
        return nr
    }

    private fun numberToCard(nr: Int): Card {
        val suit: String = when ((nr - 1) / 13) {
            0 -> "Clubs"
            1 -> "Hearts"
            2 -> "Diamonds"
            3 -> "Spades"
            else -> "Nothing"
        }
        val number: Int = (nr - 1) % 13 + 1
        return Card(number, suit)
    }

    private fun isRoyalFlush(hand: Array<Card>): Boolean {
        val highCard = isStraightFlush(hand)
        return highCard == 14
    }

    private fun isStraightFlush(hand: Array<Card>): Int {
        if (!isFlush(hand))
            return 0
        val suitOfFlush: String = hand.first { i -> hand.count { it.suit == i.suit } >= 5 }.suit
        val sameSuit: Array<Card> = hand.filter { it.suit == suitOfFlush }.toTypedArray()
        return straightHigh(sameSuit)
    }

    private fun isTwoPair(hand: Array<Card>): Boolean {
        hand.sortBy { it.nr }
        var nr = 0
        for (i in 1 until hand.size)
            if (hand[i].nr == hand[i - 1].nr)
                nr++
        return nr >= 2
    }

    private fun straightHigh(hand: Array<Card>): Int {
        val numbers = mutableListOf<Int>()
        for (i in hand) {
            numbers.add(i.nr)
            if (i.nr == 1)
                numbers.add(14)
        }
        numbers.sort()
        for (i in numbers.size - 5 downTo 0) {
            var found = true
            for (j in i + 1..i + 4)
                if (numbers[j] != numbers[j - 1] + 1)
                    found = false
            if (found)
                return numbers[i + 4]
        }
        return 0
    }

    private fun isFullHouse(hand: Array<Card>): Boolean {
        for (i in hand)
            if (hand.count { it.nr == i.nr } == 3) {
                for (j in hand)
                    if (i.nr != j.nr && hand.count { it.nr == j.nr } == 2)
                        return true
            }
        return false
    }

    private fun isThreeOfAKind(hand: Array<Card>): Boolean {
        return hand.any { i -> hand.count { it.nr == i.nr } == 3 }
    }

    private fun isPair(hand: Array<Card>): Boolean {
        return hand.any { i -> hand.count { it.nr == i.nr } == 2 }
    }

    private fun isFourOfAKind(hand: Array<Card>): Boolean {
        return hand.any { i -> hand.count { it.nr == i.nr } == 4 }
    }

    private fun isFlush(hand: Array<Card>): Boolean {
        return suits.any { suit -> hand.count { it.suit == suit } >= 5 }
    }
}