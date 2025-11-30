package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();

        JFrame application = appBuilder
                .addSearchesView()
                .addSearchByLanguageView()
                .addSearchByRegionView()
                .addSearchByCurrencyView()
                .addSaveCountryView()
                .addSaveCountryUseCase()
                .addLoginSignUpView()
                .addLoginUseCase()
                .addViewSelectedList() // must be before ViewSavedList
                .addViewSavedLists()
                .addSignUpUseCase()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
