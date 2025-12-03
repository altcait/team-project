package use_case.ViewSelectedList;

import data_access.FavoritesReadDataAccess;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ViewSelectedListInteractorTest {

    /**
     * Fake in-memory data access for this test.
     */
    private static class InMemoryFavoritesReadDataAccess implements FavoritesReadDataAccess {

        private final String description;
        private final List<String> countries;

        InMemoryFavoritesReadDataAccess(String description, List<String> countries) {
            this.description = description;
            this.countries = countries;
        }

        @Override
        public List<String> getUserLists(String username) {
            throw new UnsupportedOperationException("Not needed here");
        }

        @Override
        public Map<String, Object> getListDetails(String username, String listName) {
            return Map.of(
                    "description", description,
                    "countries", countries
            );
        }

        @Override
        public List<String> getCountriesInList(String username, String listName) {
            return countries;
        }

        @Override
        public String getListDescription(String username, String listName) {
            return description;
        }
    }

    /**
     * Test presenter that records responses.
     */
    private static class TestPresenter implements ViewSelectedListOutputBoundary {

        ViewSelectedListResponseModel lastResponse;
        String lastError;

        @Override
        public ViewSelectedListResponseModel prepareSuccessView(ViewSelectedListResponseModel responseModel) {
            this.lastResponse = responseModel;
            return responseModel;
        }

        @Override
        public ViewSelectedListResponseModel prepareFailView(String errorMessage) {
            this.lastError = errorMessage;
            return null;
        }
    }

    @Test
    void viewSelectedList_success_returnsDetails() {
        TestPresenter presenter = new TestPresenter();
        FavoritesReadDataAccess dataAccess =
                new InMemoryFavoritesReadDataAccess("A cool list", List.of("CAN", "USA"));

        ViewSelectedListInteractor interactor =
                new ViewSelectedListInteractor(presenter, dataAccess);

        ViewSelectedListRequestModel request =
                new ViewSelectedListRequestModel("derek", "visited");

        ViewSelectedListResponseModel response =
                interactor.viewSelectedList(request);

        assertNotNull(response);
        assertEquals("derek", response.getUsername());
        assertEquals("visited", response.getListName());
        assertEquals("A cool list", response.getDescription());
        assertEquals(List.of("CAN", "USA"), response.getCountries());
        assertNull(presenter.lastError);
    }

    @Test
    void viewSelectedList_emptyList_returnsEmptyCountries() {
        TestPresenter presenter = new TestPresenter();
        FavoritesReadDataAccess dataAccess =
                new InMemoryFavoritesReadDataAccess("", List.of());

        ViewSelectedListInteractor interactor =
                new ViewSelectedListInteractor(presenter, dataAccess);

        ViewSelectedListRequestModel request =
                new ViewSelectedListRequestModel("derek", "visited");

        ViewSelectedListResponseModel response =
                interactor.viewSelectedList(request);

        assertNotNull(response);
        assertEquals("derek", response.getUsername());
        assertEquals("visited", response.getListName());
        assertEquals("", response.getDescription());
        assertTrue(response.getCountries().isEmpty());
        assertNull(presenter.lastError); // no error raised by the interactor
    }
}
