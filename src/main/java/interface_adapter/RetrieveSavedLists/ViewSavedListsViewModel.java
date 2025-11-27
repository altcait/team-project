package interface_adapter.RetrieveSavedLists;

import java.util.List;

public class ViewSavedListsViewModel {

    // later we will set this from login/profile
    private String currentUsername;

    private List<String> listNames;
    private List<String> descriptions;
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

    public List<String> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<String> descriptions) {
        this.descriptions = descriptions;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
