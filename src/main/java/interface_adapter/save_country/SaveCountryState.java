package interface_adapter.save_country;

public class SaveCountryState {
    private String countryCode;
    private String listName;
    private String notes;
    private String resultString;

    public String getCountryCode() { return countryCode; }
    public String getListName() { return listName; }
    public String getNotes() { return notes; }
    public String getResultString() { return resultString; }

    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    public void setListName(String listName) { this.listName = listName; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setResultString(String resultString) { this.resultString = resultString; }
}
