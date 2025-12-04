package use_case.search.bycurrency;

import entity.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 100% Coverage Tests for SearchByCurrencyInteractor
 */
public class SearchByCurrencyInteractorTest {

    private SearchByCurrencyInteractor interactor;
    private MockDataAccess dataAccess;
    private MockPresenter presenter;

    @BeforeEach
    void setUp() {
        dataAccess = new MockDataAccess();
        presenter = new MockPresenter();
        interactor = new SearchByCurrencyInteractor(dataAccess, presenter);
    }

    /** --------------------------- TEST 1 --------------------------- */
    @Test
    void testSearchCurrency_nullInput() {
        interactor.searchCountriesByCurrency(new SearchByCurrencyInputData(null));
        assertEquals("Currency must not be empty.", presenter.errorMessage);
    }

    /** --------------------------- TEST 2 --------------------------- */
    @Test
    void testSearchCurrency_foundCountries() {
        interactor.searchCountriesByCurrency(new SearchByCurrencyInputData("Euro"));
        assertNotNull(presenter.outputCountries);
        assertEquals("Euro", presenter.outputCountries.getCurrency());
        assertEquals(1, presenter.outputCountries.getCountries().size());
    }

    /** --------------------------- TEST 3 --------------------------- */
    @Test
    void testSearchCurrency_noResults() {
        interactor.searchCountriesByCurrency(new SearchByCurrencyInputData("NON_EXISTING"));
        assertEquals("No countries found for Currency: NON_EXISTING", presenter.errorMessage);
    }

    /** --------------------------- TEST 4 --------------------------- */
    @Test
    void testListCurrencies_success() {
        interactor.listCurrencies();
        assertNotNull(presenter.outputCurrencies);
        assertTrue(presenter.outputCurrencies.getCurrencies().contains("Euro"));
        assertTrue(presenter.outputCurrencies.getCurrencies().contains("USD"));
    }

    /** --------------------------- TEST 5 --------------------------- */
    @Test
    void testListCurrencies_empty() {
        dataAccess.allCountries = new ArrayList<>(); // override
        interactor.listCurrencies();
        assertEquals("No Currencies found.", presenter.errorMessage);
    }

    /** --------------------------- TEST 6 --------------------------- */
    @Test
    void testSwitchToSaveCountryView() {
        interactor.switchToSaveCountryView();
        assertTrue(presenter.saveViewCalled);
    }

    /** --------------------------- TEST 7 --------------------------- */
    @Test
    void testSwitchToSelectedListView() {
        interactor.switchToSelectedListView();
        assertTrue(presenter.selectedListCalled);
    }

    /** ------------------------------------------------------------------
     * Mock Classes
     * ------------------------------------------------------------------ */

    private static class MockDataAccess implements SearchByCurrencyDataAccessInterface {
        List<Country> allCountries = Arrays.asList(
                new Country("France", "FRA", List.of("Euro"), "Europe", "Western", List.of("French"), List.of("Français")),
                new Country("USA", "USA", List.of("USD"), "Americas", "North", List.of("English"), List.of("English"))
        );

        @Override
        public List<Country> getAllCountries() {
            return allCountries;
        }
    }

    private static class MockPresenter implements SearchByCurrencyOutputBoundary {
        SearchByCurrencyOutputData outputCountries;
        SearchByCurrencyOutputData outputCurrencies;
        String errorMessage;
        boolean saveViewCalled = false;
        boolean selectedListCalled = false;

        @Override
        public void presentCountries(SearchByCurrencyOutputData outputData) {
            this.outputCountries = outputData;
        }

        @Override
        public void presentCurrencies(SearchByCurrencyOutputData outputData) {
            this.outputCurrencies = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        @Override
        public void switchToSaveCountryView() {
            saveViewCalled = true;
        }

        @Override
        public void switchToSelectedListView() {
            selectedListCalled = true;
        }
    }
}
