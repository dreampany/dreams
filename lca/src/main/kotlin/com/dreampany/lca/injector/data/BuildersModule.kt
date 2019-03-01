package com.dreampany.lca.injector.data

import android.content.Context
import com.dreampany.frame.injector.data.FrameModule
import com.dreampany.frame.misc.Remote
import com.dreampany.frame.misc.Room
import com.dreampany.lca.data.misc.*
import com.dreampany.lca.data.source.api.*
import com.dreampany.lca.data.source.dao.*
import com.dreampany.lca.data.source.remote.*
import com.dreampany.lca.data.source.room.*
import com.dreampany.lca.injector.vm.ViewModelModule
import com.dreampany.lca.misc.*
import com.dreampany.network.manager.NetworkManager
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton


/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */

@Module(includes = [FrameModule::class, DatabaseModule::class, SupportModule::class, ViewModelModule::class])
class BuildersModule {

    @Singleton
    @Provides
    @Room
    fun provideCoinRoomDataSource(mapper: CoinMapper,
                                  dao: CoinDao,
                                  quoteDao: QuoteDao): CoinDataSource {
        return CoinRoomDataSource(mapper, dao, quoteDao)
    }

    @Singleton
    @Provides
    @Room
    fun providePriceRoomDataSource(mapper: PriceMapper,
                                   dao: PriceDao): PriceDataSource {
        return PriceRoomDataSource(mapper, dao)
    }

    @Singleton
    @Provides
    @Room
    fun provideExchangeRoomDataSource(mapper: ExchangeMapper,
                                      dao: ExchangeDao): ExchangeDataSource {
        return ExchangeRoomDataSource(mapper, dao)
    }

    @Singleton
    @Provides
    @Room
    fun provideMarketRoomDataSource(mapper: MarketMapper,
                                    dao: MarketDao): MarketDataSource {
        return MarketRoomDataSource(mapper, dao)
    }

    @Singleton
    @Provides
    @Room
    fun provideGraphRoomDataSource(mapper: GraphMapper,
                                   dao: GraphDao): GraphDataSource {
        return GraphRoomDataSource(mapper, dao)
    }

    @Singleton
    @Provides
    @Room
    fun provideNewsRoomDataSource(mapper: NewsMapper,
                                  dao: NewsDao): NewsDataSource {
        return NewsRoomDataSource(mapper, dao)
    }

    @Singleton
    @Provides
    @Room
    fun provideIcoRoomDataSource(mapper: IcoMapper,
                                 dao: IcoDao): IcoDataSource {
        return IcoRoomDataSource(mapper, dao)
    }

    @Singleton
    @Provides
    @Room
    fun provideCoinAlertRoomDataSource(mapper: CoinAlertMapper,
                                       dao: CoinAlertDao): CoinAlertDataSource {
        return CoinAlertRoomDataSource(mapper, dao)
    }

    @Singleton
    @Provides
    @Remote
    fun provideCoinRemoteDataSource(network: NetworkManager,
                                    mapper: CoinMapper,
                                    service: CmcService): CoinDataSource {
        return CoinRemoteDataSource(network, mapper, service)
    }

    @Singleton
    @Provides
    @Remote
    fun provideExchangeRemoteDataSource(network: NetworkManager,
                                        mapper: ExchangeMapper,
                                        service: CryptoCompareExchangeService): ExchangeDataSource {
        return ExchangeRemoteDataSource(network, mapper, service)
    }

    @Singleton
    @Provides
    @Remote
    fun provideMarketRemoteDataSource(network: NetworkManager,
                                      mapper: MarketMapper,
                                      service: CryptoCompareMarketService): MarketDataSource {
        return MarketRemoteDataSource(network, mapper, service)
    }

    @Singleton
    @Provides
    @Remote
    fun provideGraphRemoteDataSource(network: NetworkManager,
                                     mapper: GraphMapper,
                                     service: CoinMarketCapGraphService): GraphDataSource {
        return GraphRemoteDataSource(network, mapper, service)
    }

