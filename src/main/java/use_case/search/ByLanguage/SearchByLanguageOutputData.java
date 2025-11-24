package use_case.search.ByLanguage;

import java.util.List;
import java.util.Set;

import entity.Country;    // TODO: get updated Country entity from remote repo

/**
 * Output data for the Search by Language use case.
 */
public class SearchByLanguageOutputData {
    private final String language;
     private final Set<List<String>> languages;  // TODO: is this necessary???
    private final List<Country> countries;    // TODO: get updated Country entity from remote repo

    public SearchByLanguageOutputData(Set<List<String>> languages, String language, List<Country> countries) {
        this.languages = languages; // TODO: is this necessary???
        this.language = language;
        this.countries = countries;
        // TODO
    }

//    public String getLanguage() {
//        return language;
//    }

    public List<Country> getCountries() {    // TODO: get updated Country entity from remote repo
        return countries;
    }
    // TODO
}
