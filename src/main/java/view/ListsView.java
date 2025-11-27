package view;

import interface_adapter.RetrieveSavedLists.ViewSavedListsController;
import interface_adapter.RetrieveSavedLists.ViewSavedListsViewModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ListsView extends JPanel {

    // Used by ViewManager / AppBuilder to refer to this screen
    public final String viewName = "lists";

    private final ViewSavedListsController controller;
    private final ViewSavedListsViewModel viewModel;

    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> listDisplay = new JList<>(listModel);
    private final JLabel errorLabel = new JLabel("");

    public ListsView(ViewSavedListsController controller,
                     ViewSavedListsViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;

        this.setLayout(new BorderLayout());

        // --- Top panel with a "Load Lists" button ---
        JButton loadButton = new JButton("Load My Lists");

        loadButton.addActionListener(e -> {
            // TEMP: hard-code a username for now so you can see it work.
            // Later this will come from the logged-in user / profile.
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
