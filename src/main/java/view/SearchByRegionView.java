package view;

import entity.Country;
import interface_adapter.search.byregion.SearchByRegionController;
import interface_adapter.search.byregion.SearchByRegionState;
import interface_adapter.search.byregion.SearchByRegionViewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * The View for the "Search By Region" use case.
 */
public class SearchByRegionView extends JPanel implements PropertyChangeListener {

    private final SearchByRegionViewModel viewModel;
    private SearchByRegionController controller;

    // UI components
    private final JComboBox<String> regionComboBox = new JComboBox<>();
    private final JComboBox<String> subregionComboBox = new JComboBox<>();
    private final JButton searchButton = new JButton("Search");

    private final DefaultListModel<String> countryListModel = new DefaultListModel<>();
    private final JList<String> countryList = new JList<>(countryListModel);

    private final JLabel errorLabel = new JLabel(" ");
    private final JButton addButton = new JButton("save country to list");
    private final JButton backToSelectedListButton = new JButton("back to selected list");

    private boolean updatingFromModel = false;

    public SearchByRegionView(SearchByRegionViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        countryList.setVisibleRowCount(10);
        countryList.setFont(countryList.getFont().deriveFont(14f));

        errorLabel.setForeground(Color.RED);

        regionComboBox.setPreferredSize(new Dimension(180, 25));
        subregionComboBox.setPreferredSize(new Dimension(180, 25));

        Dimension addSize = addButton.getPreferredSize();
        Dimension backSize = backToSelectedListButton.getPreferredSize();
        int width = Math.max(addSize.width, backSize.width);
        int height = Math.max(addSize.height, backSize.height);
        Dimension unified = new Dimension(width, height);
        addButton.setPreferredSize(unified);
        backToSelectedListButton.setPreferredSize(unified);

        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Search By Region", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        this.add(titleLabel, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(new EmptyBorder(10, 0, 0, 0));
        this.add(content, BorderLayout.CENTER);

        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        topRow.add(new JLabel("Region:"));
        topRow.add(regionComboBox);
        topRow.add(new JLabel("Subregion:"));
        topRow.add(subregionComboBox);
        topRow.add(searchButton);
        content.add(topRow, BorderLayout.NORTH);

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
        backToSelectedListButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        rightPanel.add(errorLabel);
        rightPanel.add(Box.createVerticalStrut(15));
        rightPanel.add(addButton);
        rightPanel.add(Box.createVerticalStrut(30));
        rightPanel.add(backToSelectedListButton);

        centerRow.add(rightPanel, BorderLayout.EAST);

        regionComboBox.addActionListener(e -> {
            if (updatingFromModel) {
                return;
            }
            Object selected = regionComboBox.getSelectedItem();
            if (selected != null && controller != null) {
                String region = selected.toString();
                controller.onRegionSelected(region);
            }
        });

        searchButton.addActionListener(e -> {
            if (controller == null) {
                return;
            }
            String region = (String) regionComboBox.getSelectedItem();
            String subregion = (String) subregionComboBox.getSelectedItem();
            controller.onSearch(region, subregion);
        });

        addButton.addActionListener(e -> {
            if (controller != null) {
                controller.onAddCountryButtonClicked();
            }
        });

        backToSelectedListButton.addActionListener(e -> {
            if (controller != null) {
                controller.onBackToSelectedListButtonClicked();
            }
        });
    }

    public String getViewName() {
        return viewModel.getViewName();
    }

    public void setController(SearchByRegionController controller) {
        this.controller = controller;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!"state".equals(evt.getPropertyName())) {
            return;
        }

        updatingFromModel = true;
        try {
            SearchByRegionState state = (SearchByRegionState) evt.getNewValue();
            if (state == null) {
                return;
            }

            String err = state.getErrorMessage();
            if (err == null || err.isEmpty()) {
                errorLabel.setText(" ");
            } else {
                errorLabel.setText(err);
            }

            // Region combo box
            List<String> regions = state.getRegionOptions();
            if (regions != null) {
                DefaultComboBoxModel<String> regionModel = new DefaultComboBoxModel<>();
                for (String r : regions) {
                    regionModel.addElement(r);
                }
                regionComboBox.setModel(regionModel);

                if (state.getSelectedRegion() != null) {
                    regionComboBox.setSelectedItem(state.getSelectedRegion());
                } else {
                    regionComboBox.setSelectedIndex(-1);
                }
            }

            // Subregion combo box
            List<String> subregions = state.getSubregionOptions();
            if (subregions != null) {
                DefaultComboBoxModel<String> subregionModel = new DefaultComboBoxModel<>();
                for (String s : subregions) {
                    subregionModel.addElement(s);
                }
                subregionComboBox.setModel(subregionModel);

                if (state.getSelectedSubregion() != null) {
                    subregionComboBox.setSelectedItem(state.getSelectedSubregion());
                } else {
                    subregionComboBox.setSelectedIndex(-1);
                }
            } else {
                // NEW: when there are no subregion options, clear the combo box
                subregionComboBox.setModel(new DefaultComboBoxModel<>());
                subregionComboBox.setSelectedIndex(-1);
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

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);

        if (aFlag && controller != null) {
            SearchByRegionState newState = new SearchByRegionState();
            viewModel.setState(newState);
            viewModel.firePropertyChange();
            controller.loadRegions();
        }
    }
}