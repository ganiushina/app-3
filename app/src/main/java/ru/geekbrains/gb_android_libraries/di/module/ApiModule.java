package ru.geekbrains.gb_android_libraries.di.module;


import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.geekbrains.gb_android_libraries.mvp.model.api.IDataSource;
import ru.geekbrains.gb_android_libraries.mvp.model.api.INetworkStatus;
import ru.geekbrains.gb_android_libraries.ui.NetworkStatus;

@Module
public class ApiModule {

    @Provides
    public IDataSource api(Gson gson, @Named("clientLogging") OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(IDataSource.class);
    }

    @Provides
    public Gson gson() {
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).excludeFieldsWithoutExposeAnnotation().create();
    }

    @Named("client")
    @Provides
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder().build();
    }


    @Provides
    public HttpLoggingInterceptor loggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    @Named("clientLogging")
    @Provides
    public OkHttpClient okHttpClientLogging(HttpLoggingInterceptor interceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }

    @Provides
    public INetworkStatus networkStatus() {
        return new NetworkStatus();
    }
}


//ПЕРЕРЫВ 10 МИНУТ