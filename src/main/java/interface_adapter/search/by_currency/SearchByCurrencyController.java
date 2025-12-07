package interface_adapter.search.by_currency;

import use_case.search.bycurrency.*;

import java.awt.Desktop;
import java.net.URI;
import javax.swing.JOptionPane;


/**
 * Controller for the Search by Region use case.
 * Called by the view in response to user actions.
 */
public class SearchByCurrencyController {

    private final SearchByCurrencyInputBoundary interactor;

    public SearchByCurrencyController(SearchByCurrencyInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void loadCurrencies() {
        interactor.listCurrencies();
    }

//    public void onCurrencySelected(String currency) {
//        SearchByCurrencyInputData inputData = new SearchByCurrencyInputData(currency);
//    }
    public void onCurrencySelected(String currency) {
        if (currency != null) {
            interactor.searchCountriesByCurrency(new SearchByCurrencyInputData(currency));
        }
    }


    public void onSearch(String currency) {
        System.out.println("onSearch clicked! Currency: " + currency);
        if (currency == null) {
            interactor.searchCountriesByCurrency(
                    new SearchByCurrencyInputData(null));
            return;
        }

        SearchByCurrencyInputData inputData =
                new SearchByCurrencyInputData(currency);
        interactor.searchCountriesByCurrency(inputData);

    }

    public void onExchangeRateClicked(String currency) {
        if (currency == null || currency.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No currency selected.");
            return;
        }

        try {
            // XE requires ISO currency codes like USD, EUR, etc.
            // Ensure the code is uppercase and trimmed
            String currencyCode = currency.trim().toUpperCase();

            // Construct a URL that XE accepts
            String url = "https://www.xe.com/currencyconverter/convert/";

            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
            } else {
                JOptionPane.showMessageDialog(null, "Cannot open browser on this system.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to open browser: " + e.getMessage());
        }
    }


    public void onAddCountryButtonClicked() {
        interactor.switchToSaveCountryView();
    }

    public void onBackToSelectedListButtonClicked() {
        interactor.switchToSelectedListView();
    }

}
