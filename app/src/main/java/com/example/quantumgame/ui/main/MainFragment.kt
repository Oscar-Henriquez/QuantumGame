package com.example.quantumgame.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.quantumgame.R
import android.widget.Toast
import com.example.quantumgame.MainActivity
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // set on-click listener
        startButton.setOnClickListener {
            // your code to perform when the user clicks on the button
            startGame()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

    }

    private fun startGame() {
        activity?.supportFragmentManager?.fragments?.get(0)?.startButton?.visibility = View.GONE
        // Get the text fragment instance
        val gameFragment = GameFragment()

        // Get the support fragment manager instance
        val manager = activity?.supportFragmentManager

        // Begin the fragment transition using support fragment manager
        val transaction = manager?.beginTransaction()

        // Replace the fragment on container
        transaction?.replace(R.id.main,gameFragment)
        transaction?.addToBackStack(null)

        // Finishing the transition
        transaction?.commit()
    }

}
