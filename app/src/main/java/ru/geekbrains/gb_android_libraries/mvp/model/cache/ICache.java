package ru.geekbrains.gb_android_libraries.mvp.model.cache;

import io.reactivex.Completable;
import io.reactivex.Single;
import ru.geekbrains.gb_android_libraries.mvp.model.entity.Repository;
import ru.geekbrains.gb_android_libraries.mvp.model.entity.User;

import java.util.List;

public interface ICache {
    Single<User> getUser(String username);
    Completable putUser(String username, User user);

    Single<List<Repository>> getUserRepos(User user);
    Completable putUserRepos(User user, List<Repository> repositories);
}
