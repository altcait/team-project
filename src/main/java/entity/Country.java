package entity;

import java.util.List;

/**
 * Entity representing a country used in the travel planner.
 *
 * It stores only the fields needed by the use cases:
 * - name (common name)
 * - cca3 country code
 * - a list of currency names (no symbols)
 * - region
 * - subregion
 * - a list of country languages
 * - a list of county name in its native language(s)
 */
public class Country {

    private final String name;
    private final String cca3;
    private final List<String> currencies;
    private final String region;
    private final String subregion;
    private final List<String> languages;
    private final List<String> nativeNames;

    public Country(String name,
                   String cca3,
                   List<String> currencies,
                   String region,
                   String subregion,
                   List<String> languages,
                   List<String> nativeNames) {
        this.name = name;
        this.cca3 = cca3;
        this.currencies = currencies;
        this.region = region;
        this.subregion = subregion;
        this.languages = languages;
        this.nativeNames = nativeNames;
    }

    public String getName() {
        return name;
    }

    public String getCca3() {
        return cca3;
    }

    public List<String> getCurrencies() {
        return currencies;
    }

    public String getRegion() {
        return region;
    }

    public String getSubregion() {
        return subregion;
    }

    public List<String> getLanguage() {
        return languages;
    }

    public List<String> getNativeNames() {
        return nativeNames;
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", cca3='" + cca3 + '\'' +
                ", currencies=" + currencies +
                ", region='" + region + '\'' +
                ", subregion='" + subregion + '\'' +
                ", language='" + languages + '\'' +
                ", nativeNames=" + nativeNames + '\'' +
                '}';
    }
}