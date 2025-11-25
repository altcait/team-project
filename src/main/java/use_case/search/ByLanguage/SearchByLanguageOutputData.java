package use_case.search.ByLanguage;

import java.util.List;
import java.util.Set;

import entity.Country;    // TODO: get updated Country entity from remote repo

/**
 * Output data for the Search by Language use case.
 */
public class SearchByLanguageOutputData {
    private final Set<List<String>> languages;  // TODO: is this necessary??? rename to languageOptions
    private final String language;  // TODO: rename to selectedLanguage
    private final List<Country> countries;    // TODO: get updated Country entity from remote repo

    public SearchByLanguageOutputData(Set<List<String>> languages, String language, List<Country> countries) {
        this.languages = languages; // TODO: is this necessary???
        this.language = language;
        this.countries = countries;
        // TODO
    }

    // TODO: is this necessary???
    public Set<List<String>> getLanguages() {
        return languages;
    }

//    public String getLanguage() {
//        return language;
//    }

    public List<Country> getCountries() {    // TODO: get updated Country entity from remote repo
        return countries;
    }
}
