package ru.geekbrains.gb_android_libraries.di.module;

import dagger.Module;
import dagger.Provides;
import ru.geekbrains.gb_android_libraries.App;

@Module
public class AppModule {
    private App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    public App app(){
        return app;
    }
}
