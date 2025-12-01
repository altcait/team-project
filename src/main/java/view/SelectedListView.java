package view;

import data_access.FileUserDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.ViewSelectedList.ViewSelectedListController;
import interface_adapter.ViewSelectedList.ViewSelectedListViewModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Supplier;

public class SelectedListView extends JPanel {

    public final String viewName = "selected_list";

    private final ViewSelectedListController controller;
    private final ViewSelectedListViewModel viewModel;
    private final ViewManagerModel viewManagerModel;
    private final Supplier<String> currentUserSupplier;
    private final FileUserDataAccessObject favouritesDao;

    private final String listsViewName = "lists";

    private final JLabel titleLabel = new JLabel("List: ");
    private final JTextArea descriptionArea = new JTextArea(3, 30);

    private final DefaultListModel<String> countriesModel = new DefaultListModel<>();
    private final JList<String> countriesList = new JList<>(countriesModel);

    private final JLabel errorLabel = new JLabel("");

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

        // --- Search button (placeholder) ---
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> JOptionPane.showMessageDialog(
                this,
                "Search feature not implemented yet.",
                "Search",
                JOptionPane.INFORMATION_MESSAGE
        ));
        buttonRow.add(searchButton);

        // --- Sort button (placeholder) ---
        JButton sortButton = new JButton("Sort");
        sortButton.addActionListener(e -> JOptionPane.showMessageDialog(
                this,
                "Sort feature not implemented yet.",
                "Sort",
                JOptionPane.INFORMATION_MESSAGE
        ));
        buttonRow.add(sortButton);

        // --- Delete Country (ALWAYS uses popup) ---
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

        if (newDesc != null) {
            descriptionArea.setText(newDesc);
            viewModel.setDescription(newDesc);
            // TODO: later create UpdateListDescription use case for JSON saving
        }
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
}
