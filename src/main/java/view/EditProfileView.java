package view;

import interface_adapter.profile.EditProfileState;
import interface_adapter.profile.EditProfileViewModel;
import interface_adapter.profile.ProfileController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class EditProfileView extends JPanel implements PropertyChangeListener {

    private final EditProfileViewModel editProfileViewModel;
    private ProfileController profileController;

    private final JTextField languageField = new JTextField(15);
    private final JTextField bioField = new JTextField(15);

    private final JButton saveButton = new JButton("Save");
    private final JButton backButton = new JButton("Back");

    public EditProfileView(EditProfileViewModel editProfileViewModel) {
        this.editProfileViewModel = editProfileViewModel;
        this.editProfileViewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Edit Profile");
        title.setAlignmentX(CENTER_ALIGNMENT);

        JPanel langRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        langRow.add(new JLabel("Language:"));
        langRow.add(languageField);

        JPanel bioRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bioRow.add(new JLabel("Bio:"));
        bioRow.add(bioField);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonRow.add(saveButton);
        buttonRow.add(backButton);

        add(Box.createVerticalStrut(20));
        add(title);
        add(Box.createVerticalStrut(20));
        add(langRow);
        add(bioRow);
        add(Box.createVerticalStrut(20));
        add(buttonRow);

        attachListeners();
    }

    private void attachListeners() {
        saveButton.addActionListener(e -> {
            EditProfileState state = editProfileViewModel.getState();
            profileController.onSaveProfile(languageField.getText(), bioField.getText());

            editProfileViewModel.setState(state);
            editProfileViewModel.firePropertyChange();

            JOptionPane.showMessageDialog(null, "Profile updated!");
        });

        backButton.addActionListener(e -> profileController.onBackToProfile());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        EditProfileState state = (EditProfileState) evt.getNewValue();
        languageField.setText(state.getLanguage());
        bioField.setText(state.getBio());
    }

    public void setController(ProfileController controller) {
        this.profileController = controller;
    }

    public String getViewName() {
        return "edit-profile";
    }



}
