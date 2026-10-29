package com.memoriafrutas.juego

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ScoreActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        val tvTitle = findViewById<TextView>(R.id.tvResultTitle)
        val tvPlayer = findViewById<TextView>(R.id.tvPlayer)
        val tvScore = findViewById<TextView>(R.id.tvFinalScore)
        val tvLevel = findViewById<TextView>(R.id.tvLevelReached)
        val tvBest = findViewById<TextView>(R.id.tvBestScore)

        // Todo se lee de SharedPreferences (persistencia entre vistas)
        val lastLevel = Prefs.getLastLevel(this)
        tvTitle.text = when {
            lastLevel == 0 -> "Aún no has jugado"
            Prefs.getLastWon(this) -> "🎉 ¡Ganaste!"
            else -> "😢 Fin del juego"
        }
        tvPlayer.text = Prefs.getName(this)
        tvScore.text = "Puntaje: ${Prefs.getLastScore(this)}"
        tvLevel.text = "Nivel alcanzado: $lastLevel de 3"
        tvBest.text = "🏆 Mejor puntaje: ${Prefs.getBest(this)}"

        // Animación: el título aparece creciendo
        tvTitle.scaleX = 0f
        tvTitle.scaleY = 0f
        tvTitle.animate().scaleX(1f).scaleY(1f).setDuration(500).start()

        findViewById<Button>(R.id.btnAgain).setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
            finish()
        }
        findViewById<Button>(R.id.btnMenu).setOnClickListener {
            finish()
        }
    }
}