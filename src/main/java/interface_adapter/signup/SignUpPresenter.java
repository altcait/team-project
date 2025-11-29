package interface_adapter.signup;

import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import use_case.signup.SignUpOutputBoundary;
import use_case.signup.SignUpOutput;

import javax.swing.*;

public class SignUpPresenter implements SignUpOutputBoundary {
    private final SignUpViewModel signupViewModel;
    private final LoginViewModel loginViewModel;
    private final ViewManagerModel viewManagerModel;

    public SignUpPresenter(ViewManagerModel vm, SignUpViewModel signupVM, LoginViewModel loginVM) {
        this.viewManagerModel = vm;
        this.signupViewModel = signupVM;
        this.loginViewModel = loginVM;
    }

    @Override
    public void prepareSuccessView(SignUpOutput data) {
        JOptionPane.showMessageDialog(null,
                "Account created successfully!");

        viewManagerModel.setState(loginViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailureView(String error) {
        JOptionPane.showMessageDialog(null, error);
    }

    @Override
    public void switchToLoginView() {
        viewManagerModel.setState(loginViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
