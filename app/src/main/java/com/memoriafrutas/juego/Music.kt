package com.memoriafrutas.juego

import android.content.Context
import android.media.MediaPlayer

object Music {
    private var player: MediaPlayer? = null

    fun start(ctx: Context) {
        val app = ctx.applicationContext
        if (player == null) {
            // Busca res/raw/musica_fondo.mp3; si no existe, no suena nada
            val id = app.resources.getIdentifier("musica_fondo", "raw", app.packageName)
            if (id == 0) return
            player = MediaPlayer.create(app, id)?.apply {
                isLooping = true          // se repite sin parar
                setVolume(0.4f, 0.4f)     // volumen bajo para no tapar los efectos
            }
        }
        player?.let { if (!it.isPlaying) it.start() }
    }

    fun pause() {
        player?.let { if (it.isPlaying) it.pause() }
    }

    fun stop() {
        player?.release()
        player = null
    }
}