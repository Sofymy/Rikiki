package bme.projlab.rikiki.domain.viewmodels

import androidx.lifecycle.ViewModel
import bme.projlab.rikiki.domain.base.BaseProfileRepository
import bme.projlab.rikiki.domain.base.BaseRulesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RulesViewModel @Inject constructor(
    private val rulesRepository: BaseRulesRepository
): ViewModel(){

}