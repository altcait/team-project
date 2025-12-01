package view;

import interface_adapter.profile.ProfileController;
import interface_adapter.profile.ProfileState;
import interface_adapter.profile.ProfileViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ProfileView extends JPanel implements PropertyChangeListener {

    private final ProfileViewModel viewModel;
    private ProfileController controller;

    private final JLabel usernameLabel = new JLabel("Logged in as: ");
    private final JLabel languageLabel = new JLabel("Language: —");
    private final JLabel bioLabel = new JLabel("Bio: —");

    private final JButton savedListsButton = new JButton("Saved Lists");
    private final JButton editProfileButton = new JButton("Edit Profile");
    private final JButton logoutButton = new JButton("Logout");

    public ProfileView(ProfileViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Profile");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        languageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bioLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonRow.add(savedListsButton);
        buttonRow.add(editProfileButton);
        buttonRow.add(logoutButton);
        buttonRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalStrut(20));
        add(title);
        add(Box.createVerticalStrut(20));
        add(usernameLabel);
        add(Box.createVerticalStrut(20));
        add(languageLabel);
        add(Box.createVerticalStrut(10));
        add(bioLabel);
        add(Box.createVerticalStrut(30));
        add(buttonRow);
        add(Box.createVerticalGlue());

        attachListeners();
    }

    private void attachListeners() {
        savedListsButton.addActionListener(e -> controller.onFavorites());
        editProfileButton.addActionListener(e -> controller.onEditProfile());   // NEW
        logoutButton.addActionListener(e -> controller.onLogout());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ProfileState state = (ProfileState) evt.getNewValue();
        usernameLabel.setText("Logged in as: " + state.getUsername());
        languageLabel.setText("Language: " + state.getLanguage());
        bioLabel.setText("Bio: " + state.getBio());
    }

    public void setController(ProfileController controller) {
        this.controller = controller;
    }

    public String getViewName() {
        return "profile";
    }
}
