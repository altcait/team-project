package view;

import data_access.FileUserDataAccessObject;
import interface_adapter.RetrieveSavedLists.ViewSavedListsController;
import interface_adapter.RetrieveSavedLists.ViewSavedListsViewModel;
import interface_adapter.ViewManagerModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Supplier;

public class ListsView extends JPanel {

    public final String viewName = "lists";

    private final ViewSavedListsController controller;
    private final ViewSavedListsViewModel viewModel;
    private final SelectedListView selectedListView;
    private final ViewManagerModel viewManagerModel;
    private final Supplier<String> currentUserSupplier;
    private final FileUserDataAccessObject favouritesDao;

    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> listDisplay = new JList<>(listModel);
    private final JLabel errorLabel = new JLabel("");

    private final String profileViewName = "profile";

    // 🔹 sort state for lists: true = A–Z, false = Z–A
    private boolean sortAscending = true;

    public ListsView(ViewSavedListsController controller,
                     ViewSavedListsViewModel viewModel,
                     SelectedListView selectedListView,
                     ViewManagerModel viewManagerModel,
                     Supplier<String> currentUserSupplier,
                     FileUserDataAccessObject favouritesDao) {

        this.controller = controller;
        this.viewModel = viewModel;
        this.selectedListView = selectedListView;
        this.viewManagerModel = viewManagerModel;
        this.currentUserSupplier = currentUserSupplier;
        this.favouritesDao = favouritesDao;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== TOP PANEL: Title + Action Buttons =====
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        // Row 1: Title
        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel title = new JLabel("My Saved Lists");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        titleRow.add(title);

        // Row 2: Create + Delete + Sort
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton createListButton = new JButton("Create List");
        createListButton.addActionListener(e -> openCreateListDialog());
        buttonRow.add(createListButton);

        JButton deleteListButton = new JButton("Delete List");
        deleteListButton.addActionListener(e -> deleteSelectedList());
        buttonRow.add(deleteListButton);

        JButton sortListsButton = new JButton("Sort A–Z");
        sortListsButton.addActionListener(e -> sortListsAlphabetically(sortListsButton));
        buttonRow.add(sortListsButton);

        topPanel.add(titleRow);
        topPanel.add(buttonRow);

        add(topPanel, BorderLayout.NORTH);

        // ===== CENTER: List Display =====
        JScrollPane scrollPane = new JScrollPane(listDisplay);
        add(scrollPane, BorderLayout.CENTER);

        // ===== BOTTOM: Back to Profile + Error Label =====
        JPanel bottomPanel = new JPanel(new BorderLayout());

        JButton backButton = new JButton("Back to Profile");
        backButton.addActionListener(e -> {
            viewManagerModel.setState(profileViewName);
            viewManagerModel.firePropertyChange();
        });
        bottomPanel.add(backButton, BorderLayout.WEST);

        errorLabel.setForeground(Color.RED);
        bottomPanel.add(errorLabel, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);

        // ===== LIST CLICK HANDLER =====
        listDisplay.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && listDisplay.getSelectedValue() != null) {

                String username = viewModel.getCurrentUsername();
                String listName = listDisplay.getSelectedValue();

                selectedListView.loadList(username, listName);

                viewManagerModel.setState(selectedListView.viewName);
                viewManagerModel.firePropertyChange();

                listDisplay.clearSelection();
            }
        });
    }

    /**
     * Toggle sort of lists between A–Z and Z–A based on what is currently displayed.
     */
    private void sortListsAlphabetically(JButton sortButton) {
        if (listModel.isEmpty()) {
            errorLabel.setText("No lists to sort.");
            return;
        }

        // Copy current items from the model
        java.util.List<String> names = new java.util.ArrayList<>();
        for (int i = 0; i < listModel.size(); i++) {
            names.add(listModel.getElementAt(i));
        }

        // Sort A–Z
        names.sort(String.CASE_INSENSITIVE_ORDER);

        // If we’re currently in descending mode, reverse after sorting
        if (!sortAscending) {
            java.util.Collections.reverse(names);
        }

        // Put back into model
        listModel.clear();
        for (String n : names) {
            listModel.addElement(n);
        }

        // Flip direction for next click
        sortAscending = !sortAscending;

        // Update button text to indicate what the *next* click will do
        if (sortAscending) {
            sortButton.setText("Sort A–Z");
        } else {
            sortButton.setText("Sort Z–A");
        }

        errorLabel.setText("");
    }

    // Called when CardLayout shows this view
    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            loadListsForCurrentUser();
            // Reset sort state whenever we come back to this view
            sortAscending = true;
        }
    }

    // ==== CREATE LIST DIALOG ====
    private void openCreateListDialog() {
        String username = currentUserSupplier.get();
        if (username == null || username.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No user is currently logged in.",
                    "Create List",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JTextField nameField = new JTextField(15);
        JTextField descField = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("List name:"));
        panel.add(nameField);
        panel.add(new JLabel("Description:"));
        panel.add(descField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Create New List",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String listName = nameField.getText().trim();
            String description = descField.getText().trim();

            if (listName.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "List name cannot be empty.",
                        "Create List",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Create list WITH description, empty countries
            favouritesDao.addList(username, listName, description);
            favouritesDao.save();

            // Refresh the list display
            loadListsForCurrentUser();
        }
    }

    // ==== DELETE LIST LOGIC ====
    private void deleteSelectedList() {
        String username = currentUserSupplier.get();
        if (username == null || username.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No user is currently logged in.",
                    "Delete List",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // 1. Try to use the currently selected list
        String selected = listDisplay.getSelectedValue();

        // 2. If nothing is selected, let the user choose from a dropdown IN the popup
        if (selected == null) {
            if (listModel.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "You don't have any lists to delete.",
                        "Delete List",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            JComboBox<String> comboBox = new JComboBox<>();
            for (int i = 0; i < listModel.size(); i++) {
                comboBox.addItem(listModel.get(i));
            }

            int result = JOptionPane.showConfirmDialog(
                    this,
                    comboBox,
                    "Choose a list to delete",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result != JOptionPane.OK_OPTION) {
                return; // user cancelled
            }

            selected = (String) comboBox.getSelectedItem();
            if (selected == null) {
                return;
            }
        }

        // 3. Now confirm deletion of the chosen list
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete the list \"" + selected + "\"?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            favouritesDao.removeList(username, selected);
            favouritesDao.save();
            loadListsForCurrentUser();
        }
    }

    // ==== LOAD + REFRESH UI ====
    private void loadListsForCurrentUser() {
        String username = currentUserSupplier.get();

        if (username == null || username.isEmpty()) {
            errorLabel.setText("No user is currently logged in.");
            listModel.clear();
            return;
        }

        viewModel.setCurrentUsername(username);
        controller.viewLists(username);
        refreshListDisplay();
    }

    private void refreshListDisplay() {
        listModel.clear();

        List<String> names = viewModel.getListNames();
        String error = viewModel.getErrorMessage();

        if (error != null) {
            errorLabel.setText(error);
            return;
        }

        if (names != null && !names.isEmpty()) {
            for (String name : names) {
                listModel.addElement(name);
            }
            errorLabel.setText("");
        } else {
            errorLabel.setText("No lists found.");
        }
    }
}
