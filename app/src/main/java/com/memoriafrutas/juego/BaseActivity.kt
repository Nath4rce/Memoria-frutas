package com.memoriafrutas.juego

import androidx.appcompat.app.AppCompatActivity

// Pantalla "base": todas las demás heredan de ella y así comparten la música
open class BaseActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        Music.start(this)    // al mostrarse la pantalla, suena la música
    }

    override fun onPause() {
        Music.pause()        // al salir de la app, se pausa
        super.onPause()
    }
}