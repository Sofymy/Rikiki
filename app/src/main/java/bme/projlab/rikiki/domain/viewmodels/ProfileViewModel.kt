package bme.projlab.rikiki.domain.viewmodels

import bme.projlab.rikiki.domain.base.BaseProfileRepository

import androidx.lifecycle.ViewModel
import bme.projlab.rikiki.domain.base.BaseLobbiesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: BaseProfileRepository
): ViewModel(){

}