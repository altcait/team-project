package view;

import interface_adapter.login.LoginController;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import interface_adapter.signup.SignUpController;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LoginSignUpView extends JPanel implements PropertyChangeListener {

    private final LoginViewModel viewModel;
    private LoginController loginController;
    private SignUpController signupController;

    private final JTextField usernameField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    private final JLabel errorLabel = new JLabel();

    private final JButton loginButton = new JButton("Login");
    private final JButton signupButton = new JButton("Create Account");
    private final JButton cancelButton = new JButton("Cancel");

    public LoginSignUpView(LoginViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Login");
        title.setAlignmentX(CENTER_ALIGNMENT);

        add(title);
        add(makeInputRow("Username:", usernameField));
        add(makeInputRow("Password:", passwordField));
        add(errorLabel);

        JPanel buttonRow = new JPanel();
        buttonRow.add(loginButton);
        buttonRow.add(signupButton);
        buttonRow.add(cancelButton);
        add(buttonRow);

        attachListeners();
    }

    private JPanel makeInputRow(String labelText, JComponent inputField) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel(labelText));
        row.add(inputField);
        return row;
    }

    private void attachListeners() {

        usernameField.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                LoginState state = viewModel.getState();
                state.setUsername(usernameField.getText());
                viewModel.setState(state);
            }

            @Override
            public void insertUpdate(DocumentEvent e) { update(); }

            @Override
            public void removeUpdate(DocumentEvent e) { update(); }

            @Override
            public void changedUpdate(DocumentEvent e) { update(); }
        });

        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                LoginState state = viewModel.getState();
                state.setPassword(new String(passwordField.getPassword()));
                viewModel.setState(state);
            }

            @Override
            public void insertUpdate(DocumentEvent e) { update(); }

            @Override
            public void removeUpdate(DocumentEvent e) { update(); }

            @Override
            public void changedUpdate(DocumentEvent e) { update(); }
        });

        // Login button
        loginButton.addActionListener(e -> {
            LoginState state = viewModel.getState();
            loginController.execute(state.getUsername(), state.getPassword());
        });

        //Create Account button
        signupButton.addActionListener(e -> {
            LoginState state = viewModel.getState();
            signupController.execute(state.getUsername(), state.getPassword());
        });

        // Cancel button
        cancelButton.addActionListener(e -> clearFields());
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        errorLabel.setText("");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        LoginState newState = (LoginState) evt.getNewValue();
        usernameField.setText(newState.getUsername());
        errorLabel.setText(newState.getLoginError());
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void  setSignupController(SignUpController signupController) {
        this.signupController = signupController;
    }

    public String getViewName() {
        return "login";
    }
}

