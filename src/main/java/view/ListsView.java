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

    // name of the profile view in the card layout
    private final String profileViewName = "profile";

    public ListsView(ViewSavedListsController controller,
                     ViewSavedListsViewModel viewModel,
                     SelectedListView selectedListView,
                     ViewManagerModel viewManagerModel) {

        this.controller = controller;
        this.viewModel = viewModel;
        this.selectedListView = selectedListView;
        this.viewManagerModel = viewManagerModel;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== TOP: title + Back to Profile =====
        JPanel topPanel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("My Saved Lists");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        topPanel.add(title, BorderLayout.WEST);

        JButton backButton = new JButton("Back to Profile");
        backButton.addActionListener(e -> {
            viewManagerModel.setState(profileViewName);
            viewManagerModel.firePropertyChange();
        });
        topPanel.add(backButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // ===== CENTER: list of lists =====
        JScrollPane scrollPane = new JScrollPane(listDisplay);
        add(scrollPane, BorderLayout.CENTER);

        // ===== BOTTOM: error label =====
        errorLabel.setForeground(Color.RED);
        add(errorLabel, BorderLayout.SOUTH);

        // When user clicks a list name, open SelectedListView
        listDisplay.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && listDisplay.getSelectedValue() != null) {
                String username = viewModel.getCurrentUsername();
                String listName = listDisplay.getSelectedValue();

                selectedListView.loadList(username, listName);

                viewManagerModel.setState(selectedListView.viewName);
                viewManagerModel.firePropertyChange();

                // clear selection so user can click again after coming back
                listDisplay.clearSelection();
            }
        });

        // ===== AUTO-LOAD LISTS when this view is created =====
        // TODO: replace "testUser" with real logged-in username once profile is done.
        String username = "testUser";
        viewModel.setCurrentUsername(username);
        controller.viewLists(username);
        refreshListDisplay();
    }

    /** Reads data from the ViewModel and shows it in the JList. */
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
