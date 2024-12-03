package com.myfitnesspal.nyt

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.mockito.Mockito.mock

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class]
)
object TestNetworkModule {

    @Provides
    fun provideMockApiService(): ArticleApi {
        return mock(ArticleApi::class.java)
    }

    @Provides
    fun provideMockRepository(articleApi: ArticleApi): ArticleRepository {
        return ArticleRepositoryImpl(articleApi)
    }
}