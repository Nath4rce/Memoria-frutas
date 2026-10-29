package com.memoriafrutas.juego

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.ToneGenerator

class SoundManager(private val ctx: Context) {

    private val tone: ToneGenerator? = try {
        ToneGenerator(AudioManager.STREAM_MUSIC, 80)
    } catch (e: RuntimeException) {
        null
    }
    private var player: MediaPlayer? = null

    fun playMatch() = play("acierto") { tone?.startTone(ToneGenerator.TONE_PROP_ACK, 150) }
    fun playError() = play("error") { tone?.startTone(ToneGenerator.TONE_PROP_NACK, 200) }
    fun playWin() = play("victoria") { tone?.startTone(ToneGenerator.TONE_PROP_BEEP2, 400) }

    private fun play(rawName: String, fallback: () -> Unit) {
        // Busca res/raw/<rawName>.mp3 (o .wav/.ogg). Si no existe, usa el tono.
        val id = ctx.resources.getIdentifier(rawName, "raw", ctx.packageName)
        if (id == 0) {
            fallback()
            return
        }
        player?.release()
        val mp = MediaPlayer.create(ctx, id)
        if (mp == null) {
            fallback()
            return
        }
        mp.setOnCompletionListener { it.release() }
        mp.start()
        player = mp
    }

    fun release() {
        tone?.release()
        player?.release()
        player = null
    }
}