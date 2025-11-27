package use_case.RetrieveSavedLists;

import java.util.List;

public class ViewSavedListsResponseModel {

    private final List<String> listNames;
    private final List<String> descriptions;

    public ViewSavedListsResponseModel(List<String> listNames,
                                       List<String> descriptions) {
        this.listNames = listNames;
        this.descriptions = descriptions;
    }

    public List<String> getListNames() {
        return listNames;
    }

    public List<String> getDescriptions() {
        return descriptions;
    }
}
