package view;

import interface_adapter.login.LoginController;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LoginView extends JPanel implements PropertyChangeListener {

    private final LoginViewModel viewModel;
    private LoginController controller;

    private final JTextField usernameField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    private final JLabel errorLabel = new JLabel();

    private final JButton loginButton = new JButton("Login");
    private final JButton cancelButton = new JButton("Cancel");

    public LoginView(LoginViewModel viewModel) {
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

        // Username listener (CSC207 style)
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

        // Password listener (CSC207 style)
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
            controller.execute(state.getUsername(), state.getPassword());
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

    public void setLoginController(LoginController controller) {
        this.controller = controller;
    }

    public String getViewName() {
        return "login";
    }
}

