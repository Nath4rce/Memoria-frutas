package com.memoriafrutas.juego

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var etPlayer: EditText
    private lateinit var tvBest: TextView
    private var pulse: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        etPlayer = findViewById(R.id.etPlayer)
        tvBest = findViewById(R.id.tvBest)
        val btnPlay = findViewById<Button>(R.id.btnPlay)
        val btnScore = findViewById<Button>(R.id.btnScore)
        val btnCredits = findViewById<Button>(R.id.btnCredits)

        // Persistencia: recupera el último nombre guardado
        etPlayer.setText(Prefs.getName(this))

        // Animación de entrada de toda la pantalla
        val root = findViewById<View>(R.id.homeRoot)
        root.alpha = 0f
        root.animate().alpha(1f).setDuration(700).start()

        // Animación: el botón Jugar "late" para llamar la atención
        pulse = ObjectAnimator.ofPropertyValuesHolder(
            btnPlay,
            PropertyValuesHolder.ofFloat("scaleX", 1f, 1.06f),
            PropertyValuesHolder.ofFloat("scaleY", 1f, 1.06f)
        ).apply {
            duration = 700
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            start()
        }

        btnPlay.setOnClickListener {
            val name = etPlayer.text.toString().trim().ifEmpty { "Jugador" }
            Prefs.setName(this, name)
            startActivity(Intent(this, GameActivity::class.java))
        }
        btnScore.setOnClickListener {
            startActivity(Intent(this, ScoreActivity::class.java))
        }
        btnCredits.setOnClickListener {
            startActivity(Intent(this, CreditsActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        // Se actualiza cada vez que se vuelve al Home
        tvBest.text = "🏆 Mejor puntaje: ${Prefs.getBest(this)}"
    }

    override fun onDestroy() {
        pulse?.cancel()
        super.onDestroy()
    }
}