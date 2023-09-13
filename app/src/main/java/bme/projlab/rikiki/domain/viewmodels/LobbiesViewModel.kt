package bme.projlab.rikiki.domain.viewmodels

import androidx.lifecycle.ViewModel
import bme.projlab.rikiki.domain.base.BaseLobbiesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LobbiesViewModel @Inject constructor(
    private val lobbiesRepository: BaseLobbiesRepository
): ViewModel(){

}