package use_case.save_country;

/**
 * Input Boundary for actions which are related to saving a country to a user's favourites list.
 */
public interface SaveCountryInputBoundary {
    /**
     * Executes the Save Country Use Case.
     * @param saveCountryInputData the input data for this use case
     */
    void execute(SaveCountryInputData saveCountryInputData);
}
