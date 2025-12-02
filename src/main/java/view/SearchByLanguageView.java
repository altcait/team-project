package view;

import entity.Country;
import interface_adapter.search.ByLanguage.SearchByLanguageController;
import interface_adapter.search.ByLanguage.SearchByLanguageState;
import interface_adapter.search.ByLanguage.SearchByLanguageViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Swing view for the Search by Language feature.
 */
public class SearchByLanguageView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "searchByLanguage";

    private final SearchByLanguageViewModel searchByLanguageViewModel;
    private SearchByLanguageController searchByLanguageController;

    private final JComboBox<String> languageComboBox = new JComboBox<>();;
    private final JButton searchButton = new JButton(SearchByLanguageViewModel.SEARCH_BUTTON_LABEL);
    private final JButton saveButton = new JButton(SearchByLanguageViewModel.SAVE_BUTTON_LABEL);
    private final JButton backButton = new JButton(SearchByLanguageViewModel.BACK_BUTTON_LABEL);

    private final JList<String> countryList = new JList<>();
    private final JLabel errorLabel = new JLabel();
    private final JLabel nativeNamesLabel = new JLabel();

    public SearchByLanguageView(SearchByLanguageViewModel searchByLanguageViewModel) {
        this.searchByLanguageViewModel = searchByLanguageViewModel;
        searchByLanguageViewModel.addPropertyChangeListener(this);

        buildLayout();  // GUI layout helper
        addListeners();
    }

    /**
     * Sets up the layout and components of the view.
     */
    private void buildLayout() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel(SearchByLanguageViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Top pane: Search dropdown and button
        JPanel searchPanel = new JPanel();
        searchPanel.setPreferredSize(new Dimension(100, 50));
        searchPanel.add(new JLabel(SearchByLanguageViewModel.LANGUAGE_LABEL));
        searchPanel.add(languageComboBox);
        searchPanel.add(searchButton);

        // Left pane: Search results list
        JPanel  leftPanel = new JPanel(new BorderLayout());
        countryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        countryList.setVisibleRowCount(10);
        countryList.setFixedCellHeight(30);
        leftPanel.add(new JScrollPane(countryList), BorderLayout.CENTER);

        // Top-right pane: Translated greetings if success, otherwise, error label
        JPanel topRightPanel = new JPanel();
        topRightPanel.setLayout(new BoxLayout(topRightPanel, BoxLayout.Y_AXIS));
        topRightPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLabel.setForeground(Color.RED);

        nativeNamesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topRightPanel.add(Box.createVerticalGlue()); // Add spacing above
        topRightPanel.add(errorLabel);
        topRightPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        topRightPanel.add(nativeNamesLabel);
        topRightPanel.add(Box.createVerticalGlue()); // Add spacing below

        // Bottom-right pane: Navigation buttons to save country and to return to previous view
        JPanel bottomRightPanel = new JPanel();
        bottomRightPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomRightPanel.add(saveButton);
        bottomRightPanel.add(backButton);

        // Vertical split on right pane
        JScrollPane scrollableTopRight = new JScrollPane(topRightPanel);
        scrollableTopRight.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollableTopRight.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JSplitPane rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollableTopRight, bottomRightPanel);
        rightSplit.setResizeWeight(0.5);

        // Horizontal split across main pane
        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightSplit);
        mainSplit.setResizeWeight(0.6);

        // main content centered in BorderLayout to ensure nested JPanels fill and resize with the main pane
        add(searchPanel, BorderLayout.NORTH);
        add(mainSplit, BorderLayout.CENTER);
    }

    /**
     * Adds action listeners for each button to the view.
     */
    private void addListeners() {

        searchButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(searchButton)) {
                            final SearchByLanguageState currentState = searchByLanguageViewModel.getState();
                            String selected = (String) languageComboBox.getSelectedItem();

                            if (selected != null) {
                                currentState.setSelectedLanguage(selected);
//                                searchByLanguageViewModel.setState(currentState); // fire update if needed
                                searchByLanguageController.execute(selected);
//                            searchByLanguageController.execute(
//                                    currentState.getSelectedLanguage()
//                            );
                            } else {
//                                SearchByLanguageState errorState = new SearchByLanguageState();
//                                errorState.setErrorMessage("Please select a language before searching.");
                                currentState.setErrorMessage("Please select a language before searching.");
//                                searchByLanguageViewModel.setState(errorState);
                            }
                        }
                    }
                }
        );

        backButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                         if (evt.getSource().equals(backButton)) {
                            // final SearchByLanguageState currentState = searchByLanguageViewModel.getState();
                            searchByLanguageController.switchToPreviousView();
                         }
                    }
                }
        );

        saveButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(saveButton)) {
                            searchByLanguageController.switchToSaveCountryView();
                        }
                    }
                }
        );

        countryList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = countryList.getSelectedIndex();
                if (index >= 0) {
                    List<Country> countries = searchByLanguageViewModel.getState().getCountries();
                    if (countries != null && index < countries.size()) {
                        Country selectedCountry = countries.get(index);
//                        nativeNamesLabel.setText(
//                                selectedCountry.getName() + "'s native names: " +
//                                selectedCountry
//                                .getNativeNames()
//                                .stream()
//                                .collect(Collectors.joining(" ; "))
//                        );
                        String formattedNames =
                                selectedCountry.getNativeNames()
                                        .stream()
                                        .map(name -> "<li>" + name + "</li>")
                                        .collect(Collectors.joining());

                        nativeNamesLabel.setText(
                                "<html><b>" +
                                        selectedCountry.getName() + "'s native names:</b><br><ul style='margin-left:15px'>" +
                                        "<ul>" + formattedNames + "</ul>" +
                                "</html>"
                        );
                    }
                }
            }
        });

    }

    /**
     * React to a button click that results in evt.
     * @param evt the ActionEvent to react to
     */
    public void actionPerformed(ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
//        // if (evt.getPropertyName().equals("state")) {
//            SearchByLanguageState state = (SearchByLanguageState) evt.getNewValue();
//            username.setText(state.getUsername());
////        }
////        else if (evt.getPropertyName().equals("password")) {
////            final ProfileState state = (ProfileState) evt.getNewValue();
////            JOptionPane.showMessageDialog(null, "password updated for " + state.getUsername());
////        }
//        // Update with the filtered countries list
//        List<Country> countries = state.getCountries();
//        if (countries != null) {
//            DefaultListModel<String> model = new DefaultListModel<>();
//            for (Country c : countries) {
//                model.addElement(c.getName() + " (" + c.getCca3() + ")");
//            }
//            countryList.setModel(model);
//        }

        SearchByLanguageState state = (SearchByLanguageState) evt.getNewValue();

        // Populate the initial language selection box
//        Set<String> languages = state.getLanguageOptions();
//        if (languages != null && languageComboBox.getItemCount() != languages.size()) {
//            languageComboBox.removeAllItems();
//            for (String lang : languages) {
//                languageComboBox.addItem(lang);
//            }
//
//            String selected = state.getSelectedLanguage();
//            if (selected != null) {
//                languageComboBox.setSelectedItem(selected);
//            }
//        }

//        Set<String> languages = state.getLanguageOptions();
//        if (languages != null && !languages.isEmpty()) {
//            // Save current selection BEFORE resetting the model
//            String previouslySelected = (String) languageComboBox.getSelectedItem();
//
//            // Create new model from language set
//            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
//            for (String lang : languages) {
//                model.addElement(lang);
//            }
//
//            // Set new model on combo box (cleaner than removeAllItems + addItem)
//            languageComboBox.setModel(model);
//
//            // Restore selection if it exists in new options
//            if (previouslySelected != null && languages.contains(previouslySelected)) {
//                languageComboBox.setSelectedItem(previouslySelected);
//            } else {
//                // Or select whatever is stored in the ViewModel state
//                String selectedFromState = state.getSelectedLanguage();
//                if (selectedFromState != null && languages.contains(selectedFromState)) {
//                    languageComboBox.setSelectedItem(selectedFromState);
//                }
//            }
//        }



        // Update the country list when countries are loaded after search
        List<Country> countries = state.getCountries();
        if (countries != null) {
            DefaultListModel<String> model = new DefaultListModel<>();
            for (Country c : countries) {
                model.addElement(c.getName() + " (" + c.getCca3() + ")");
            }
            countryList.setModel(model);
            if (!countries.isEmpty()) {
                countryList.setSelectedIndex(0); // Auto-select first country
            }
        }

        // Display error(s) and country native name(s) when applicable
        if (state.getErrorMessage() != null && !state.getErrorMessage().isEmpty()) {
            errorLabel.setText(state.getErrorMessage());
        } else {
            // Clear error if none
            errorLabel.setText("");
        }

    }

//    @Override
//    public void propertyChange(PropertyChangeEvent evt) {
//        SearchByLanguageState state = searchByLanguageViewModel.getState();
//
//        // Update the region dropdown box
//        List<String> regions = state.getRegionOptions();
//        if (regions != null) {
//            languageComboBox.removeAllItems();
//            for (String r : regions) {
//                languageComboBox.addItem(r);
//            }
//        }
//
//        // Update the error message
//        if (state.getErrorMessage() != null) {
//            errorLabel.setText(state.getErrorMessage());
//        } else {
//            errorLabel.setText("");
//        }
//    }

    public String getViewName(){
        return viewName;
    }

    public void setSearchByLanguageController(SearchByLanguageController searchByLanguageController) {
        this.searchByLanguageController = searchByLanguageController;

        // trigger loading of all language options
        searchByLanguageController.loadLanguages();

        // populate dropdown with language options
        SearchByLanguageState state = searchByLanguageViewModel.getState();
        Set<String> languages = state.getLanguageOptions();
        if (languages != null && !languages.isEmpty()) {
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            for (String lang : languages) {
                model.addElement(lang);
            }
            languageComboBox.setModel(model);
        }
    }
}