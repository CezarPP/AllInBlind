package com.example.all_in_blind

import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.viewModels
import com.example.all_in_blind.databinding.FragmentGameBinding


class GameFragment : Fragment() {
    private lateinit var binding: FragmentGameBinding
    private val viewModel: GameViewModel by viewModels()
    private var toastsEnabled: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            toastsEnabled = it.getBoolean("areToastsEnabled")
            Log.d("debug", "In gameFragment -> toastsEnabled = $toastsEnabled")
        }
        binding = inflate(inflater, R.layout.fragment_game, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.btnDeal.setOnClickListener{
            binding.btnDeal.isEnabled = false
            viewModel.shuffleDeck()

            dealPlayerCards(viewModel.deck)

            dealFlop(viewModel.deck)
            dealTurn(viewModel.deck)
            dealRiver(viewModel.deck)

            val winner = viewModel.determineWinner()
            if(toastsEnabled)
                when (winner) {
                    0 -> Toast.makeText(context, "Draw", Toast.LENGTH_SHORT).show()
                    1 -> Toast.makeText(
                        context, "Winner Player 1 with ${
                            viewModel.findHandPlayer(
                                viewModel.deck,
                                viewModel.deck[0],
                                viewModel.deck[1]
                            ).hand
                        }", Toast.LENGTH_SHORT
                    ).show()
                    2 -> Toast.makeText(
                        context, "Winner Player 2 with ${
                            viewModel.findHandPlayer(
                                viewModel.deck,
                                viewModel.deck[2],
                                viewModel.deck[3]
                            ).hand
                        }", Toast.LENGTH_SHORT
                    ).show()
                }
            binding.btnDeal.isEnabled = true
        }
        binding.btnReset.setOnClickListener {
            binding.btnReset.isEnabled = false
            viewModel.resetScore()
            resetImages()
            binding.btnReset.isEnabled = true
        }
    }
    private fun dealPlayerCards(deck: Array<Card>) {
        binding.player1Card1.setImageResource(deck[0].toId())
        binding.player1Card2.setImageResource(deck[1].toId())
        binding.player2Card1.setImageResource(deck[2].toId())
        binding.player2Card2.setImageResource(deck[3].toId())
    }
    private fun dealRiver(deck: Array<Card>) {
        binding.turn.setImageResource(deck[8].toId())
    }

    private fun dealTurn(deck: Array<Card>) {
        binding.river.setImageResource(deck[7].toId())
    }

    private fun dealFlop(deck: Array<Card>) {
        val flopIds: Array<ImageView> = arrayOf(binding.flop1, binding.flop2, binding.flop3)
        for (i in 4..6)
            flopIds[i - 4].setImageResource(deck[i].toId())
    }
    private fun resetImages() {
        binding.player1Card1.setImageResource(android.R.color.transparent)
        binding.player1Card2.setImageResource(android.R.color.transparent)
        binding.player2Card1.setImageResource(android.R.color.transparent)
        binding.player2Card2.setImageResource(android.R.color.transparent)
        binding.flop1.setImageResource(android.R.color.transparent)
        binding.flop2.setImageResource(android.R.color.transparent)
        binding.flop3.setImageResource(android.R.color.transparent)
        binding.turn.setImageResource(android.R.color.transparent)
        binding.river.setImageResource(android.R.color.transparent)
    }
}