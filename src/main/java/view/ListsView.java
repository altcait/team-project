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

        // ===== TOP PANEL: Title + Action Buttons =====
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        // Row 1: Title
        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel title = new JLabel("My Saved Lists");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        titleRow.add(title);

        // Row 2: Create + Delete
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton createListButton = new JButton("Create List");
        createListButton.addActionListener(e ->
                JOptionPane.showMessageDialog(
                        this,
                        "Create List feature not implemented yet.",
                        "Create List",
                        JOptionPane.INFORMATION_MESSAGE
                )
        );
        buttonRow.add(createListButton);

        JButton deleteListButton = new JButton("Delete List");
        deleteListButton.addActionListener(e ->
                JOptionPane.showMessageDialog(
                        this,
                        "Delete List feature not implemented yet.",
                        "Delete List",
                        JOptionPane.INFORMATION_MESSAGE
                )
        );
        buttonRow.add(deleteListButton);

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

        // ===== AUTO-LOAD LISTS =====
        String username = "testUser"; // TODO: replace with profile username
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
