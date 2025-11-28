package view;

import interface_adapter.save_country.SaveCountryController;
import interface_adapter.save_country.SaveCountryState;
import interface_adapter.save_country.SaveCountryViewModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for the SaveCountry Use Case.
 */
public class SaveCountryView extends JPanel implements ActionListener, PropertyChangeListener {
    private final SaveCountryViewModel saveCountryViewModel;

    // result of operations:
    private final JLabel saveCountryResultField = new JLabel();

    private final JTextField countryCodeInputField = new JTextField(3);
    private final JComboBox<String> listNameDropdown = new JComboBox<>(new String[]{"Want to travel", "Visited", "Bucket list"});
    private final JTextField countryNotesInputField = new JTextField(15);
    private final JButton saveCountryButton = new JButton("Save");



    private SaveCountryController saveCountryController = null;

    public SaveCountryView(SaveCountryViewModel saveCountryViewModel) {
        this.saveCountryViewModel = saveCountryViewModel;
        this.saveCountryViewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Instantiate panel and labels
        final JPanel saveCountryPanel = new JPanel();
        final JLabel saveCountryLabel = new JLabel("3-letter country code");
        final JLabel listNameLabel = new JLabel("Choose which list you want to add this country to.");

        // Error message
        saveCountryPanel.add(saveCountryResultField);

        // Country code
        saveCountryPanel.add(saveCountryLabel);
        saveCountryPanel.add(countryCodeInputField);
        // List name
        saveCountryPanel.add(listNameLabel);
        listNameDropdown.setSelectedIndex(-1);
        saveCountryPanel.add(listNameDropdown);
        // Notes
        saveCountryPanel.add(countryNotesInputField);
        saveCountryPanel.add(saveCountryButton);

        saveCountryButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (e.getSource().equals(saveCountryButton)) {
                            String countryCode = countryCodeInputField.getText();
                            String countryNotes = countryNotesInputField.getText();
                            String listName = (String)listNameDropdown.getSelectedItem();
//                            final SaveCountryState  currentState = saveCountryViewModel.getState();

                            if (countryCode.isEmpty() || listName.isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Please enter at minimum a country code and select a list.", "Error", JOptionPane.ERROR_MESSAGE);
                            } else if (countryCode.length() != 3) {
                                JOptionPane.showMessageDialog(null, "Please enter a 3-letter country code", "Error", JOptionPane.ERROR_MESSAGE);
                            } else {
                                saveCountryController.execute(countryCode, listName, countryNotes);
                            }
                        }
                    }
                }
        );

//        addCountryCodeListener();
        this.add(saveCountryPanel);
    }

//    private void addCountryCodeListener() {
//        countryCodeInputField.getDocument().addDocumentListener(new DocumentListener() {
//            private void documentListenerHelper() {
//                final SaveCountryState savedState = saveCountryViewModel.getState();
//                savedState.setCountryCode(countryCodeInputField.getText());
//                saveCountryViewModel.setState(savedState);
//                System.out.println("Enter country code");
//
//            }
//
//            @Override
//            public void insertUpdate(DocumentEvent e) {
//                documentListenerHelper();
//            }
//
//            @Override
//            public void removeUpdate(DocumentEvent e) {
//                documentListenerHelper();
//            }
//
//            @Override
//            public void changedUpdate(DocumentEvent e) {
//                documentListenerHelper();
//            }
//        });
//    }

    /**
     * React to a button click that results in evt.
     * @param evt the ActionEvent to react to
     */
    public void actionPerformed(ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
        // Gather the info needed and pass it to the controller
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final SaveCountryState saveCountryState = (SaveCountryState)evt.getNewValue();

        // reset form fields
        countryCodeInputField.setText(saveCountryState.getCountryCode());
        countryNotesInputField.setText(saveCountryState.getNotes());
        listNameDropdown.setSelectedIndex(-1);
        // show error or success message
//        saveCountryResultField.setText(saveCountryState.getResultString());
        JOptionPane.showMessageDialog(null, saveCountryState.getResultString(), "Result", JOptionPane.INFORMATION_MESSAGE);
    }

    public void setSaveCountryController(SaveCountryController saveCountryController) { this.saveCountryController = saveCountryController; }

    public String getViewName() {
        return "save country";
    }
}
