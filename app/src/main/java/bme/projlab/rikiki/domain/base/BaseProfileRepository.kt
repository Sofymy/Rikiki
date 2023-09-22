package bme.projlab.rikiki.domain.base

import bme.projlab.rikiki.data.entities.User
import bme.projlab.rikiki.domain.responses.ResourceResponse
import kotlinx.coroutines.flow.Flow

interface BaseProfileRepository {
    fun getProfile(): Flow<ResourceResponse<User>>
}