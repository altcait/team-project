package use_case.save_country;

import java.util.List;

/**
 * The output boundary for the Save Country Use Case.
 */
public interface SaveCountryOutputBoundary {
    void prepareListNames(List<String> listNames);

    /**
     * Prepares the success view for the Save Country Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(SaveCountryOutputData outputData);

    /**
     * Prepares the failure view for the Save Country Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
