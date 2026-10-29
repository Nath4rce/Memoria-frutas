# Memoria de Frutas 

Juego de memoria de parejas para Android, hecho como **aplicación nativa** con Kotlin y XML.
Entrega #4 - Final (Juego).

## Descripción
Voltea las cartas y encuentra todas las parejas de frutas antes de que se acaben el tiempo o las vidas.

## Características
- 3 niveles de dificultad (4, 6 y 8 parejas)
- Sistema de vidas y control de tiempo con cuenta regresiva
- 5 secciones: Splash, Home, Juego, Puntaje y Créditos
- Persistencia de datos entre vistas (nombre, último resultado y mejor puntaje) con SharedPreferences
- Animaciones: volteo y rebote de cartas, transiciones y botón pulsante
- Música de fondo y efectos de sonido
- UI personalizada con paleta pastel
- Esta app esconde algunos secretos 

## Cómo se juega
1. Escribe tu nombre en el Home y pulsa **Jugar**.
2. Toca dos cartas por turno. Si son iguales, ganas puntos; si no, pierdes una vida.
3. Completa cada nivel antes de que se acabe el tiempo para pasar al siguiente.

## Tecnologías
- Android Studio
- Kotlin
- XML (Views)
- Mínimo SDK: API 24

## Estructura del proyecto
- `SplashActivity`: pantalla de bienvenida
- `HomeActivity`: menú principal
- `GameActivity`: lógica del juego
- `ScoreActivity`: resultado y mejor puntaje
- `CreditsActivity`: créditos
- `Prefs`: persistencia con SharedPreferences
- `SoundManager`: efectos de sonido
- `Music`: música de fondo

## Wireframe
Ver el enlace de Figma: [Figma](https://www.figma.com/design/1V6eqGJiWskYk5BSzlNLZ4/Memoria-de-frutas?node-id=0-1&t=Naf7WbuXelkRlBTj-1)

## Cómo ejecutar
1. Clonar el repositorio.
2. Abrir el proyecto con Android Studio.
3. Sincronizar Gradle y ejecutar en un emulador o dispositivo.

## Créditos
- Desarrollado por: Natalia Arce Peñuela
- Música y efectos de sonido: [Pixabay](https://pixabay.com)
