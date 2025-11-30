package view;

import entity.Country;
import interface_adapter.search.bycurrency.SearchByCurrencyController;
import interface_adapter.search.bycurrency.SearchByCurrencyState;
import interface_adapter.search.bycurrency.SearchByCurrencyViewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**

 * The View for the "Search By Currency" use case.
 *
 * Layout:
 * * Top: title "Search By Currency"
 * * Below: Currency combo box + Search button
 * * Left: list of countries (name + cca3)
 * * Right: error label + "add country to list" + "back to profile"
 */
public class SearchByCurrencyView extends JPanel implements PropertyChangeListener {

    private final SearchByCurrencyViewModel viewModel;
    private SearchByCurrencyController controller;

    private final JComboBox<String> currencyComboBox = new JComboBox<>();
    private final JButton searchButton = new JButton("Search");

    private final DefaultListModel<String> countryListModel = new DefaultListModel<>();
    private final JList<String> countryList = new JList<>(countryListModel);

    private final JLabel errorLabel = new JLabel(" ");
    private final JButton addButton = new JButton("add country to list");
    private final JButton backToProfileButton = new JButton("back to profile");

    private boolean updatingFromModel = false;

    public SearchByCurrencyView(SearchByCurrencyViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        // UI setup
        countryList.setVisibleRowCount(10);
        countryList.setFont(countryList.getFont().deriveFont(14f));

        errorLabel.setForeground(Color.RED);

        currencyComboBox.setPreferredSize(new Dimension(180, 25));

        // Make addButton and backToProfileButton the same size
        Dimension addSize = addButton.getPreferredSize();
        Dimension backSize = backToProfileButton.getPreferredSize();
        int width = Math.max(addSize.width, backSize.width);
        int height = Math.max(addSize.height, backSize.height);
        Dimension unified = new Dimension(width, height);
        addButton.setPreferredSize(unified);
        backToProfileButton.setPreferredSize(unified);

        // Layout
        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Search By Currency", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        this.add(titleLabel, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(new EmptyBorder(10, 0, 0, 0));
        this.add(content, BorderLayout.CENTER);

        // Top row: currency + search
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        topRow.add(new JLabel("Currency:"));
        topRow.add(currencyComboBox);
        topRow.add(searchButton);
        content.add(topRow, BorderLayout.NORTH);

        // Center row: left (country list) + right (error + buttons)
        JPanel centerRow = new JPanel(new BorderLayout());
        content.add(centerRow, BorderLayout.CENTER);

        JPanel listPanel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(countryList);
        listPanel.add(scrollPane, BorderLayout.CENTER);
        listPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 20, 10, 20),
                BorderFactory.createLineBorder(Color.GRAY)));
        centerRow.add(listPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(new EmptyBorder(10, 40, 10, 10));

        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backToProfileButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        rightPanel.add(errorLabel);
        rightPanel.add(Box.createVerticalStrut(15));
        rightPanel.add(addButton);
        rightPanel.add(Box.createVerticalStrut(30));
        rightPanel.add(backToProfileButton);

        centerRow.add(rightPanel, BorderLayout.EAST);

        // Listeners
        searchButton.addActionListener((ActionEvent e) -> {
            if (controller != null) {
                String currency = (String) currencyComboBox.getSelectedItem();
                controller.onSearch(currency);
            }
        });

        addButton.addActionListener((ActionEvent e) -> {
            if (controller != null) {
                controller.onAddCountryButtonClicked();
            }
        });

        backToProfileButton.addActionListener((ActionEvent e) -> {
            if (controller != null) {
                controller.onBackToProfileButtonClicked();
            }
        });

    }

    public String getViewName() {
        return viewModel.getViewName();
    }

    public void setController(SearchByCurrencyController controller) {
        this.controller = controller;
        if (controller != null) {
            this.controller.loadCurrencies();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!"state".equals(evt.getPropertyName())) {
            return;
        }

        updatingFromModel = true;
        try {
            SearchByCurrencyState state = (SearchByCurrencyState) evt.getNewValue();
            if (state == null) return;

            // Error message
            String err = state.getErrorMessage();
            errorLabel.setText((err == null || err.isEmpty()) ? " " : err);

            // Currency combo box
            List<String> currencies = state.getCurrencyOptions();
            if (currencies != null) {
                DefaultComboBoxModel<String> currencyModel = new DefaultComboBoxModel<>();
                for (String c : currencies) currencyModel.addElement(c);
                currencyComboBox.setModel(currencyModel);
                if (state.getSelectedCurrency() != null) {
                    currencyComboBox.setSelectedItem(state.getSelectedCurrency());
                } else {
                    currencyComboBox.setSelectedIndex(-1);
                }
            }

            // Country list
            List<Country> countries = state.getCountries();
            countryListModel.clear();
            if (countries != null) {
                for (Country c : countries) {
                    String line = c.getName() + " (" + c.getCca3() + ")";
                    countryListModel.addElement(line);
                }
            }
        } finally {
            updatingFromModel = false;
        }

    }
}
