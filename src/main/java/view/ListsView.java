package view;

import interface_adapter.RetrieveSavedLists.ViewSavedListsController;
import interface_adapter.RetrieveSavedLists.ViewSavedListsViewModel;
import interface_adapter.ViewManagerModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ListsView extends JPanel {

    public final String viewName = "lists";

    private final ViewSavedListsController controller;
    private final ViewSavedListsViewModel viewModel;

    // New: needed to open the selected list screen
    private final SelectedListView selectedListView;
    private final ViewManagerModel viewManagerModel;

    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> listDisplay = new JList<>(listModel);
    private final JLabel errorLabel = new JLabel("");

    public ListsView(ViewSavedListsController controller,
                     ViewSavedListsViewModel viewModel,
                     SelectedListView selectedListView,
                     ViewManagerModel viewManagerModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.selectedListView = selectedListView;
        this.viewManagerModel = viewManagerModel;

        this.setLayout(new BorderLayout());

        // --- Top panel with a "Load Lists" button ---
        JButton loadButton = new JButton("Load My Lists");

        loadButton.addActionListener(e -> {
            // TEMP: hard-code a username for now so you can see it work.
            String username = "testUser";
            viewModel.setCurrentUsername(username);

            controller.viewLists(username);
            refreshListDisplay();
        });

        JPanel topPanel = new JPanel();
        topPanel.add(loadButton);

        // --- Center: list of saved lists ---
        JScrollPane scrollPane = new JScrollPane(listDisplay);

        // --- Bottom: error label ---
        errorLabel.setForeground(Color.RED);

        this.add(topPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(errorLabel, BorderLayout.SOUTH);

        // ==== NEW: when user clicks a list, open SelectedListView ====
        listDisplay.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && listDisplay.getSelectedValue() != null) {
                String username = viewModel.getCurrentUsername();
                String listName = listDisplay.getSelectedValue();

                // Ask SelectedListView to load that list
                selectedListView.loadList(username, listName);

                // Switch to the selected_list screen
                viewManagerModel.setState(selectedListView.viewName);
                viewManagerModel.firePropertyChange();
            }
        });
    }

    /** Re-read data from the ViewModel and show it in the JList. */
    private void refreshListDisplay() {
        listModel.clear();

        List<String> names = viewModel.getListNames();
        String error = viewModel.getErrorMessage();

        if (error != null) {
            errorLabel.setText(error);
            return;
        }

        if (names != null) {
            for (String name : names) {
                listModel.addElement(name);
            }
            errorLabel.setText("");
        } else {
            errorLabel.setText("No lists found.");
        }
    }
}
