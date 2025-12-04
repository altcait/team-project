package entity;

import java.util.List;

/**
 * Factory for creating Country entities.
 * Using a factory keeps object creation in the entity layer,
 * and lets data access / use cases avoid calling new Country(...)
 * directly.
 */
public class CountryFactory {

    public Country create(String name,
                          String cca3,
                          List<String> currencies,
                          String region,
                          String subregion,
                          List<String> languages,
                          List<String> nativeNames) {
        return new Country(name, cca3, currencies, region, subregion, languages, nativeNames);
    }
}