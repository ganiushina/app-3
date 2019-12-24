package ru.geekbrains.gb_android_libraries.mvp.model.cache;

import io.paperdb.Paper;
import io.reactivex.Completable;
import io.reactivex.Single;
import ru.geekbrains.gb_android_libraries.mvp.model.entity.Repository;
import ru.geekbrains.gb_android_libraries.mvp.model.entity.User;

import java.util.List;

public class PaperCache implements ICache {

    @Override
    public Single<User> getUser(String username) {
        if (!Paper.book("users").contains(username)) {
            return Single.error(new RuntimeException("No such user in cache"));
        }
        return Single.fromCallable(() -> Paper.book("users").read(username));
    }

    @Override
    public Completable putUser(String username, User user) {
        return Completable.fromAction(() -> Paper.book("users").write(username, user));
    }

    @Override
    public Single<List<Repository>> getUserRepos(User user) {
        if (!Paper.book("repos").contains(user.getLogin())) {
            return Single.error(new RuntimeException("No repos for such user in cache"));
        }
        return Single.fromCallable(() -> Paper.book("repos").read(user.getLogin()));
    }

    @Override
    public Completable putUserRepos(User user, List<Repository> repositories) {
        return Completable.fromAction(() -> Paper.book("repos").write(user.getLogin(), repositories));
    }
}
