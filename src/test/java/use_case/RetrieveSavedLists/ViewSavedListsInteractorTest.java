package use_case.RetrieveSavedLists;

import data_access.FavoritesReadDataAccess;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the ViewSavedListsInteractor use case.
 */
class ViewSavedListsInteractorTest {

    /**
     * Simple in-memory implementation of FavoritesReadDataAccess
     * for this test. We only care about getUserLists here.
     */
    private static class InMemoryFavoritesReadDataAccess implements FavoritesReadDataAccess {
        private final List<String> lists;

        InMemoryFavoritesReadDataAccess(List<String> lists) {
            this.lists = lists;
        }

        @Override
        public List<String> getUserLists(String username) {
            return lists;
        }

        @Override
        public Map<String, Object> getListDetails(String username, String listName) {
            throw new UnsupportedOperationException("not needed in this test");
        }

        @Override
        public List<String> getCountriesInList(String username, String listName) {
            throw new UnsupportedOperationException("not needed in this test");
        }

        @Override
        public String getListDescription(String username, String listName) {
            throw new UnsupportedOperationException("not needed in this test");
        }
    }

    /**
     * Test presenter that just records what it was given.
     */
    private static class TestPresenter implements ViewSavedListsOutputBoundary {
        ViewSavedListsResponseModel lastResponse;
        String lastError;

        @Override
        public ViewSavedListsResponseModel prepareSuccessView(ViewSavedListsResponseModel responseModel) {
            this.lastResponse = responseModel;
            return responseModel;
        }

        @Override
        public ViewSavedListsResponseModel prepareFailView(String errorMessage) {
            this.lastError = errorMessage;
            return null;
        }
    }

    @Test
    void viewLists_success_returnsLists() {
        TestPresenter presenter = new TestPresenter();
        FavoritesReadDataAccess dataAccess =
                new InMemoryFavoritesReadDataAccess(List.of("favorites", "visited"));

        ViewSavedListsInteractor interactor =
                new ViewSavedListsInteractor(presenter, dataAccess);

        // Assuming your request model has a constructor ViewSavedListsRequestModel(String username)
        ViewSavedListsRequestModel request = new ViewSavedListsRequestModel("derek");

        ViewSavedListsResponseModel response = interactor.viewLists(request);

        assertNotNull(response);
        assertEquals("derek", response.getUsername());
        assertEquals(List.of("favorites", "visited"), response.getListNames());
        assertNull(presenter.lastError, "No error should be set on success");
    }

    @Test
    void viewLists_noLists_triggersError() {
        TestPresenter presenter = new TestPresenter();
        FavoritesReadDataAccess dataAccess =
                new InMemoryFavoritesReadDataAccess(List.of());

        ViewSavedListsInteractor interactor =
                new ViewSavedListsInteractor(presenter, dataAccess);

        ViewSavedListsRequestModel request = new ViewSavedListsRequestModel("derek");

        ViewSavedListsResponseModel response = interactor.viewLists(request);

        // In your interactor, you return presenter.prepareFailView(...)
        // which returns null in this TestPresenter.
        assertNull(response, "Interactor should return null on failure here");
        assertEquals("No lists found for user: derek", presenter.lastError);
    }
}
