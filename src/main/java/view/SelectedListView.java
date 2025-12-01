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

        // ===== TOP: title row + button row =====
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        // Row 1: List title
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleRow.add(titleLabel);

        // Row 2: Edit Description + Search + Sort buttons
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

        // --- NEW: Sort button (placeholder) ---
        JButton sortButton = new JButton("Sort");
        sortButton.addActionListener(e -> JOptionPane.showMessageDialog(
                this,
                "Sort feature not implemented yet.",
                "Sort",
                JOptionPane.INFORMATION_MESSAGE
        ));
        buttonRow.add(sortButton);

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
        // If we're already showing this same list, just use whatever is in the
        // ViewModel (which includes any edited description) and DON'T reload
        // from the JSON file.
        if (username.equals(viewModel.getCurrentUsername())
                && listName.equals(viewModel.getCurrentListName())) {
            refreshFromViewModel();
            return;
        }

        // First time opening this list (or switching to a different list):
        // load from the use case / DAO
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
}
