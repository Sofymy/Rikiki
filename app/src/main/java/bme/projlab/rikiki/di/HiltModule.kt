package bme.projlab.rikiki.di

import bme.projlab.rikiki.data.AuthRepository
import bme.projlab.rikiki.domain.base.BaseAuthRepository
import bme.projlab.rikiki.domain.base.BaseAuthenticator
import bme.projlab.rikiki.data.FirebaseAuthenticator
import bme.projlab.rikiki.data.LobbiesRepository
import bme.projlab.rikiki.data.ProfileRepository
import bme.projlab.rikiki.data.RulesRepository
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
}