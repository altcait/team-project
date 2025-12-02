package interface_adapter.retrieve_saved_lists;

import java.util.List;

public class ViewSavedListsViewModel {

    private String currentUsername;
    private List<String> listNames;
    private String errorMessage;

    public String getCurrentUsername() {
        return currentUsername;
    }

    public void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }

    public List<String> getListNames() {
        return listNames;
    }

    public void setListNames(List<String> listNames) {
        this.listNames = listNames;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
