package view;

import data_access.UserCSVDataAccess;
import interface_adapter.ViewManagerModel;
import interface_adapter.save_country.SaveCountryController;
import interface_adapter.save_country.SaveCountryState;
import interface_adapter.save_country.SaveCountryViewModel;
import interface_adapter.ViewManagerModel;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * The View for the SaveCountry Use Case.
 */
public class SaveCountryView extends JPanel implements ActionListener, PropertyChangeListener {
    private final SaveCountryViewModel saveCountryViewModel;
    // result of operations - success or error message:
    private final JLabel saveCountryResultField = new JLabel();
    private final JTextField countryCodeInputField = new JTextField(3);
    private final JComboBox<String> listNameDropdown = new JComboBox<>(new String[]{"Want to travel", "Visited", "Bucket list"});
    private final JTextField countryNotesInputField = new JTextField(15);
    private final JButton saveCountryButton = new JButton("Save");
    private final JButton backProfileButton = new JButton("Back to Profile"); // Added back button
    private final JButton backListButton = new JButton("Back to List"); // Added back button
    private final String profileViewName = "profile";
    private final String listsViewName = "lists";


    private SaveCountryController saveCountryController = null;

    public SaveCountryView(SaveCountryViewModel saveCountryViewModel, ViewManagerModel viewManagerModel) {
        this.saveCountryViewModel = saveCountryViewModel;
        this.viewManagerModel = viewManagerModel;
        this.saveCountryViewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Instantiate panel and labels
        final JPanel saveCountryPanel = new JPanel();
        saveCountryPanel.setLayout(new BoxLayout(saveCountryPanel, BoxLayout.Y_AXIS));
        // Title
        JLabel title = new JLabel("Add a Country to your Favourites List");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveCountryPanel.add(title);

        final JLabel saveCountryLabel = new JLabel("Country code:");
        final JLabel listNameLabel = new JLabel("Choose which list you want to add this country to:");
        final JLabel notesLabel = new JLabel("Add some notes if you want:");

        // Error or success message
        JPanel result = new JPanel(new FlowLayout(FlowLayout.CENTER));
        saveCountryResultField.setFont(new Font("Arial", Font.BOLD, 14));
        saveCountryResultField.setAlignmentX(Component.CENTER_ALIGNMENT);
        result.add(saveCountryResultField);
        saveCountryPanel.add(result);

        // Country code
        JPanel country = new JPanel(new FlowLayout(FlowLayout.LEFT));
        country.add(saveCountryLabel);
        country.add(countryCodeInputField);
        saveCountryPanel.add(country);

        // List name
        JPanel list = new JPanel(new FlowLayout(FlowLayout.LEFT));
        list.add(listNameLabel);
        list.add(listNameDropdown);
        listNameDropdown.setSelectedIndex(-1);
        saveCountryPanel.add(list);

        // Notes
        JPanel notes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        notes.add(notesLabel);
        notes.add(countryNotesInputField);
        saveCountryPanel.add(notes);

        // Submit button
        saveCountryPanel.add(saveCountryButton);

        JButton backButton = new JButton("Back to lists");
        backButton.addActionListener(e -> {
            viewManagerModel.setState("lists");
            viewManagerModel.firePropertyChange();
        });

        saveCountryPanel.add(backButton);

        saveCountryButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (e.getSource().equals(saveCountryButton)) {
                            String countryCode = countryCodeInputField.getText();
                            String countryNotes = countryNotesInputField.getText();
                            String listName = (String)listNameDropdown.getSelectedItem();
//                            final SaveCountryState  currentState = saveCountryViewModel.getState();

                            if (countryCode == null || listName == null) {
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
        this.add(saveCountryPanel);

        // add a "Back to List/Profile " button
        JPanel backPanel = new JPanel();
        backListButton.addActionListener(e -> {
            viewManagerModel.setState(listsViewName);
            viewManagerModel.firePropertyChange();
        });
        backProfileButton.addActionListener(e -> {
            viewManagerModel.setState(profileViewName);
            viewManagerModel.firePropertyChange();
        });
        backPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        backPanel.add(backListButton);
        backPanel.add(backProfileButton);
        add(backPanel, BorderLayout.SOUTH);
    }

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
        // Set list names in state
        this.saveCountryController.fetchListNames();

        // get list names from state
        List<String> listNames = saveCountryState.getLists();

        if (listNames != null) {
            DefaultComboBoxModel listNamesModel =  new DefaultComboBoxModel<>();
            for (String list : listNames) {
                listNamesModel.addElement(list);
            }
            listNameDropdown.setModel(listNamesModel);
        }

        listNameDropdown.setSelectedIndex(-1);

        // Set form fields
        countryCodeInputField.setText(saveCountryState.getCountryCode());
        countryNotesInputField.setText(saveCountryState.getNotes());
        // show error or success message
        saveCountryResultField.setText(saveCountryState.getResultString());
    }

    public void setSaveCountryController(SaveCountryController saveCountryController) {
        this.saveCountryController = saveCountryController;
    }

    public String getViewName() {
        return "save country";
    }


}
