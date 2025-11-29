package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppBuilder appBuilder = new AppBuilder();

            JFrame application = appBuilder
                    .addLoginSignUpView()
                    .addLoginUseCase()
                    .addSignUpUseCase()

                    .addProfileUseCase()
                    .addEditProfileUseCase()

                    .addSaveCountryView()
                    .addSaveCountryUseCase()

                    .addViewSelectedList()
                    .addViewSavedLists()

                    .addSearchByRegionView()
                    .addSearchByRegionUseCase()

                    .addSearchByCurrencyView()
                    .addSearchByCurrencyUseCase()

                    .addSearchByLanguageView()
                    .addSearchByLanguageUseCase()

                    .addSearchesView()

                    .build();

            application.pack();
            application.setLocationRelativeTo(null);
            application.setVisible(true);
        });
    }
}