package ru.geekbrains.gb_android_libraries.mvp.presenter;

import io.reactivex.subjects.PublishSubject;
import ru.geekbrains.gb_android_libraries.mvp.view.ReposotoryItemView;

public interface IRepositoryListPresenter {
    void bind(ReposotoryItemView view);
    int getCount();
    PublishSubject<ReposotoryItemView> getClickSubject();
}
