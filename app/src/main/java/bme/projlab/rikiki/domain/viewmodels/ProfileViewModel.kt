package bme.projlab.rikiki.domain.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import bme.projlab.rikiki.domain.base.BaseProfileRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bme.projlab.rikiki.data.entities.Lobby
import bme.projlab.rikiki.data.entities.User
import bme.projlab.rikiki.domain.base.BaseLobbiesRepository
import bme.projlab.rikiki.domain.responses.LobbiesResponse
import bme.projlab.rikiki.domain.responses.ResourceResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: BaseProfileRepository
): ViewModel(){
    private var _profile = mutableStateOf(User())
    val profile: State<User> = _profile

    init {
        profileRepository.getProfile()
            .onEach { resource ->
                when (resource) {
                    is ResourceResponse.Error -> {
                    }

                    is ResourceResponse.Success -> {
                        if(resource.data!=null)
                            _profile.value = resource.data
                    }

                }
            }.launchIn(viewModelScope)
    }
}