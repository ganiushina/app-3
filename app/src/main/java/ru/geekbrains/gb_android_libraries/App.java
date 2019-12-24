package ru.geekbrains.gb_android_libraries;

import android.app.Application;

import io.paperdb.Paper;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import ru.geekbrains.gb_android_libraries.di.AppComponent;
import ru.geekbrains.gb_android_libraries.di.DaggerAppComponent;
import ru.geekbrains.gb_android_libraries.di.module.AppModule;
import ru.geekbrains.gb_android_libraries.mvp.model.entity.room.db.Database;
import timber.log.Timber;

public class App extends Application {

    static private App instance;

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Timber.plant(new Timber.DebugTree());
        Database.create(this);
        Paper.init(this);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }


    public static App getInstance() {
        return instance;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
