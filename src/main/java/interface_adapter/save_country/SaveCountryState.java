package interface_adapter.save_country;

import java.util.List;

public class SaveCountryState {
    private String countryCode;
    private String listName;
    private String notes;
    private String resultString;
    private List<String> lists;

    public String getCountryCode() { return countryCode; }
    public String getListName() { return listName; }
    public String getNotes() { return notes; }
    public String getResultString() { return resultString; }
    public List<String> getLists() {
        return lists;
    }

    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    public void setListName(String listName) { this.listName = listName; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setResultString(String resultString) { this.resultString = resultString; }
    public void setLists(List<String> lists) { this.lists = lists; }
}
