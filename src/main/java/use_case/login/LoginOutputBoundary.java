package use_case.login;

public interface LoginOutputBoundary {

    void prepareSuccess(LoginOutput loginOutput);

    void prepareFailure(String errorMessage);

}
