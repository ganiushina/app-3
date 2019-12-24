package ru.geekbrains.gb_android_libraries.mvp.view;

import android.support.annotation.NonNull;

public interface ReposotoryItemView {
    int getPos();
    void setTitle(@NonNull String title);
}
