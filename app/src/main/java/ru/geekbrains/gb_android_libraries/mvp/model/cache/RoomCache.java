package ru.geekbrains.gb_android_libraries.mvp.model.cache;

import io.paperdb.Paper;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import ru.geekbrains.gb_android_libraries.mvp.model.entity.Repository;
import ru.geekbrains.gb_android_libraries.mvp.model.entity.User;
import ru.geekbrains.gb_android_libraries.mvp.model.entity.room.RoomRepository;
import ru.geekbrains.gb_android_libraries.mvp.model.entity.room.RoomUser;
import ru.geekbrains.gb_android_libraries.mvp.model.entity.room.db.Database;

import java.util.ArrayList;
import java.util.List;

public class RoomCache implements ICache {

    @Override
    public Single<User> getUser(String username) {
        return Single.create(emitter -> {
            RoomUser roomUser = Database.getInstance().getUserDao()
                    .findByLogin(username);

            if (roomUser == null) {
                emitter.onError(new RuntimeException("No such user in cache"));
            } else {
                emitter.onSuccess(new User(roomUser.getLogin(), roomUser.getAvatarUrl(), roomUser.getReposUrl()));
            }
        }).subscribeOn(Schedulers.io()).cast(User.class);
    }

    @Override
    public Completable putUser(String username, User user) {
        return Completable.fromAction(() -> {
            RoomUser roomUser = Database.getInstance().getUserDao()
                    .findByLogin(username);

            if (roomUser == null) {
                roomUser = new RoomUser();
                roomUser.setLogin(username);
            }

            roomUser.setAvatarUrl(user.getAvatarUrl());
            roomUser.setReposUrl(user.getReposUrl());

            Database.getInstance().getUserDao()
                    .insert(roomUser);
        });
    }

    @Override
    public Single<List<Repository>> getUserRepos(User user) {
        return Single.create(emitter -> {
            RoomUser roomUser = Database.getInstance()
                    .getUserDao()
                    .findByLogin(user.getLogin());

            if (roomUser == null) {
                emitter.onError(new RuntimeException("No such user in cache"));
            } else {
                List<RoomRepository> roomRepositories = Database.getInstance().getRepositoryDao()
                        .getAll();

                List<Repository> repos = new ArrayList<>();
                for (RoomRepository roomRepository : roomRepositories) {
                    repos.add(new Repository(roomRepository.getId(), roomRepository.getName()));
                }

                emitter.onSuccess(repos);
            }
        }).subscribeOn(Schedulers.io()).cast((Class<List<Repository>>) (Class) List.class);
    }

    @Override
    public Completable putUserRepos(User user, List<Repository> repositories) {
        return Completable.fromAction(() -> {
            RoomUser roomUser = Database.getInstance().getUserDao()
                    .findByLogin(user.getLogin());

            if (roomUser == null) {
                roomUser = new RoomUser();
                roomUser.setLogin(user.getLogin());
                roomUser.setAvatarUrl(user.getAvatarUrl());
                roomUser.setReposUrl(user.getReposUrl());
                Database.getInstance()
                        .getUserDao()
                        .insert(roomUser);
            }


            if (!repositories.isEmpty()) {
                List<RoomRepository> roomRepositories = new ArrayList<>();
                for (Repository repository : repositories) {
                    RoomRepository roomRepository = new RoomRepository(repository.getId(), repository.getName(), user.getLogin());
                    roomRepositories.add(roomRepository);
                }

                Database.getInstance()
                        .getRepositoryDao()
                        .insert(roomRepositories);
            }
        });
    }
}
