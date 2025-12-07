package view;

import javax.swing.*;
import java.awt.*;
import entity.Country;
import interface_adapter.search.bycurrency.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * The View for the "Search By Currency" use case.
 *
 * Layout:
 *  - Top: title "Search By Currency"
 *  - Below: Currency boxes + Search button
 *  - Left: list of countries (name + cca3)
 *  - Right: error label + "add country to list" + "back to selected list"
 */
public class SearchByCurrencyView extends JPanel implements PropertyChangeListener {

    private final SearchByCurrencyViewModel viewModel;
    private SearchByCurrencyController controller;

    // UI components
    private final JComboBox<String> currencyComboBox = new JComboBox<>();
    private final JButton searchButton = new JButton("Search");

    private final DefaultListModel<String> countryListModel = new DefaultListModel<>();
    private final JList<String> countryList = new JList<>(countryListModel);

    private final JLabel errorLabel = new JLabel(" ");
    private final JButton addButton = new JButton("save country to list");
    private final JButton backToSelectedListButton = new JButton("back to selected list");
    private final JButton exchangeRateButton = new JButton("View Exchange Rate");

    /**
     * Flag to avoid triggering use cases while updating UI from the ViewModel.
     */
    private boolean updatingFromModel = false;

    public SearchByCurrencyView(SearchByCurrencyViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        // UI setup
        countryList.setVisibleRowCount(10);
        countryList.setFont(countryList.getFont().deriveFont(14f));

        errorLabel.setForeground(Color.RED);

        currencyComboBox.setPreferredSize(new Dimension(180, 25));

        // Make addButton and backToSelectedListButton the same size
        Dimension addSize = addButton.getPreferredSize();
        Dimension backSize = backToSelectedListButton.getPreferredSize();
        int width = Math.max(addSize.width, backSize.width);
        int height = Math.max(addSize.height, backSize.height);
        Dimension unified = new Dimension(width, height);
        addButton.setPreferredSize(unified);
        backToSelectedListButton.setPreferredSize(unified);

        // Layout
        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Title at the top
        JLabel titleLabel = new JLabel("Search By Currency", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        this.add(titleLabel, BorderLayout.NORTH);

        // Main content panel
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(new EmptyBorder(10, 0, 0, 0));
        this.add(content, BorderLayout.CENTER);

        // Top row: Currency Search
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        topRow.add(new JLabel("Currency:"));
        topRow.add(currencyComboBox);
        topRow.add(searchButton);
        content.add(topRow, BorderLayout.NORTH);

        // Center row: left (country list) + right (error + buttons)
        JPanel centerRow = new JPanel(new BorderLayout());
        content.add(centerRow, BorderLayout.CENTER);

        // Left: country list in a bordered panel
        JPanel listPanel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(countryList);
        listPanel.add(scrollPane, BorderLayout.CENTER);
        listPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 20, 10, 20),
                BorderFactory.createLineBorder(Color.GRAY)));
        centerRow.add(listPanel, BorderLayout.CENTER);

        // Right: error label + add + back to profile
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(new EmptyBorder(10, 40, 10, 10));

        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backToSelectedListButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exchangeRateButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        rightPanel.add(errorLabel);
        rightPanel.add(Box.createVerticalStrut(15));
        rightPanel.add(addButton);
        rightPanel.add(Box.createVerticalStrut(15));
        rightPanel.add(backToSelectedListButton);
        rightPanel.add(Box.createVerticalStrut(15));
        rightPanel.add(exchangeRateButton);

        exchangeRateButton.addActionListener(e -> {
            if (controller != null) {
                String currency = (String) currencyComboBox.getSelectedItem();
                if (currency != null && !currency.isEmpty()) {
                    controller.onExchangeRateClicked(currency);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Please select a currency first.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        centerRow.add(rightPanel, BorderLayout.EAST);

        // Listeners
        // Currency selection: then ask controller to load subregions
        currencyComboBox.addActionListener(e -> {
            if (updatingFromModel) {
                return;
            }
            Object selected = currencyComboBox.getSelectedItem();
            if (selected != null && controller != null) {
                String currency = selected.toString();
                controller.onCurrencySelected(currency);
            }
        });

        // Search by Currency
        searchButton.addActionListener(e -> {
            if (controller == null) {
                return;
            }
            String currency = (String) currencyComboBox.getSelectedItem();
            controller.onSearch(currency);
        });

        // Click add country button
        addButton.addActionListener(e -> {
            if (controller != null) {
                controller.onAddCountryButtonClicked();
            }
        });

        // Click back to selected list
        backToSelectedListButton.addActionListener(e -> {
            if (controller != null) {
                controller.onBackToSelectedListButtonClicked();
            }
        });
    }

    public String getViewName() {
        return viewModel.getViewName();
    }


    public void setController(SearchByCurrencyController controller) {
        this.controller = controller;
        // Once controller is available, we can safely load Currencies
        this.controller.loadCurrencies();
    }

    /**
     * react to state changes from the ViewModel and refresh the UI
     * response to change in view model
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!"state".equals(evt.getPropertyName())) {
            return;
        }

        updatingFromModel = true;
        try {
            SearchByCurrencyState state = (SearchByCurrencyState) evt.getNewValue();
            if (state == null) {
                return;
            }

            // Error message
            String err = state.getErrorMessage();
            if (err == null || err.isEmpty()) {
                errorLabel.setText(" ");
            } else {
                errorLabel.setText(err);
            }

            // Currency combo box
            List<String> currencies = state.getCurrencyOptions();
            if (currencies != null) {
                DefaultComboBoxModel<String> currencyModel = new DefaultComboBoxModel<>();
                for (String r : currencies) {
                    currencyModel.addElement(r);
                }
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
        }
        finally {
            updatingFromModel = false;
        }
    }
}
