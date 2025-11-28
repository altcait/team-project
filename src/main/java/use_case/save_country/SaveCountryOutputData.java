package use_case.save_country;

/**
 * Output Data for the Save Country Use Case.
 */
public class SaveCountryOutputData {
    private final String countryCode;
    private final String listName;

    public SaveCountryOutputData(String countryCode, String listName) {
        this.countryCode = countryCode;
        this.listName = listName;
    }
    public String getCountryCode() { return countryCode; }
    public String getListName() { return listName; }
}
