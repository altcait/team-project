package view;

import interface_adapter.ViewSelectedList.ViewSelectedListController;
import interface_adapter.ViewSelectedList.ViewSelectedListViewModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SelectedListView extends JPanel {

    // Used by ViewManager / AppBuilder
    public final String viewName = "selected_list";

    private final ViewSelectedListController controller;
    private final ViewSelectedListViewModel viewModel;

    private final JLabel titleLabel = new JLabel("List: ");
    private final JTextArea descriptionArea = new JTextArea(3, 30);

    private final DefaultListModel<String> countriesModel = new DefaultListModel<>();
    private final JList<String> countriesList = new JList<>(countriesModel);

    private final JLabel errorLabel = new JLabel("");

    public SelectedListView(ViewSelectedListController controller,
                            ViewSelectedListViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;

        setLayout(new BorderLayout());

        // ===== TOP: title =====
        JPanel topPanel = new JPanel(new BorderLayout());
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
        topPanel.add(titleLabel, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        // ===== CENTER: description + countries list =====
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JScrollPane(descriptionArea), BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(countriesList), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // ===== BOTTOM: back button (logic wired in ListsView later) + error =====
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back to Lists");
        bottomPanel.add(backButton, BorderLayout.WEST);

        errorLabel.setForeground(Color.RED);
        bottomPanel.add(errorLabel, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Called from ListsView when the user selects a list.
     * This runs the use case and refreshes the screen.
     */
    public void loadList(String username, String listName) {
        viewModel.setCurrentUsername(username);
        viewModel.setCurrentListName(listName);
        controller.viewSelectedList(username, listName);
        refreshFromViewModel();
    }

    /** Read data from the ViewModel and show it on screen. */
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
}