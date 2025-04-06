package org.how_wow.infrastructure.ui.view.dialogs;

import org.how_wow.infrastructure.ui.presenters.dialogs.DialogPresenter;

public interface DialogView<T, V> {
    void setData(T data);

    V getData();

    void setSaveButtonText(String text);

    void setTitle(String title);

    void showDialog();

    void closeDialog();

    void showError(String message);

    void showSuccess(String message);

    void setPresenter(DialogPresenter presenter);
}
