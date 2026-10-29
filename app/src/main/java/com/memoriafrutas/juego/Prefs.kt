package com.memoriafrutas.juego

import android.content.Context

object Prefs {
    private const val FILE = "memoria_prefs"

    private fun sp(c: Context) = c.getSharedPreferences(FILE, Context.MODE_PRIVATE)

    fun getName(c: Context): String = sp(c).getString("name", "Jugador") ?: "Jugador"
    fun setName(c: Context, name: String) = sp(c).edit().putString("name", name).apply()

    fun getBest(c: Context): Int = sp(c).getInt("best", 0)
    fun getLastScore(c: Context): Int = sp(c).getInt("lastScore", 0)
    fun getLastLevel(c: Context): Int = sp(c).getInt("lastLevel", 0)
    fun getLastWon(c: Context): Boolean = sp(c).getBoolean("lastWon", false)

    fun saveResult(c: Context, score: Int, level: Int, won: Boolean) {
        val best = maxOf(getBest(c), score)
        sp(c).edit()
            .putInt("lastScore", score)
            .putInt("lastLevel", level)
            .putBoolean("lastWon", won)
            .putInt("best", best)
            .apply()
    }
}