package ru.geekbrains.gb_android_libraries.di.module;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import ru.geekbrains.gb_android_libraries.mvp.model.cache.ICache;
import ru.geekbrains.gb_android_libraries.mvp.model.cache.PaperCache;
import ru.geekbrains.gb_android_libraries.mvp.model.cache.RealmCache;
import ru.geekbrains.gb_android_libraries.mvp.model.cache.RoomCache;

@Module
public class CacheModule {

    @Named("room")
    @Provides
    public ICache roomCache() {
        return new RoomCache();
    }

    @Named("realm")
    @Provides
    public ICache realmCache() {
        return new RealmCache();
    }

    @Named("paper")
    @Provides
    public ICache paperCache() {
        return new PaperCache();
    }
}
