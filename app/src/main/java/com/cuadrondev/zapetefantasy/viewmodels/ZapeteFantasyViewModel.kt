package com.cuadrondev.zapetefantasy.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cuadrondev.zapetefantasy.R
import com.cuadrondev.zapetefantasy.model.datastore.PreferencesDataStore
import com.cuadrondev.zapetefantasy.model.entities.Player
import com.cuadrondev.zapetefantasy.model.entities.Post
import com.cuadrondev.zapetefantasy.model.entities.User
import com.cuadrondev.zapetefantasy.model.entities.UserTeam
import com.cuadrondev.zapetefantasy.model.repositories.UserDataRepository
import com.cuadrondev.zapetefantasy.model.repositories.UserTeamRepository
import com.cuadrondev.zapetefantasy.utils.LanguageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class ZapeteFantasyViewModel @Inject constructor(
    private val dataStore: PreferencesDataStore,
    private val userDataRepository: UserDataRepository,
    private val userTeamRepository: UserTeamRepository,
    private val languageManager: LanguageManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {


    //EXTRAS username
    private var currentUser = (savedStateHandle.get("USERNAME") as? String)!!


    //IDIOMA
    val currentSetLang by languageManager::currentLang
    val idioma = dataStore.getUserLanguage(currentUser)
    val coin = dataStore.getUserCoin(currentUser)

    //DIALOGOS
    var dialogoPost = mutableStateOf(false)

    var username = mutableStateOf(currentUser)

    //PREFERENCES

    fun changeLanguage(lang: String) {
        Log.d("cambioLang", lang)
        // cambiar lenguaje en datastore y en la interfaz tb
        languageManager.changeLang(lang)
        viewModelScope.launch(Dispatchers.IO) {
            userDataRepository.changeUserLanguage(currentUser, lang)
        }
    }

    fun reloadLanguage(lang: String){
        languageManager.changeLang(lang)
    }

    fun changeUserCoin(coin: String) {
        viewModelScope.launch {
            dataStore.setUserCoin(currentUser, coin)
        }
    }

    val seed = System.currentTimeMillis()

    //DATABASE
    var userData: Flow<User> = userDataRepository.getUserData(username.value)
    var userTeamFlow: Flow<UserTeam> = userDataRepository.getUserTeam(username.value)

    //var alineacionPlayers: Flow<List<UserTeam>> = userTeamRepository.getUserTeam()
    var marketFlow = userTeamRepository.getMarket()

    var postsFlow = userTeamRepository.getPosts()

    var standingsFlow = userDataRepository.getStandings()


    fun updatePlayer(player: Player) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userTeamRepository.updatePlayer(player)
            }
        }
    }



    fun updateUser(user: User) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userDataRepository.updateUser(user)
            }
        }
    }

    fun insertPost(post: Post) {
        viewModelScope.launch {
            userTeamRepository.insertPost(post)
        }
    }



    //SIGUIENTES PARTIDOS
    data class Partido(
        val local: String,
        val visitante: String,
        val dia: String,
        val hora: String
    )

    var listaPartidos = mutableStateOf<List<Partido>>(emptyList())

    fun initializePartidos() {
        // Obtener la lista de nombres de equipos
        val equipos = listOf(
            "Alaves", "Almeria", "Athletic Club", "Atletico de Madrid", "FC Barcelona",
            "Real Betis", "Cadiz", "Celta de Vigo", "Getafe", "Girona", "Granada",
            "Las Palmas", "Mallorca", "Osasuna", "Real Sociedad", "Rayo Vallecano",
            "Real Madrid", "Sevilla", "Valencia", "Villarreal"
        )

        // Validar que haya suficientes equipos para formar los pares
        if (equipos.size < 2) {
            throw IllegalArgumentException("Debe haber al menos dos equipos para generar partidos")
        }

        // Crear la lista de partidos con pares de equipos y asignar días y horas aleatorias
        val partidos = mutableListOf<Partido>()

        val dias = listOf("Fri", "Sat", "Sun")
        val horas = listOf("14:00", "16:15", "18:30", "21:00")

        val equiposDisponibles = equipos.toMutableList()

        // Generar 10 pares de equipos sin repetición
        repeat(10) {
            val equipoLocal = equiposDisponibles.random()
            equiposDisponibles.remove(equipoLocal) // Evitar repetición

            val equipoVisitante = equiposDisponibles.random()
            equiposDisponibles.remove(equipoVisitante) // Evitar repetición

            val diaAleatorio = dias.random()
            val horaAleatoria = horas.random()

            partidos.add(Partido(equipoLocal, equipoVisitante, diaAleatorio, horaAleatoria))
        }

        listaPartidos.value = partidos
    }

    fun initializeLists() {
        initializePartidos()
    }

    //FUNCIONES
    fun obtenerEscudo(nombre: String): Int {
        return when (nombre) {
            "Alaves" -> R.drawable.alaves
            "Almeria" -> R.drawable.almeria
            "Athletic Club" -> R.drawable.athletic
            "Atletico de Madrid" -> R.drawable.atletico
            "FC Barcelona" -> R.drawable.barcelona
            "Real Betis" -> R.drawable.betis
            "Cadiz" -> R.drawable.cadiz
            "Celta de Vigo" -> R.drawable.celta
            "Getafe" -> R.drawable.getafe
            "Girona" -> R.drawable.girona
            "Granada" -> R.drawable.granada
            "Las Palmas" -> R.drawable.las_palmas
            "Mallorca" -> R.drawable.mallorca
            "Osasuna" -> R.drawable.osasuna
            "Real Sociedad" -> R.drawable.real_sociedad
            "Rayo Vallecano" -> R.drawable.rayo
            "Real Madrid" -> R.drawable.real_madrid
            "Sevilla" -> R.drawable.sevilla
            "Valencia" -> R.drawable.valencia
            "Villarreal" -> R.drawable.villareal
            else -> R.drawable.athletic
        }
    }

    fun obtenerMaxPosicion(pos: String): Int {
        return when (pos) {
            "DL" -> 3
            "MC" -> 3
            "DF" -> 4
            "PT" -> 1
            else -> 0
        }
    }

    fun obtenerNombreAlineacion(nombre: String): String {
        val parts = nombre.split(" ")
        return if (parts.size > 1) {
            "${parts[0][0]}. ${parts.drop(1).joinToString(" ")}"
        } else {
            nombre
        }
    }

    fun obtenerPlantilla(): String {
        return runBlocking {
            return@runBlocking postsFlow.first().map { it.texto }.joinToString("\n")
        }
    }



}