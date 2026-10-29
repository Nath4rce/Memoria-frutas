package com.memoriafrutas.juego

import android.content.Intent
import android.graphics.Color
import android.os.CountDownTimer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    // Una carta: su emoji, su vista y su estado
    private class Card(val emoji: String, val view: TextView) {
        var faceUp = false
        var matched = false
    }

    // --- Configuración de los 3 niveles ---
    private val fruits = listOf("🍎", "🍓", "🍇", "🍌", "🍉", "🍍", "🍒", "🥝")
    private val pairsPerLevel = listOf(4, 6, 8)        // parejas por nivel
    private val secondsPerLevel = listOf(60, 90, 120)  // tiempo por nivel
    private val livesPerLevel = listOf(6, 5, 4)        // vidas por nivel

    private lateinit var grid: GridLayout
    private lateinit var tvLevel: TextView
    private lateinit var tvLives: TextView
    private lateinit var tvTime: TextView
    private lateinit var tvScore: TextView
    private lateinit var sounds: SoundManager

    private val handler = Handler(Looper.getMainLooper())
    private var timer: CountDownTimer? = null
    private val cards = mutableListOf<Card>()
    private var first: Card? = null
    private var busy = false          // true mientras se comparan dos cartas
    private var finished = false

    private var levelIndex = 0
    private var lives = 0
    private var score = 0
    private var matchedPairs = 0
    private var secondsLeft = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        grid = findViewById(R.id.grid)
        tvLevel = findViewById(R.id.tvLevel)
        tvLives = findViewById(R.id.tvLives)
        tvTime = findViewById(R.id.tvTime)
        tvScore = findViewById(R.id.tvScore)
        sounds = SoundManager(this)

        startLevel()
    }

    private fun startLevel() {
        timer?.cancel()
        handler.removeCallbacksAndMessages(null)
        cards.clear()
        grid.removeAllViews()
        first = null
        busy = false
        matchedPairs = 0
        lives = livesPerLevel[levelIndex]
        secondsLeft = secondsPerLevel[levelIndex]

        val pairs = pairsPerLevel[levelIndex]
        // Duplica las frutas del nivel y las mezcla
        val emojis = (fruits.take(pairs) + fruits.take(pairs)).shuffled()

        val cols = 4
        grid.columnCount = cols
        val margin = dp(6)
        val size = (resources.displayMetrics.widthPixels - dp(32) - cols * margin * 2) / cols

        for (e in emojis) {
            val tv = TextView(this)
            tv.layoutParams = GridLayout.LayoutParams().apply {
                width = size
                height = size
                setMargins(margin, margin, margin, margin)
            }
            tv.gravity = Gravity.CENTER
            tv.textSize = 32f
            tv.setTextColor(Color.WHITE)
            tv.text = "?"
            tv.setBackgroundResource(R.drawable.card_back)

            val card = Card(e, tv)
            tv.setOnClickListener { onCardClicked(card) }
            grid.addView(tv)
            cards.add(card)
        }

        updateHud()
        startTimer()
    }

    private fun startTimer() {
        timer = object : CountDownTimer(secondsLeft * 1000L, 1000) {
            override fun onTick(msLeft: Long) {
                secondsLeft = ((msLeft + 999) / 1000).toInt()
                updateHud()
            }

            override fun onFinish() {
                secondsLeft = 0
                updateHud()
                endGame(false)   // se acabó el tiempo
            }
        }.start()
    }

    private fun onCardClicked(card: Card) {
        if (busy || finished || card.faceUp || card.matched) return

        flip(card, true)

        val firstCard = first
        if (firstCard == null) {
            first = card          // primera carta del intento
            return
        }
        // Segunda carta: esperamos un momento para que el jugador la vea
        busy = true
        first = null
        handler.postDelayed({ checkPair(firstCard, card) }, 600)
    }

    private fun checkPair(a: Card, b: Card) {
        if (a.emoji == b.emoji) {
            // ACIERTO
            a.matched = true
            b.matched = true
            sounds.playMatch()
            bounce(a.view)
            bounce(b.view)
            score += 100
            matchedPairs++
            updateHud()
            busy = false
            if (matchedPairs == pairsPerLevel[levelIndex]) levelCompleted()
        } else {
            // ERROR: pierde una vida y las cartas se voltean de nuevo
            sounds.playError()
            lives--
            updateHud()
            flip(a, false)
            flip(b, false)
            if (lives <= 0) {
                endGame(false)
            } else {
                handler.postDelayed({ busy = false }, 300)
            }
        }
    }

    private fun levelCompleted() {
        timer?.cancel()
        val bonus = secondsLeft * 5 + lives * 20   // bonus por tiempo y vidas sobrantes
        score += bonus
        updateHud()

        if (levelIndex == pairsPerLevel.lastIndex) {
            endGame(true)   // terminó el nivel 3: ganó
        } else {
            sounds.playWin()
            AlertDialog.Builder(this)
                .setTitle("¡Nivel ${levelIndex + 1} completado! 🎉")
                .setMessage("Bonus por tiempo y vidas: +$bonus")
                .setCancelable(false)
                .setPositiveButton("Siguiente nivel") { _, _ ->
                    levelIndex++
                    startLevel()
                }
                .show()
        }
    }

    private fun endGame(won: Boolean) {
        if (finished) return
        finished = true
        timer?.cancel()
        if (won) sounds.playWin() else sounds.playError()

        // Persistencia: se guarda el resultado para que lo lea ScoreActivity
        Prefs.saveResult(this, score, levelIndex + 1, won)

        handler.postDelayed({
            startActivity(Intent(this, ScoreActivity::class.java))
            finish()
        }, 900)
    }

    // Animación de volteo: gira hasta 90°, cambia la cara y termina de girar
    private fun flip(card: Card, faceUp: Boolean) {
        card.faceUp = faceUp
        val v = card.view
        v.cameraDistance = 8000f * resources.displayMetrics.density
        v.animate().rotationY(90f).setDuration(120).withEndAction {
            if (faceUp) {
                v.text = card.emoji
                v.setBackgroundResource(R.drawable.card_front)
            } else {
                v.text = "?"
                v.setBackgroundResource(R.drawable.card_back)
            }
            v.rotationY = -90f
            v.animate().rotationY(0f).setDuration(120).start()
        }.start()
    }

    // Animación de "rebote" al acertar
    private fun bounce(v: View) {
        v.animate().scaleX(1.2f).scaleY(1.2f).setDuration(150).withEndAction {
            v.animate().scaleX(1f).scaleY(1f).setDuration(150).start()
        }.start()
    }

    private fun updateHud() {
        tvLevel.text = "Nivel ${levelIndex + 1}/3"
        tvLives.text = "❤️ $lives"
        tvTime.text = "⏱ $secondsLeft"
        tvScore.text = "⭐ $score"
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    override fun onDestroy() {
        timer?.cancel()
        handler.removeCallbacksAndMessages(null)
        sounds.release()
        super.onDestroy()
    }
}