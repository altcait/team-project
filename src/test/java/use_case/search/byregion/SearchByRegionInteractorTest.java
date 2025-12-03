package use_case.search.byregion;

import entity.Country;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for SearchByRegionInteractor.
 * We use simple in-memory stubs for the DAO and presenter
 * to hit every branch in the interactor.
 */
public class SearchByRegionInteractorTest {

    /**
     * Small helper so we don't repeat the 7-arg Country constructor everywhere.
     * We only really care about name / region / subregion in these tests,
     * so the other fields can be dummy values.
     */
    private Country makeCountry(String name, String region, String subregion) {
        String cca3 = name.substring(0, Math.min(3, name.length())).toUpperCase();
        List<String> emptyList = Collections.emptyList();
        return new Country(
                name,
                cca3,
                emptyList,   // currencies
                region,
                subregion,
                emptyList,   // languages
                emptyList    // nativeNames
        );
    }

    /** Simple DAO stub that just returns a fixed list of countries. */
    private static class InMemoryDao implements SearchByRegionDataAccessInterface {
        private final List<Country> countries;

        InMemoryDao(List<Country> countries) {
            this.countries = countries;
        }

        @Override
        public List<Country> getAllCountries() {
            return countries;
        }
    }

    /**
     * Presenter stub that just records what was called.
     * This lets us assert on errors / outputs / switches.
     */
    private static class RecordingPresenter implements SearchByRegionOutputBoundary {
        String lastError;
        SearchByRegionOutputData regionsOutput;
        SearchByRegionOutputData subregionsOutput;
        SearchByRegionOutputData countriesOutput;
        boolean switchedToSave;
        boolean switchedToSelected;

        @Override
        public void presentCountries(SearchByRegionOutputData outputData) {
            this.countriesOutput = outputData;
        }

        @Override
        public void presentSubregions(SearchByRegionOutputData outputData) {
            this.subregionsOutput = outputData;
        }

        @Override
        public void presentRegions(SearchByRegionOutputData outputData) {
            this.regionsOutput = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.lastError = errorMessage;
        }

        @Override
        public void switchToSaveCountryView() {
            this.switchedToSave = true;
        }

        @Override
        public void switchToSelectedListView() {
            this.switchedToSelected = true;
        }
    }

    /**
     * Shared sample data for tests.
     * */
    private List<Country> sampleCountries() {
        return Arrays.asList(
                makeCountry("France", "Europe", "Western Europe"),
                makeCountry("Germany", "Europe", "Western Europe"),
                makeCountry("Japan", "Asia", "Eastern Asia"),
                // This one has null region so we cover that branch
                makeCountry("Mystery", null, null)
        );
    }

    // ---------- listRegions ----------

    @Test
    public void listRegions_noRegions_callsFail() {
        List<Country> onlyNullRegion =
                Collections.singletonList(makeCountry("Nowhere", null, null));
        InMemoryDao dao = new InMemoryDao(onlyNullRegion);
        RecordingPresenter presenter = new RecordingPresenter();
        SearchByRegionInteractor interactor =
                new SearchByRegionInteractor(dao, presenter);

        interactor.listRegions();

        assertEquals("No regions found.", presenter.lastError);
        assertNull(presenter.regionsOutput);
    }

    @Test
    public void listRegions_withRegions_returnsSortedUniqueRegions() {
        InMemoryDao dao = new InMemoryDao(sampleCountries());
        RecordingPresenter presenter = new RecordingPresenter();
        SearchByRegionInteractor interactor =
                new SearchByRegionInteractor(dao, presenter);

        interactor.listRegions();

        assertNull(presenter.lastError);
        assertNotNull(presenter.regionsOutput);
        List<String> regions = presenter.regionsOutput.getRegions();
        // Should be unique + sorted: [Asia, Europe]
        assertEquals(Arrays.asList("Asia", "Europe"), regions);
    }

    @Test
    public void listRegions_emptyCountries_callsFail() {
        InMemoryDao dao = new InMemoryDao(new ArrayList<>());
        RecordingPresenter presenter = new RecordingPresenter();
        SearchByRegionInteractor interactor =
                new SearchByRegionInteractor(dao, presenter);

        interactor.listRegions();

        assertEquals("No regions found.", presenter.lastError);
        assertNull(presenter.regionsOutput);
    }

    // ---------- searchCountriesByRegion ----------

    @Test
    public void searchCountriesByRegion_nullRegion_fails() {
        InMemoryDao dao = new InMemoryDao(sampleCountries());
        RecordingPresenter presenter = new RecordingPresenter();
        SearchByRegionInteractor interactor =
                new SearchByRegionInteractor(dao, presenter);

        interactor.searchCountriesByRegion(new SearchByRegionInputData(null, null));

        assertEquals("Region must not be empty.", presenter.lastError);
        assertNull(presenter.countriesOutput);
    }

    @Test
    public void searchCountriesByRegion_noMatches_fails() {
        InMemoryDao dao = new InMemoryDao(sampleCountries());
        RecordingPresenter presenter = new RecordingPresenter();
        SearchByRegionInteractor interactor =
                new SearchByRegionInteractor(dao, presenter);

        interactor.searchCountriesByRegion(
                new SearchByRegionInputData("Africa", null));

        assertEquals("No countries found for region: Africa", presenter.lastError);
    }

    @Test
    public void searchCountriesByRegion_withMatches_returnsCountries() {
        InMemoryDao dao = new InMemoryDao(sampleCountries());
        RecordingPresenter presenter = new RecordingPresenter();
        SearchByRegionInteractor interactor =
                new SearchByRegionInteractor(dao, presenter);

        interactor.searchCountriesByRegion(
                new SearchByRegionInputData("Asia", null));

        assertNull(presenter.lastError);
        assertNotNull(presenter.countriesOutput);
        assertEquals("Asia", presenter.countriesOutput.getRegion());
        assertEquals(1, presenter.countriesOutput.getCountries().size());
        assertEquals("Japan",
                presenter.countriesOutput.getCountries().get(0).getName());
    }

