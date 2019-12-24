package ru.geekbrains.gb_android_libraries.mvp.model.cache;

import io.paperdb.Paper;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import ru.geekbrains.gb_android_libraries.mvp.model.entity.Repository;
import ru.geekbrains.gb_android_libraries.mvp.model.entity.User;
import ru.geekbrains.gb_android_libraries.mvp.model.entity.realm.RealmRepository;
import ru.geekbrains.gb_android_libraries.mvp.model.entity.realm.RealmUser;
import ru.geekbrains.gb_android_libraries.mvp.model.entity.room.RoomRepository;
import ru.geekbrains.gb_android_libraries.mvp.model.entity.room.RoomUser;
import ru.geekbrains.gb_android_libraries.mvp.model.entity.room.db.Database;

import java.util.ArrayList;
import java.util.List;

public class RealmCache implements ICache {

    @Override
    public Single<User> getUser(String username) {
        return Single.create(emitter -> {
            Realm realm = Realm.getDefaultInstance();
            RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", username).findFirst();
            if (realmUser == null) {
                emitter.onError(new RuntimeException("No such user in cache"));
            } else {
                emitter.onSuccess(new User(realmUser.getLogin(), realmUser.getAvatarUrl(), realmUser.getReposUrl()));
            }
            realm.close();
        }).subscribeOn(Schedulers.io()).cast(User.class);
    }

    @Override
    public Completable putUser(String username, User user) {
        return Completable.fromAction(() -> {
                    Realm realm = Realm.getDefaultInstance();
                    RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", username).findFirst();
                    if (realmUser == null) {
                        realm.executeTransaction(innerRealm -> {
                            RealmUser newRealmUser = innerRealm.createObject(RealmUser.class, username);
                            newRealmUser.setAvatarUrl(user.getAvatarUrl());
                            newRealmUser.setReposUrl(user.getReposUrl());
                        });
                    } else {
                        realm.executeTransaction(innerRealm -> {
                            realmUser.setAvatarUrl(user.getAvatarUrl());
                            realmUser.setReposUrl(user.getReposUrl());
                        });
                    }
                    realm.close();
                }
        );
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
            Realm realm = Realm.getDefaultInstance();
            RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", user.getLogin()).findFirst();

            if (realmUser == null) {
                realm.executeTransaction(innerRealm -> {
                    RealmUser newRealmUser = innerRealm.createObject(RealmUser.class, user.getLogin());
                    newRealmUser.setAvatarUrl(user.getAvatarUrl());
                    newRealmUser.setReposUrl(user.getReposUrl());
                });
            }

            realm.executeTransaction(innerRealm -> {
                realmUser.getRepos().deleteAllFromRealm();
                for (Repository repository : repositories) {
                    RealmRepository realmRepository = innerRealm.createObject(RealmRepository.class, repository.getId());
                    realmRepository.setName(repository.getName());
                    realmUser.getRepos().add(realmRepository);
                }
            });
            realm.close();
        });
    }
}
