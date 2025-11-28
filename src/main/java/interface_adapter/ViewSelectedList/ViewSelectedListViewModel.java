package interface_adapter.ViewSelectedList;

import java.util.List;

public class ViewSelectedListViewModel {

    private String currentUsername;
    private String currentListName;
    private String description;
    private List<String> countries;
    private String errorMessage;

    public String getCurrentUsername() {
        return currentUsername;
    }

    public void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }

    public String getCurrentListName() {
        return currentListName;
    }

    public void setCurrentListName(String currentListName) {
        this.currentListName = currentListName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
