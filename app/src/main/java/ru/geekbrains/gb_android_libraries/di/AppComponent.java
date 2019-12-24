package ru.geekbrains.gb_android_libraries.di;

import javax.inject.Singleton;

import dagger.Component;
import ru.geekbrains.gb_android_libraries.di.module.AppModule;
import ru.geekbrains.gb_android_libraries.di.module.RepoModule;
import ru.geekbrains.gb_android_libraries.mvp.presenter.MainPresenter;
import ru.geekbrains.gb_android_libraries.ui.activity.MainActivity;

@Singleton
@Component(modules = {
        RepoModule.class,
        AppModule.class
})

public interface AppComponent {
    void inject(MainPresenter presenter);
    void inject(MainActivity mainActivity);
}
