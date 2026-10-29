package com.memoriafrutas.juego

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Animación: aparece (fade) y crece (scale)
        val content = findViewById<View>(R.id.splashContent)
        content.alpha = 0f
        content.scaleX = 0.6f
        content.scaleY = 0.6f
        content.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(1200).start()

        // A los 2.5 segundos pasa al Home
        handler.postDelayed({
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }, 2500)
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}