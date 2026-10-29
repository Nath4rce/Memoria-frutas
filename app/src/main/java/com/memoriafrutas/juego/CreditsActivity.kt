package com.memoriafrutas.juego

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CreditsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credits)
        findViewById<Button>(R.id.btnBack).setOnClickListener { finish() }
    }
}