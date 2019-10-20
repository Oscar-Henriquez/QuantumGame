package com.example.quantumgame.ui.main

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.example.quantumgame.R


class MarketLayout : LinearLayout {
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    constructor(context: Context) : super(context) {
        initView()
    }

    fun initView() {
        val view = View.inflate(context, R.layout.game_fragment, null)
        addView(view)
    }
}