    // ---------- listSubregionsForRegion ----------

    @Test
    public void listSubregions_nullRegion_fails() {
        InMemoryDao dao = new InMemoryDao(sampleCountries());
        RecordingPresenter presenter = new RecordingPresenter();
        SearchByRegionInteractor interactor =
                new SearchByRegionInteractor(dao, presenter);

        interactor.listSubregionsForRegion(new SearchByRegionInputData(null, null));

        assertEquals("Region must not be empty.", presenter.lastError);
    }

    @Test
    public void listSubregions_noSubregions_fails() {
        List<Country> antarcticaOnly = Collections.singletonList(
                makeCountry("Base", "Antarctica", null)
        );
        InMemoryDao dao = new InMemoryDao(antarcticaOnly);
        RecordingPresenter presenter = new RecordingPresenter();
        SearchByRegionInteractor interactor =
                new SearchByRegionInteractor(dao, presenter);

        interactor.listSubregionsForRegion(
                new SearchByRegionInputData("Antarctica", null));

        assertEquals("No subregions found for region: Antarctica",
                presenter.lastError);
    }

    @Test
    public void listSubregions_withSubregions_returnsSortedList() {
        InMemoryDao dao = new InMemoryDao(sampleCountries());
        RecordingPresenter presenter = new RecordingPresenter();
        SearchByRegionInteractor interactor =
                new SearchByRegionInteractor(dao, presenter);

        interactor.listSubregionsForRegion(
                new SearchByRegionInputData("Europe", null));

        assertNull(presenter.lastError);
        assertNotNull(presenter.subregionsOutput);
        assertEquals("Europe", presenter.subregionsOutput.getRegion());
        assertEquals(Collections.singletonList("Western Europe"),
                presenter.subregionsOutput.getSubregions());
    }

    // ---------- searchCountriesByRegionAndSubregion ----------

    @Test
    public void searchByRegionAndSubregion_nullRegion_fails() {
        InMemoryDao dao = new InMemoryDao(sampleCountries());
        RecordingPresenter presenter = new RecordingPresenter();
        SearchByRegionInteractor interactor =
                new SearchByRegionInteractor(dao, presenter);

        interactor.searchCountriesByRegionAndSubregion(
                new SearchByRegionInputData(null, "Eastern Asia"));

        assertEquals("A region needs to be selected.", presenter.lastError);
    }

    @Test
    public void searchByRegionAndSubregion_noMatches_fails() {
        InMemoryDao dao = new InMemoryDao(sampleCountries());
        RecordingPresenter presenter = new RecordingPresenter();
        SearchByRegionInteractor interactor =
                new SearchByRegionInteractor(dao, presenter);

        interactor.searchCountriesByRegionAndSubregion(
                new SearchByRegionInputData("Asia", "Southern Asia"));

        assertEquals("No countries found for region Asia and subregion Southern Asia.",
                presenter.lastError);
    }

    @Test
    public void searchByRegionAndSubregion_emptyCountries_fails() {
        InMemoryDao dao = new InMemoryDao(new ArrayList<>());
        RecordingPresenter presenter = new RecordingPresenter();
        SearchByRegionInteractor interactor =
                new SearchByRegionInteractor(dao, presenter);

        interactor.searchCountriesByRegionAndSubregion(
                new SearchByRegionInputData("Asia", "Eastern Asia"));

        assertEquals("No countries found for region Asia and subregion Eastern Asia.",
                presenter.lastError);
        assertNull(presenter.countriesOutput);
    }

    @Test
    public void searchByRegionAndSubregion_withMatches_returnsCountries() {
        InMemoryDao dao = new InMemoryDao(sampleCountries());
        RecordingPresenter presenter = new RecordingPresenter();
        SearchByRegionInteractor interactor =
                new SearchByRegionInteractor(dao, presenter);

        interactor.searchCountriesByRegionAndSubregion(
                new SearchByRegionInputData("Asia", "Eastern Asia"));

        assertNull(presenter.lastError);
        assertNotNull(presenter.countriesOutput);
        assertEquals("Asia", presenter.countriesOutput.getRegion());
        assertEquals("Eastern Asia", presenter.countriesOutput.getSubregion());
        assertEquals(1, presenter.countriesOutput.getCountries().size());
        assertEquals("Japan",
                presenter.countriesOutput.getCountries().get(0).getName());
    }

    // ---------- switchToSaveCountryView / switchToSelectedListView ----------

    @Test
    public void switchToSaveCountryView_delegatesToPresenter() {
        InMemoryDao dao = new InMemoryDao(new ArrayList<>());
        RecordingPresenter presenter = new RecordingPresenter();
        SearchByRegionInteractor interactor =
                new SearchByRegionInteractor(dao, presenter);

        interactor.switchToSaveCountryView();

        assertTrue(presenter.switchedToSave);
        assertFalse(presenter.switchedToSelected);
    }

    @Test
    public void switchToSelectedListView_delegatesToPresenter() {
        InMemoryDao dao = new InMemoryDao(new ArrayList<>());
        RecordingPresenter presenter = new RecordingPresenter();
        SearchByRegionInteractor interactor =
                new SearchByRegionInteractor(dao, presenter);

        interactor.switchToSelectedListView();

        assertTrue(presenter.switchedToSelected);
        assertFalse(presenter.switchedToSave);
    }
}