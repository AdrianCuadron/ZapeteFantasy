package com.cuadrondev.zapetefantasy.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cuadrondev.zapetefantasy.model.datastore.PreferencesDataStore
import com.cuadrondev.zapetefantasy.model.entities.User
import com.cuadrondev.zapetefantasy.model.repositories.UserDataRepository
import com.cuadrondev.zapetefantasy.model.repositories.UserTeamRepository
import com.cuadrondev.zapetefantasy.utils.LanguageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
) : ViewModel() {
    fun checkUserExist(username: String): String {
        return userDataRepository.checkUsernameExists(username)
    }

    suspend fun checkCredentials(username: String, password: String): Boolean {
        return userDataRepository.checkCredentials(username, password)
    }

    fun createUser(user: User) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userDataRepository.addUserData(user)
                userDataRepository.changeUserLanguage(user.username, "es")
                userDataRepository.changeUserCoin(user.username, "euro")
            }
        }
    }


}