package ru.geekbrains.gb_android_libraries.mvp.model.repo;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import ru.geekbrains.gb_android_libraries.mvp.model.api.IDataSource;
import ru.geekbrains.gb_android_libraries.mvp.model.api.INetworkStatus;
import ru.geekbrains.gb_android_libraries.mvp.model.cache.ICache;
import ru.geekbrains.gb_android_libraries.mvp.model.entity.Repository;
import ru.geekbrains.gb_android_libraries.mvp.model.entity.User;

import java.util.List;

public class UsersRepo {

    private INetworkStatus networkStatus;
    private IDataSource api;
    private ICache cache;

    public UsersRepo(INetworkStatus networkStatus, IDataSource api, ICache cache) {
        this.networkStatus = networkStatus;
        this.api = api;
        this.cache = cache;
    }

    public Single<User> getUser(String username) {
        if (networkStatus.isOnline()) {
            return api.getUser(username)
                    .subscribeOn(Schedulers.io())
                    .map(user -> {
                        cache.putUser(username, user).subscribe();
                        return user;
                    });
        } else {
            return cache.getUser(username);
        }
    }

    public Single<List<Repository>> getUserRepos(User user) {
        if (networkStatus.isOnline()) {
            return api.getUserRepos(user.getReposUrl()).subscribeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .map(repos -> {
                        cache.putUserRepos(user, repos).subscribe();
                        return repos;
                    });
        } else {
            return cache.getUserRepos(user);
        }
    }
}