    @Singleton
    @Provides
    @Remote
    fun provideNewsRemoteDataSource(network: NetworkManager,
                                    mapper: NewsMapper,
                                    service: CryptoCompareNewsService): NewsDataSource {
        return NewsRemoteDataSource(network, mapper, service)
    }

    @Singleton
    @Provides
    @Remote
    fun provideIcoRemoteDataSource(network: NetworkManager,
                                   mapper: IcoMapper,
                                   service: IcoService): IcoDataSource {
        return IcoRemoteDataSource(network, mapper, service)
    }

    @Provides
    @Singleton
    fun provideHttpClient(context: Context): OkHttpClient {
/*        val cacheSize = 10 * 1024 * 1024 // 10 MB
        val httpCacheDirectory = File(context.getCacheDir(), "http-cache")
        val cache = Cache(httpCacheDirectory, cacheSize.toLong())

        val networkCacheInterceptor = Interceptor { chain ->
            val response = chain.proceed(chain.request())

            var cacheControl = CacheControl.Builder()
                    .maxAge(1, TimeUnit.MINUTES)
                    .build()

            response.newBuilder()
                    .header("Cache-Control", cacheControl.toString())
                    .build()
        }
*/
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
                //.cache(cache)
                //.addNetworkInterceptor(networkCacheInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()

        return httpClient
    }

/*    @Singleton
    @Provides
    @CoinMarketCap
    fun provideCoinMarketCapRetrofit(client: OkHttpClient): Retrofit {
        val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(Constants.Api.CoinMarketCapApiUrl)
                //.addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }*/

    @Singleton
    @Provides
    @CoinMarketCap
    fun provideCoinMarketCapRetrofit(client: OkHttpClient): Retrofit {
        val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(Constants.Api.CMCApiUrlV1)
                //.addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }

    @Singleton
    @Provides
    @CoinMarketCapGraph
    fun provideCoinMarketCapGraphRetrofit(client: OkHttpClient): Retrofit {
        val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(Constants.Api.CoinMarketCapGraphApiUrl)
                //.addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }

    @Singleton
    @Provides
    @CryptoCompare
    fun provideCryptoCompareRetrofit(client: OkHttpClient): Retrofit {
        val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(Constants.Api.CryptoCompareApiUrl)
                //.addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }

    @Singleton
    @Provides
    @IcoWatchList
    fun provideIcoWatchListRetrofit(client: OkHttpClient): Retrofit {
        val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(Constants.Api.IcoWatchListApiUrl)
                //.addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }

/*    @Provides
    fun provideCoinMarketCapService(@CoinMarketCap retrofit: Retrofit): CoinMarketCapService {
        return retrofit.create(CoinMarketCapService::class.java);
    }*/

    @Provides
    fun provideCoinMarketCapService(@CoinMarketCap retrofit: Retrofit): CmcService {
        return retrofit.create(CmcService::class.java);
    }

    @Provides
    fun provideCoinMarketCapGraphService(@CoinMarketCapGraph retrofit: Retrofit): CoinMarketCapGraphService {
        return retrofit.create(CoinMarketCapGraphService::class.java);
    }

    @Provides
    fun provideCryptoCompareExchangeService(@CryptoCompare retrofit: Retrofit): CryptoCompareExchangeService {
        return retrofit.create(CryptoCompareExchangeService::class.java);
    }

    @Provides
    fun provideCryptoCompareMarketService(@CryptoCompare retrofit: Retrofit): CryptoCompareMarketService {
        return retrofit.create(CryptoCompareMarketService::class.java);
    }

    @Provides
    fun provideCryptoCompareNewsService(@CryptoCompare retrofit: Retrofit): CryptoCompareNewsService {
        return retrofit.create(CryptoCompareNewsService::class.java);
    }

    @Provides
    fun provideIcoService(@IcoWatchList retrofit: Retrofit): IcoService {
        return retrofit.create(IcoService::class.java);
    }

/*    @Singleton
    @Provides
    fun provideExecutors(): AppExecutors {
        return AppExecutors()
    }*/

    @Singleton
    @Provides
    fun provideCurrencyFormatter(context: Context): CurrencyFormatter {
        return CurrencyFormatter(context)
    }
}
