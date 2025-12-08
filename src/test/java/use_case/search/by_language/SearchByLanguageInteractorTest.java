package use_case.search.by_language;

import entity.Country;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SearchByLanguageInteractorTest {

    // Helper to build Country instances: contains placeholders for variables irrelevant to this use case interactor
    private Country makeCountry(String name, String cca3, List<String> langs, List<String> nativeNames) {
        return new Country(
                name,
                cca3,
                List.of("Currency"),
                "Region",
                "Subregion",
                langs,
                nativeNames
        );
    }

    // Minimal in-memory DAO for testing
    private static class InMemoryCountryDAO implements SearchByLanguageCountryDataAccessInterface {
        private final List<Country> countries;

        InMemoryCountryDAO(List<Country> countries) {
            this.countries = countries;
        }

        @Override
        public List<Country> getAllCountries() {
            return countries;
        }
    }

    // 1. Display language options tests

    @Test
    void successLoadLanguageOptionsTest() {
        SearchByLanguageCountryDataAccessInterface countryDAO = new InMemoryCountryDAO(
                List.of(
                        makeCountry("France", "FRA", List.of("French"), List.of("République française(France)")),
                        makeCountry("Canada", "CAN", List.of("English", "French"), List.of("Canada(Canada)", "Canada(Canada)"))
                )
        );

        SearchByLanguageOutputBoundary presenter = new SearchByLanguageOutputBoundary() {
            @Override
            public void presentLanguages(SearchByLanguageOutputData outputData) {
                Set<String> expected = new TreeSet<>(Set.of("English", "French"));
                assertEquals(expected, outputData.getLanguageOptions());
                assertNull(outputData.getSelectedLanguage());
                assertNull(outputData.getCountries());
            }

            @Override
            public void presentCountries(SearchByLanguageOutputData outputData) {
                // this should never be reached since this test doesn't execute this part of the use case
                fail("presentCountries failure is unexpected.");
            }

            @Override public void prepareFailView(String errorMessage) { fail("Use case failure is unexpected"); }

            @Override public void switchToPreviousView() {
                // this should never be reached since this test doesn't execute this part of the use case
                fail("switchToPreviousView failure is unexpected.");
            }

            @Override public void switchToSaveCountryView() {
                // this should never be reached since this test doesn't execute this part of the use case
                fail("switchToSaveCountryView failure is unexpected.");
            }
        };

        SearchByLanguageInteractor interactor = new SearchByLanguageInteractor(countryDAO, presenter);
        interactor.languageOptions();
    }

    @Test
    void failureLoadLanguageOptionsTest() {
        SearchByLanguageCountryDataAccessInterface countryDAO = new InMemoryCountryDAO(List.of());

        SearchByLanguageOutputBoundary presenter = new SearchByLanguageOutputBoundary() {
            @Override public void presentLanguages(SearchByLanguageOutputData outputData) { fail("Use case success is unexpected"); }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("No languages found.", errorMessage);
            }

            @Override
            public void presentCountries(SearchByLanguageOutputData outputData) {
                // this should never be reached since this test doesn't execute this part of the use case
                fail("presentCountries failure is unexpected.");
            }

            @Override public void switchToPreviousView() {
                // this should never be reached since this test doesn't execute this part of the use case
                fail("switchToPreviousView failure is unexpected.");
            }

            @Override public void switchToSaveCountryView() {
                // this should never be reached since this test doesn't execute this part of the use case
                fail("switchToSaveCountryView failure is unexpected.");
            }
        };

        SearchByLanguageInteractor interactor = new SearchByLanguageInteractor(countryDAO, presenter);
        interactor.languageOptions();
    }

    // 2. Executing search by language tests

    @Test
    void executionSuccessTest() {
        SearchByLanguageInputData inputData = new SearchByLanguageInputData("Georgian");

        SearchByLanguageCountryDataAccessInterface countryDAO = new InMemoryCountryDAO(
                List.of(
                        makeCountry("Georgia", "GEO", List.of("Georgian"), List.of("საქართველო"))
                )
        );

        SearchByLanguageOutputBoundary presenter = new SearchByLanguageOutputBoundary() {
            @Override public void presentLanguages(SearchByLanguageOutputData outputData) {
                // this should never be reached since this test doesn't execute this part of the use case
                fail("presentLanguages failure is unexpected.");
            }

            @Override
            public void presentCountries(SearchByLanguageOutputData outputData) {
                assertEquals("Georgian", outputData.getSelectedLanguage());
                assertEquals(1, outputData.getCountries().size());
                assertEquals("Georgia", outputData.getCountries().get(0).getName());
            }

            @Override public void prepareFailView(String errorMessage) { fail("Use case failure is unexpected"); }

            @Override public void switchToPreviousView() {
                // this should never be reached since this test doesn't execute this part of the use case
                fail("switchToPreviousView failure is unexpected.");
            }

            @Override public void switchToSaveCountryView() {
                // this should never be reached since this test doesn't execute this part of the use case
                fail("switchToSaveCountryView failure is unexpected.");
            }
        };

        SearchByLanguageInteractor interactor = new SearchByLanguageInteractor(countryDAO, presenter);
        interactor.execute(inputData);
    }

    @Test
    void executionFailureMissingLanguageInputTest() {
        SearchByLanguageInputData inputData = new SearchByLanguageInputData("");

        SearchByLanguageCountryDataAccessInterface countryDAO = new InMemoryCountryDAO(
                List.of(
                        makeCountry("Georgia", "GEO", List.of("Georgian"), List.of("საქართველო"))
                )
        );

        SearchByLanguageOutputBoundary presenter = new SearchByLanguageOutputBoundary() {
            @Override public void presentLanguages(SearchByLanguageOutputData outputData) {
                // this should never be reached since this test doesn't execute this part of the use case
                fail("presentLanguages failure is unexpected.");
            }

            @Override public void presentCountries(SearchByLanguageOutputData outputData) {
                // this should never be reached since this test doesn't execute this part of the use case
                fail("presentCountries failure is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Please select a language before searching.", errorMessage);
            }

            @Override public void switchToPreviousView() {
                // this should never be reached since this test doesn't execute this part of the use case
                fail("switchToPreviousView failure is unexpected.");
            }

            @Override public void switchToSaveCountryView() {
                // this should never be reached since this test doesn't execute this part of the use case
                fail("switchToSaveCountryView failure is unexpected.");
            }
        };

        SearchByLanguageInteractor interactor = new SearchByLanguageInteractor(countryDAO, presenter);
        interactor.execute(inputData);
    }

    @Test
    void executionFailureNullLanguageInputTest() {
        SearchByLanguageInputData inputData = new SearchByLanguageInputData(null);

        SearchByLanguageCountryDataAccessInterface countryDAO = new InMemoryCountryDAO(
                List.of(makeCountry("Germany", "DEU", List.of("German"), List.of("Bundesrepublik Deutschland(Deutschland)")))
        );

        SearchByLanguageOutputBoundary presenter = new SearchByLanguageOutputBoundary() {
            @Override public void presentLanguages(SearchByLanguageOutputData outputData) {
                // this should never be reached since this test doesn't execute this part of the use case
                fail("presentLanguages failure is unexpected.");
            }

            @Override public void presentCountries(SearchByLanguageOutputData outputData) {
                // this should never be reached since this test doesn't execute this part of the use case
                fail("presentCountries failure is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Please select a language before searching.", errorMessage);
            }

            @Override public void switchToPreviousView() {
                // this should never be reached since this test doesn't execute this part of the use case
                fail("switchToPreviousView failure is unexpected.");
            }

            @Override public void switchToSaveCountryView() {
                // this should never be reached since this test doesn't execute this part of the use case
                fail("switchToSaveCountryView failure is unexpected.");
            }
        };

        SearchByLanguageInteractor interactor = new SearchByLanguageInteractor(countryDAO, presenter);
        interactor.execute(inputData);
    }

    @Test
    void executionFailureNoMatchingCountriesTest() {
        SearchByLanguageInputData inputData = new SearchByLanguageInputData("Spanish");

        SearchByLanguageCountryDataAccessInterface countryDAO = new InMemoryCountryDAO(
                List.of(
                        makeCountry("Japan", "JPN", List.of("Japanese"), List.of("日本(日本)"))
                )
        );

        SearchByLanguageOutputBoundary presenter = new SearchByLanguageOutputBoundary() {
            @Override public void presentLanguages(SearchByLanguageOutputData outputData) {
                // this should never be reached since this test doesn't execute this part of the use case
                fail("presentLanguages failure is unexpected.");
            }

            @Override public void presentCountries(SearchByLanguageOutputData outputData) {
                // this should never be reached since this test doesn't execute this part of the use case
                fail("presentCountries failure is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("No countries found for language: Spanish", errorMessage);
            }

            @Override public void switchToPreviousView() {
                // this should never be reached since this test doesn't execute this part of the use case
                fail("switchToPreviousView failure is unexpected.");
            }

            @Override public void switchToSaveCountryView() {
                // this should never be reached since this test doesn't execute this part of the use case
                fail("switchToSaveCountryView failure is unexpected.");
            }
        };

        SearchByLanguageInteractor interactor = new SearchByLanguageInteractor(countryDAO, presenter);
        interactor.execute(inputData);
    }

    @Test
    void executionFailureCountryWithNullLanguageListTest() {
        SearchByLanguageInputData inputData = new SearchByLanguageInputData("French");

        SearchByLanguageCountryDataAccessInterface countryDAO = new InMemoryCountryDAO(
                List.of(makeCountry("France", "FRA", null, List.of("République française(France)")))
        );

        SearchByLanguageOutputBoundary presenter = new SearchByLanguageOutputBoundary() {
            @Override public void presentLanguages(SearchByLanguageOutputData outputData) {
                // this should never be reached since this test doesn't execute this part of the use case
                fail("presentLanguages failure is unexpected.");
            }

            @Override public void presentCountries(SearchByLanguageOutputData outputData) {
                // this should never be reached since this test doesn't execute this part of the use case
                fail("presentCountries failure is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("No countries found for language: French", errorMessage);
            }

            @Override public void switchToPreviousView() {
                // this should never be reached since this test doesn't execute this part of the use case
                fail("switchToPreviousView failure is unexpected.");
            }

            @Override public void switchToSaveCountryView() {
                // this should never be reached since this test doesn't execute this part of the use case
                fail("switchToSaveCountryView failure is unexpected.");
            }
        };

        SearchByLanguageInteractor interactor = new SearchByLanguageInteractor(countryDAO, presenter);
        interactor.execute(inputData);
    }

    // 3. View-switching test

    @Test
    void successSwitchToPreviousViewTest() {
        final boolean[] called = {false};

        SearchByLanguageOutputBoundary presenter = new SearchByLanguageOutputBoundary() {
            @Override public void presentLanguages(SearchByLanguageOutputData outputData) {
                // this should never be reached since this test doesn't execute this part of the use case
                fail("presentLanguages failure is unexpected.");
            }

            @Override public void presentCountries(SearchByLanguageOutputData outputData) {
                // this should never be reached since this test doesn't execute this part of the use case
                fail("presentCountries failure is unexpected.");
            }

            @Override public void prepareFailView(String errorMessage) { fail("Use case failure is unexpected"); }

            @Override public void switchToSaveCountryView() {
                // this should never be reached since this test doesn't execute this part of the use case
                fail("switchToSaveCountryView failure is unexpected.");
            }

            @Override
            public void switchToPreviousView() {
                called[0] = true;
            }
        };

        SearchByLanguageInteractor interactor = new SearchByLanguageInteractor(new InMemoryCountryDAO(List.of()), presenter);
        interactor.switchToPreviousView();
        assertTrue(called[0]);
    }

    @Test
    void successSwitchToSaveCountryViewTest() {
        final boolean[] called = {false};

        SearchByLanguageOutputBoundary presenter = new SearchByLanguageOutputBoundary() {
            @Override public void presentLanguages(SearchByLanguageOutputData outputData) {
                // this should never be reached since this test doesn't execute this part of the use case
                fail("presentLanguages failure is unexpected.");
            }

            @Override public void presentCountries(SearchByLanguageOutputData outputData) {
                // this should never be reached since this test doesn't execute this part of the use case
                fail("presentCountries failure is unexpected.");
            }

            @Override public void prepareFailView(String errorMessage) { fail("Use case failure is unexpected"); }


            @Override public void switchToPreviousView() {
                // this should never be reached since this test doesn't execute this part of the use case
                fail("switchToPreviousView failure is unexpected.");
            }

            @Override
            public void switchToSaveCountryView() {
                called[0] = true;
            }
        };

        SearchByLanguageInteractor interactor = new SearchByLanguageInteractor(new InMemoryCountryDAO(List.of()), presenter);
        interactor.switchToSaveCountryView();
        assertTrue(called[0]);
    }

}
