package use_case.save_country;

/**
 * The Input Data for the Save Country Use Case.
 */
public class SaveCountryInputData {
    private final String countryCode;
    private final String listName;
    private final String notes;

    public SaveCountryInputData(String countryCode, String listName, String notes) {
        this.countryCode = countryCode;
        this.listName = listName;
        this.notes = notes;
    }

    String getCountryCode() {
        return countryCode;
    }

    String getListName() {
        return listName;
    }

    String getNotes() {
        return notes;
    }
}
