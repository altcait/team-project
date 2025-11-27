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
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== TOP: simple header (no button) =====
        JLabel header = new JLabel("My Saved Lists");
        header.setFont(header.getFont().deriveFont(Font.BOLD, 16f));
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(header);

        // ===== CENTER: list of saved lists =====
        JScrollPane scrollPane = new JScrollPane(listDisplay);

        // ===== BOTTOM: error label =====
        errorLabel.setForeground(Color.RED);

        this.add(topPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(errorLabel, BorderLayout.SOUTH);

        // ==== When user clicks a list, open SelectedListView ====
        listDisplay.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && listDisplay.getSelectedValue() != null) {
                String username = viewModel.getCurrentUsername();
                String listName = listDisplay.getSelectedValue();

                selectedListView.loadList(username, listName);

                viewManagerModel.setState(selectedListView.viewName);
                viewManagerModel.firePropertyChange();
            }
        });

        // ===== AUTO-LOAD LISTS when this view is created =====
        // TODO: replace "testUser" with the real logged-in username later.
        String username = "testUser";
        viewModel.setCurrentUsername(username);
        controller.viewLists(username);
        refreshListDisplay();
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
