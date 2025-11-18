package view;

import interface_adapter.login.LoginController;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LoginView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "log in";
    private final LoginViewModel loginViewModel;

    private final JTextField usernameInputField = new JTextField(15);
    private final JLabel usernameErrorField = new JLabel();

    private final JPasswordField passwordInputField = new JPasswordField(15);
    private final JLabel passwordErrorField = new JLabel();

    private final JButton loginButton;
    private final JButton cancelButton;
    private LoginController loginController = null;

    public LoginView(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
        this.loginViewModel.addPropertyChangeListener(this);

        final JLabel title = new JLabel("Login Screen");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel usernameInfo = new LabelTextPanel(
                new JLabel("Username"), usernameInputField
        );
        final LabelTextPanel passwordInfo = new LabelTextPanel(
                new JLabel("Password"), passwordInputField
        );

        final JPanel buttons = new JPanel();
        loginButton = new JButton("Login");
        buttons.add(loginButton);
        cancelButton = new JButton("Cancel");
        buttons.add(cancelButton);

        loginButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (e.getSource().equals(loginButton)) {
                            final LoginState currentState = loginViewModel.getState();
                            loginController.execute(
                                    currentState.getUsername(),
                                    currentState.getPassword()
                            );
                        }
                    }
                }
        );
        cancelButton.addActionListener(this);
        usernameInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListener() {
                final LoginState currentState = loginViewModel.getState();
                currentState.setUsername(usernameInputField.getText());
                loginViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {documentListener();}
            @Override
            public void removeUpdate(DocumentEvent e) {documentListener();}
            @Override
            public void changedUpdate(DocumentEvent e) {documentListener();}

        });

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        passwordInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListener() {
                final LoginState currentState = loginViewModel.getState();
                currentState.setPassword(passwordInputField.getText());
                loginViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {documentListener();}
            @Override
            public void removeUpdate(DocumentEvent e) {documentListener();}
            @Override
            public void changedUpdate(DocumentEvent e) {documentListener();}

        });

        this.add(title);
        this.add(usernameInfo);
        this.add(usernameErrorField);
        this.add(passwordInfo);
        this.add(buttons);
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println("Click" + e.getActionCommand());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final LoginState state = (LoginState) evt.getNewValue();
        setFields(state);
        usernameErrorField.setText(state.getLoginError());
    }

    private void setFields(LoginState state) {
        usernameInputField.setText(state.getUsername());
    }

    public String getViewName() {
        return viewName;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }
}
