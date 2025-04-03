package org.how_wow.infrastructure.ui.presenter.utils;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

public class SwingUtils {

    public static void executeAsync(Runnable task, Runnable onComplete) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                task.run();
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    if (onComplete != null) {
                        SwingUtilities.invokeLater(onComplete);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE)
                    );
                }
            }
        }.execute();
    }

    public static void executeAsync(Runnable task) {
        executeAsync(task, null);
    }

}
