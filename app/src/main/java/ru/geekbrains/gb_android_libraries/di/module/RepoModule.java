package ru.geekbrains.gb_android_libraries.di.module;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.geekbrains.gb_android_libraries.mvp.model.api.IDataSource;
import ru.geekbrains.gb_android_libraries.mvp.model.api.INetworkStatus;
import ru.geekbrains.gb_android_libraries.mvp.model.cache.ICache;
import ru.geekbrains.gb_android_libraries.mvp.model.repo.UsersRepo;

@Module(includes = {ApiModule.class, CacheModule.class})
public class RepoModule {

    @Singleton
    @Provides
    public UsersRepo usersRepo(IDataSource api, INetworkStatus networkStatus, @Named("room") ICache cache) {
        return new UsersRepo(networkStatus, api, cache);
    }

}
