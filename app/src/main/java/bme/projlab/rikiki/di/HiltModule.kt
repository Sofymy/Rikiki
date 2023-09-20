package bme.projlab.rikiki.di

import bme.projlab.rikiki.data.repositories.AuthRepository
import bme.projlab.rikiki.data.repositories.GameRepository
import bme.projlab.rikiki.domain.base.BaseAuthRepository
import bme.projlab.rikiki.domain.base.BaseAuthenticator
import bme.projlab.rikiki.data.utils.FirebaseAuthenticator
import bme.projlab.rikiki.data.repositories.LobbiesRepository
import bme.projlab.rikiki.data.repositories.ProfileRepository
import bme.projlab.rikiki.data.repositories.RulesRepository
import bme.projlab.rikiki.domain.base.BaseGameRepository
import bme.projlab.rikiki.domain.base.BaseLobbiesRepository
import bme.projlab.rikiki.domain.base.BaseProfileRepository
import bme.projlab.rikiki.domain.base.BaseRulesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    @Singleton
    @Provides
    fun provideAuthenticator() : BaseAuthenticator {
        return FirebaseAuthenticator()
    }

    @Singleton
    @Provides
    fun provideAuthRepository(authenticator: BaseAuthenticator): BaseAuthRepository {
        return AuthRepository(authenticator)
    }

    @Singleton
    @Provides
    fun provideLobbiesRepository(): BaseLobbiesRepository {
        return LobbiesRepository()
    }

    @Singleton
    @Provides
    fun provideProfileRepository(): BaseProfileRepository {
        return ProfileRepository()
    }

    @Singleton
    @Provides
    fun provideRulesRepository(): BaseRulesRepository {
        return RulesRepository()
    }

    @Singleton
    @Provides
    fun provideGameRepository(): BaseGameRepository {
        return GameRepository()
    }
}