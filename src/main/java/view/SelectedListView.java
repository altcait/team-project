package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewSelectedList.ViewSelectedListController;
import interface_adapter.ViewSelectedList.ViewSelectedListViewModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SelectedListView extends JPanel {

    public final String viewName = "selected_list";

    private final ViewSelectedListController controller;
    private final ViewSelectedListViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    // card name of the lists screen
    private final String listsViewName = "lists";

    private final JLabel titleLabel = new JLabel("List: ");
    private final JTextArea descriptionArea = new JTextArea(3, 30);

    private final DefaultListModel<String> countriesModel = new DefaultListModel<>();
    private final JList<String> countriesList = new JList<>(countriesModel);

    private final JLabel errorLabel = new JLabel("");

    public SelectedListView(ViewSelectedListController controller,
                            ViewSelectedListViewModel viewModel,
                            ViewManagerModel viewManagerModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== TOP: title + Edit Description button =====
        JPanel topPanel = new JPanel(new BorderLayout());
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
        topPanel.add(titleLabel, BorderLayout.WEST);

        // NEW: Edit Description button
        JButton editDescriptionButton = new JButton("Edit Description");
        editDescriptionButton.addActionListener(e -> editDescription());
        topPanel.add(editDescriptionButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // ===== CENTER: description + countries list =====
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false); // stays non-editable; we use dialog instead

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

    /** Called from ListsView when a list is selected. */
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

    /**
     * Opens a dialog that lets the user edit the list description.
     * For now this only updates the View + ViewModel.
     * Later, a separate use case can save it to JSON.
     */
    private void editDescription() {
        String current = descriptionArea.getText();
        String newDesc = JOptionPane.showInputDialog(
                this,
                "Edit list description:",
                current
        );

        if (newDesc != null) { // user pressed OK (not Cancel)
            descriptionArea.setText(newDesc);
            viewModel.setDescription(newDesc);
            // TODO: later call a "UpdateListDescription" controller/use case
        }
    }
}
