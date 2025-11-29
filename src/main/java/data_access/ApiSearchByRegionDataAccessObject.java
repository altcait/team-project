package data_access;

import entity.Country;
import entity.CountryFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.search.byregion.SearchByRegionDataAccessInterface;

import java.io.IOException;
import java.util.*;

/**
 * Data access object for the "Search by region" use case.
 * It calls the REST Countries API once, converts the JSON response
 * into Country entities (via CountryFactory), and caches them in memory
 * (both as a List and in a HashMap indexed by cca3).
 */
public class ApiSearchByRegionDataAccessObject implements SearchByRegionDataAccessInterface {

    // REST Countries endpoint: independent countries with selected fields
    private static final String API_URL =
            "https://restcountries.com/v3.1/independent?status=true" +
                    "&fields=name,languages,currencies,region,subregion,cca3";

    private final OkHttpClient client;
    private final CountryFactory countryFactory;

    // Cache of all countries as a list
    private List<Country> cachedCountries;

    // Extra cache: map from cca3 code to Country (shows off HashMap usage)
    private final Map<String, Country> countriesByCode;

    /**
     * Preferred constructor: inject a CountryFactory.
     */
    public ApiSearchByRegionDataAccessObject(CountryFactory countryFactory) {
        this.client = new OkHttpClient();
        this.countriesByCode = new HashMap<>();
        this.countryFactory = countryFactory;
    }

    /**
     * Convenience constructor: use a default CountryFactory.
     */
    public ApiSearchByRegionDataAccessObject() {
        this(new CountryFactory());
    }

    @Override
    public List<Country> getAllCountries() {
        if (cachedCountries == null) {
            cachedCountries = fetchCountriesFromApi();
        }
        // Return an unmodifiable view to prevent external modification
        return Collections.unmodifiableList(cachedCountries);
    }

    /**
     * get a single country by its cca3 code using the HashMap cache.
     */
    public Country getCountryByCca3(String cca3) {
        if (cachedCountries == null) {
            cachedCountries = fetchCountriesFromApi();
        }
        return countriesByCode.get(cca3);
    }

    /**
     * Calls the REST Countries API, parses the JSON response,
     * and fills both cachedCountries and countriesByCode.
     */
    private List<Country> fetchCountriesFromApi() {
        Request request = new Request.Builder()
                .url(API_URL)
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new RuntimeException("Failed to fetch countries: HTTP " + response.code());
            }

            if (response.body() == null) {
                throw new RuntimeException("Failed to fetch countries: empty response body");
            }

            String bodyString = response.body().string();
            JSONArray jsonArray = new JSONArray(bodyString);

            List<Country> result = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject countryJson = jsonArray.getJSONObject(i);
                Country country = parseCountry(countryJson);

                result.add(country);
                // Fill the HashMap cache by cca3
                countriesByCode.put(country.getCca3(), country);
            }

            return result;

        } catch (IOException e) {
            throw new RuntimeException("Error while calling REST Countries API", e);
        }
    }

    /**
     * Parses a single JSONObject representing a country into a Country entity.
     *
     * Example JSON:
     * {
     *   "name":{
     *      "common":"Brunei",
     *      ...,
     *      "nativeName": {
     *         "msa": {
     *           "official": "Nation of Brunei, Abode Damai",
     *           "common": "Negara Brunei Darussalam"
     *         }
     *       }
     *       },
     *   "cca3":"BRN",
     *   "currencies":{
     *       "BND":{"name":"Brunei dollar","symbol":"$"},
     *       "SGD":{"name":"Singapore dollar","symbol":"$"}
     *   },
     *   "region":"Asia",
     *   "subregion":"South-Eastern Asia",
     *   "languages":{"msa":"Malay"}
     * }
     */
    private Country parseCountry(JSONObject countryJson) {
        // name.common
        String name = "";
        JSONObject nameObj = countryJson.optJSONObject("name");
        if (nameObj != null) {
            name = nameObj.optString("common", "");
        }

        // cca3
        String cca3 = countryJson.optString("cca3", "");

        // region / subregion
        String region = countryJson.optString("region", "");
        String subregion = countryJson.optString("subregion", "");

        // languages: collect all language values into a List<String>
        List<String> languages = extractLanguageNames(countryJson.optJSONObject("languages"));

        // currencies: collect all currency names into a List<String>
        List<String> currencies = extractCurrencyNames(countryJson.optJSONObject("currencies"));

        // nativeNames: collect a List<String> of the country's name in its native language(s)
        List<String> nativeNames = extractNativeNames(nameObj.optJSONObject("nativeName"));

        // Use CountryFactory to create Country
        return countryFactory.create(name, cca3, currencies, region, subregion, languages, nativeNames);
    }

    /**
     * Extracts all currency names from the currencies object
     * (ignoring symbols) into a List<String>.
     */
    private List<String> extractCurrencyNames(JSONObject currenciesObj) {
        List<String> currencyNames = new ArrayList<>();

        if (currenciesObj == null) {
            return currencyNames;
        }

        Iterator<String> keys = currenciesObj.keys();
        while (keys.hasNext()) {
            String code = keys.next();
            JSONObject currencyObj = currenciesObj.optJSONObject(code);
            if (currencyObj != null) {
                String name = currencyObj.optString("name", "");
                if (!name.isEmpty()) {
                    currencyNames.add(name);
                }
            }
        }

        return currencyNames;
    }

    private List<String> extractLanguageNames(JSONObject languagesObj) {
        List<String> languageNames = new ArrayList<>();

        if (languagesObj == null) {
            return languageNames;
        }

        Iterator<String> keys = languagesObj.keys();
        while (keys.hasNext()) {
            String code = keys.next();
            // value is directly the language name string
            String name = languagesObj.optString(code, "");
            if (!name.isEmpty()) {
                languageNames.add(name);
            }
        }

        return languageNames;
    }

    /**
     * Extracts nativeNames List from nativeNames JSONObject.
     * @param nativeNamesObj object from which the names are extracted
     * @return List<String> of countries' name in its native language(s)
     */
    private List<String> extractNativeNames(JSONObject nativeNamesObj) {
        List<String> nativeNames = new ArrayList<>();

        if (nativeNamesObj == null) {
            return nativeNames;
        }

        Iterator<String> keys = nativeNamesObj.keys();
        while (keys.hasNext()) {
            String languageCode = keys.next();
            JSONObject nativeNameObj = nativeNamesObj.optJSONObject(languageCode);
            if (nativeNameObj != null) {
                String officialName = nativeNameObj.optString("official", "");
                String commonName = nativeNameObj.optString("common", "");
                if (!commonName.isEmpty() && !officialName.isEmpty()) {
                    nativeNames.add(officialName + "(" + commonName + ")");
                }
            }

        }
        return nativeNames;
    }
}