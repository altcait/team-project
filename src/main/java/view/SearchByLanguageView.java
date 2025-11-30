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

    // TODO: separate declaration & initialization?
    private final JList<String> countryList = new JList<>();
    private final JLabel errorLabel = new JLabel();

    public SearchByLanguageView(SearchByLanguageViewModel searchByLanguageViewModel) {
        this.searchByLanguageViewModel = searchByLanguageViewModel;
        searchByLanguageViewModel.addPropertyChangeListener(this);  // TODO: removed "this." from the front of line

        buildLayout();  // GUI layout helper
        addListeners(); // TODO: CURRENT

        // "Load during initialization" languages
        this.searchByLanguageController.loadLanguages();
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
        searchPanel.setPreferredSize(new Dimension(100, 50)); // TODO: Set fixed height
        searchPanel.add(new JLabel(SearchByLanguageViewModel.LANGUAGE_LABEL));
        searchPanel.add(languageComboBox);
        searchPanel.add(searchButton);;
        searchPanel.add(backButton);

        // Left pane: Search results list
        JPanel  leftPanel = new JPanel();
        leftPanel.add(new JScrollPane(countryList));

        // Top-right pane: Translated greetings if success, otherwise, error label
        JPanel topRightPanel = new JPanel();
        // TODO: error label
        errorLabel.setForeground(Color.RED);
        topRightPanel.add(errorLabel);
        // TODO: translated greetings or country name translated

        // Bottom-right pane: Navigation buttons to save country and to return to previous view
        JPanel bottomRightPanel = new JPanel();
        bottomRightPanel.add(saveButton);
        bottomRightPanel.add(backButton);

        // Vertical split on right pane
        JSplitPane rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topRightPanel, bottomRightPanel);
        rightSplit.setResizeWeight(0.5); // TODO

        // Horizontal split across main pane
        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightSplit);
        mainSplit.setResizeWeight(0.3); // TODO

        // main content centered in BorderLayout to ensure nested JPanels fill and resize with the main pane
        add(searchPanel, BorderLayout.NORTH);
        add(mainSplit, BorderLayout.CENTER);
    }

    /**
     * Adds action listeners for each button to the view.
     */
    private void addListeners() {

        languageComboBox.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(languageComboBox)) {
                            // TODO 2 options:
                            // 1) language options as entity -> not part of controller, view model, etc.
                            final SearchByLanguageState currentState = searchByLanguageViewModel.getState();
                            currentState.getLanguageOptions();
                            // 2) language options retrieved as part of the usecase (not accessed as entity)
                            searchByLanguageController.loadLanguages();
                        }
                    }
                }
        );

        searchButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(searchButton)) {
                            final SearchByLanguageState currentState = searchByLanguageViewModel.getState();

                            searchByLanguageController.execute(
                                    currentState.getSelectedLanguage()
                            );
                        }
                    }
                }
        );

        backButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        // TODO: if loop not needed apparently?
                        // if (evt.getSource().equals(backButton)) {
                            // final SearchByLanguageState currentState = searchByLanguageViewModel.getState();
                            searchByLanguageController.switchToPreviousView();
                        // }
                    }
                }
        );

        saveButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(saveButton)) {
                            searchByLanguageController.

                        }
                    }
                }
        )

//        languageComboBox.addActionListener(e -> {
//            String selected = (String) languageComboBox.getSelectedItem();
//            searchByLanguageController.onRegionSelected(selected);
//        });
//
//        searchButton.addActionListener(e -> {
//            String region = (String) languageComboBox.getSelectedItem();
//            searchByLanguageController.onSearchByRegionAndSubregion(region, subregion);
//        });
    }

    /**
     * React to a button click that results in evt.
     * @param evt the ActionEvent to react to
     */
    public void actionPerformed(ActionEvent evt) {
        // TODO
        System.out.println("Click " + evt.getActionCommand());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // if (evt.getPropertyName().equals("state")) {
            SearchByLanguageState state = (SearchByLanguageState) evt.getNewValue();
            username.setText(state.getUsername());
//        }
//        else if (evt.getPropertyName().equals("password")) {
//            final ProfileState state = (ProfileState) evt.getNewValue();
//            JOptionPane.showMessageDialog(null, "password updated for " + state.getUsername());
//        }
        // Update with the filtered countries list
        List<Country> countries = state.getCountries();
        if (countries != null) {
            DefaultListModel<String> model = new DefaultListModel<>();
            for (Country c : countries) {
                model.addElement(c.getName() + " (" + c.getCca3() + ")");
            }
            countryList.setModel(model);
        }

    }

    // TODO: CURRENT
//    public String getViewName() {
//        return viewName;
//    }

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
    }
}