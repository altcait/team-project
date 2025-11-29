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
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel usernameRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        usernameRow.add(new JLabel("Username:"));
        usernameRow.add(usernameField);

        JPanel passwordRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        passwordRow.add(new JLabel("Password:"));
        passwordRow.add(passwordField);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonRow.add(loginButton);
        buttonRow.add(signupButton);
        buttonRow.add(cancelButton);

        errorLabel.setForeground(Color.RED);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(usernameRow);
        centerPanel.add(passwordRow);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(errorLabel);

        add(Box.createVerticalStrut(15));
        add(title);
        add(Box.createVerticalStrut(20));
        add(centerPanel);
        add(Box.createVerticalStrut(15));
        add(buttonRow);
        add(Box.createVerticalGlue());

        attachListeners();
    }

    private void attachListeners() {

        usernameField.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                LoginState state = viewModel.getState();
                state.setUsername(usernameField.getText());
                viewModel.setState(state);
            }
            @Override public void insertUpdate(DocumentEvent e) { update(); }
            @Override public void removeUpdate(DocumentEvent e) { update(); }
            @Override public void changedUpdate(DocumentEvent e) { update(); }
        });

        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                LoginState state = viewModel.getState();
                state.setPassword(new String(passwordField.getPassword()));
                viewModel.setState(state);
            }
            @Override public void insertUpdate(DocumentEvent e) { update(); }
            @Override public void removeUpdate(DocumentEvent e) { update(); }
            @Override public void changedUpdate(DocumentEvent e) { update(); }
        });

        loginButton.addActionListener(e -> {
            LoginState state = viewModel.getState();
            loginController.execute(state.getUsername(), state.getPassword());
        });

        signupButton.addActionListener(e -> {
            LoginState state = viewModel.getState();
            signupController.execute(state.getUsername(), state.getPassword());
        });

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
        passwordField.setText(newState.getPassword());
        errorLabel.setText(newState.getLoginError());
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void setSignupController(SignUpController signupController) {
        this.signupController = signupController;
    }

    public String getViewName() {
        return "login";
    }
}


