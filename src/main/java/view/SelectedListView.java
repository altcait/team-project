package view;

import data_access.FileUserDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.ViewSelectedList.ViewSelectedListController;
import interface_adapter.ViewSelectedList.ViewSelectedListViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SelectedListView extends JPanel {

    public final String viewName = "selected_list";

    private final ViewSelectedListController controller;
    private final ViewSelectedListViewModel viewModel;
    private final ViewManagerModel viewManagerModel;
    private final Supplier<String> currentUserSupplier;
    private final FileUserDataAccessObject favouritesDao;

    private final String listsViewName = "lists";
    // 🔹 MUST match SearchesView.getViewName()
    private final String searchesViewName = "searchesView";

    private final JLabel titleLabel = new JLabel("List: ");
    private final JTextArea descriptionArea = new JTextArea(3, 30);

    private final DefaultListModel<String> countriesModel = new DefaultListModel<>();
    private final JList<String> countriesList = new JList<>(countriesModel);

    private final JLabel errorLabel = new JLabel("");

    // 🔹 sort state: true = A–Z, false = Z–A
    private boolean sortAscending = true;

    public SelectedListView(ViewSelectedListController controller,
                            ViewSelectedListViewModel viewModel,
                            ViewManagerModel viewManagerModel,
                            Supplier<String> currentUserSupplier,
                            FileUserDataAccessObject favouritesDao) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
        this.currentUserSupplier = currentUserSupplier;
        this.favouritesDao = favouritesDao;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== TOP: title row + button row =====
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        // Row 1: List title
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleRow.add(titleLabel);

        // Row 2: Edit Description + Search + Sort + Delete Country
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // --- Edit Description ---
        JButton editDescriptionButton = new JButton("Edit Description");
        editDescriptionButton.addActionListener(e -> editDescription());
        buttonRow.add(editDescriptionButton);

        // --- Search button: go to SearchesView ---
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> {
            viewManagerModel.setState(searchesViewName);
            viewManagerModel.firePropertyChange();
        });
        buttonRow.add(searchButton);

        // --- Sort button (toggles A–Z / Z–A) ---
        JButton sortButton = new JButton("Sort A–Z");
        sortButton.addActionListener(e -> sortCountriesAlphabetically(sortButton));
        buttonRow.add(sortButton);

        // --- Delete Country (popup-based) ---
        JButton deleteCountryButton = new JButton("Delete Country");
        deleteCountryButton.addActionListener(e -> deleteCountryViaPopup());
        buttonRow.add(deleteCountryButton);

        // Add both rows to the top panel
        topPanel.add(titleRow);
        topPanel.add(buttonRow);

        add(topPanel, BorderLayout.NORTH);

        // ===== CENTER: description + list of countries =====
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JScrollPane(descriptionArea), BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(countriesList), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // 🔹 Double-click a country to edit notes
        countriesList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && !e.isConsumed()) {
                    e.consume();
                    String selected = countriesList.getSelectedValue();
                    if (selected != null) {
                        editCountryNotes(selected);
                    }
                }
            }
        });

        // ===== BOTTOM: Back button + error label =====
        JPanel bottomPanel = new JPanel(new BorderLayout());

        JButton backButton = new JButton("Back to Lists");
        backButton.addActionListener(e -> {
            viewManagerModel.setState(listsViewName);
            viewManagerModel.firePropertyChange();
        });
        bottomPanel.add(backButton, BorderLayout.WEST);

        errorLabel.setForeground(Color.RED);
        bottomPanel.add(errorLabel, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void loadList(String username, String listName) {
        viewModel.setCurrentUsername(username);
        viewModel.setCurrentListName(listName);
        controller.viewSelectedList(username, listName);
        refreshFromViewModel();
        // reset sort direction whenever we load a list
        sortAscending = true;
    }

    /**
     * Sort countries currently shown in the JList, toggling between A–Z and Z–A.
     */
    private void sortCountriesAlphabetically(JButton sortButton) {
        if (countriesModel.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "There are no countries to sort in this list.",
                    "Sort Countries",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        // Copy items from the model into a List
        java.util.List<String> countries = new java.util.ArrayList<>();
        for (int i = 0; i < countriesModel.size(); i++) {
            countries.add(countriesModel.getElementAt(i));
        }

        // Sort A–Z first
        countries.sort(String.CASE_INSENSITIVE_ORDER);

        // If current direction is descending, reverse after sort
        if (!sortAscending) {
            java.util.Collections.reverse(countries);
        }

        // Put them back into the model
        countriesModel.clear();
        for (String c : countries) {
            countriesModel.addElement(c);
        }

        // Flip the direction for next time
        sortAscending = !sortAscending;

        // Update button label to show what the *next* click will do
        if (sortAscending) {
            sortButton.setText("Sort A–Z");
        } else {
            sortButton.setText("Sort Z–A");
        }
    }

    private void refreshFromViewModel() {
        String error = viewModel.getErrorMessage();
        if (error != null) {
            errorLabel.setText(error);
            return;
        }

        titleLabel.setText("List: " + viewModel.getCurrentListName());
        descriptionArea.setText(viewModel.getDescription());

        countriesModel.clear();
        List<String> countries = viewModel.getCountries();
        if (countries != null) {
            for (String country : countries) {
                countriesModel.addElement(country);
            }
        }
    }

    private void editDescription() {
        String current = descriptionArea.getText();
        String newDesc = JOptionPane.showInputDialog(
                this,
                "Edit list description:",
                current
        );

        if (newDesc == null) {
            // user pressed Cancel
            return;
        }

        // Update UI + view model
        descriptionArea.setText(newDesc);
        viewModel.setDescription(newDesc);

        // --- Persist to JSON via favouritesDao ---
        String username = currentUserSupplier.get();
        String listName = viewModel.getCurrentListName();

        if (username == null || username.isEmpty()
                || listName == null || listName.isEmpty()) {
            // Shouldn't happen in normal flow, but fail gracefully
            return;
        }

        // Get the existing list details and update the description
        Map<String, Object> listDetails =
                favouritesDao.getListDetails(username, listName);

        if (listDetails == null) {
            // List not found in JSON; nothing more we can safely do
            return;
        }

        listDetails.put("description", newDesc);

        // Write the updated structure back to favouritesRepository.json
        favouritesDao.save();
    }

    /**
     * Delete a country using a popup dialog only
     * (no need to click it in the JList first).
     */
    private void deleteCountryViaPopup() {
        String username = currentUserSupplier.get();
        if (username == null || username.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No user is currently logged in.",
                    "Delete Country",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String listName = viewModel.getCurrentListName();
        if (listName == null || listName.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No list is currently selected.",
                    "Delete Country",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Get the current countries from the view model
        List<String> countries = viewModel.getCountries();
        if (countries == null || countries.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "There are no countries to delete in this list.",
                    "Delete Country",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        // Build combo box with all countries
        JComboBox<String> comboBox = new JComboBox<>();
        for (String c : countries) {
            comboBox.addItem(c);
        }

        int result = JOptionPane.showConfirmDialog(
                this,
                comboBox,
                "Choose a country to delete",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return; // user cancelled
        }

        String selected = (String) comboBox.getSelectedItem();
        if (selected == null) {
            return;
        }

        // Confirm deletion
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete country \"" + selected
                        + "\" from list \"" + listName + "\"?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        // Perform deletion & reload
        favouritesDao.removeCountry(username, listName, selected);

        controller.viewSelectedList(username, listName);
        refreshFromViewModel();
    }

    /**
     * Double-click handler: edit notes for a specific country.
     */
    private void editCountryNotes(String countryCode) {
        String username = currentUserSupplier.get();
        if (username == null || username.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No user is currently logged in.",
                    "Edit Notes",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String listName = viewModel.getCurrentListName();
        if (listName == null || listName.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No list is currently selected.",
                    "Edit Notes",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Get the list details from the DAO (description + countries map)
        Map<String, Object> listDetails = favouritesDao.getListDetails(username, listName);
        if (listDetails == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not find list data.",
                    "Edit Notes",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        @SuppressWarnings("unchecked")
        Map<String, String> countriesWithNotes =
                (Map<String, String>) listDetails.get("countries");

        if (countriesWithNotes == null) {
            // If no countries map yet, create one
            countriesWithNotes = new java.util.HashMap<>();
            listDetails.put("countries", countriesWithNotes);
        }

        String currentNote = countriesWithNotes.getOrDefault(countryCode, "");

        // Text area inside scroll pane for editing notes
        JTextArea noteArea = new JTextArea(5, 25);
        noteArea.setLineWrap(true);
        noteArea.setWrapStyleWord(true);
        noteArea.setText(currentNote);

        JScrollPane scrollPane = new JScrollPane(noteArea);

        int result = JOptionPane.showConfirmDialog(
                this,
                scrollPane,
                "Edit notes for " + countryCode,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return; // user cancelled
        }

        String newNote = noteArea.getText();

        // Update in-memory map
        countriesWithNotes.put(countryCode, newNote);

        // Persist to JSON
        favouritesDao.save();

        JOptionPane.showMessageDialog(
                this,
                "Notes saved for " + countryCode + ".",
                "Edit Notes",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
