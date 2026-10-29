package com.memoriafrutas.juego

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast

class CreditsActivity : BaseActivity() {

    private var taps = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credits)

        findViewById<Button>(R.id.btnBack).setOnClickListener { finish() }

        // EASTER EGG: tocar 7 veces el título dispara la lluvia de frutas
        findViewById<TextView>(R.id.tvCreditsTitle).setOnClickListener {
            taps++
            if (taps >= 7) {
                taps = 0
                fruitRain()
            }
        }
    }

    private fun fruitRain() {
        Toast.makeText(this, "🍓 ¡Lluvia de frutas! 🍓", Toast.LENGTH_SHORT).show()

        // android.R.id.content es el contenedor raíz: añadir vistas aquí las pone encima de todo
        val root = findViewById<ViewGroup>(android.R.id.content)
        val fruits = listOf("🍎", "🍓", "🍇", "🍌", "🍉", "🍍", "🍒", "🥝")
        val width = root.width
        val height = root.height

        repeat(30) { i ->
            val fruit = TextView(this)
            fruit.text = fruits.random()
            fruit.textSize = (24..44).random().toFloat()
            fruit.translationX = (0..maxOf(width - 120, 1)).random().toFloat()
            fruit.translationY = -150f   // empieza justo arriba de la pantalla
            root.addView(
                fruit,
                FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )

            fruit.animate()
                .translationY(height + 150f)           // cae hasta debajo de la pantalla
                .rotationBy(360f)                      // mientras gira
                .setStartDelay(i * 100L)               // cada fruta sale un poco después
                .setDuration((2000..3500).random().toLong())
                .withEndAction { root.removeView(fruit) }  // se elimina al terminar
                .start()
        }
    }
}