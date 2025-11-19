package view;

import entity.Country;
import interface_adapter.search.byregion.SearchByRegionController;
import interface_adapter.search.byregion.SearchByRegionState;
import interface_adapter.search.byregion.SearchByRegionViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Swing view for the Search by Region feature.
 */
public class SearchByRegionView extends JPanel implements PropertyChangeListener {

    private final SearchByRegionController controller;
    private final SearchByRegionViewModel viewModel;

    private final JComboBox<String> regionComboBox = new JComboBox<>();
    private final JComboBox<String> subregionComboBox = new JComboBox<>();
    private final JButton searchButton = new JButton("Search");
    private final JList<String> countryList = new JList<>();
    private final JLabel errorLabel = new JLabel();

    public SearchByRegionView(SearchByRegionController controller,
                              SearchByRegionViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;

        this.viewModel.addPropertyChangeListener(this);

        buildLayout();
        registerListeners();

        // 初始化时加载 regions
        this.controller.loadRegions();
    }

    private void buildLayout() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        topPanel.add(new JLabel("Region:"));
        topPanel.add(regionComboBox);

        topPanel.add(new JLabel("Subregion:"));
        topPanel.add(subregionComboBox);

        topPanel.add(searchButton);

        add(topPanel, BorderLayout.NORTH);

        add(new JScrollPane(countryList), BorderLayout.CENTER);

        errorLabel.setForeground(Color.RED);
        add(errorLabel, BorderLayout.SOUTH);
    }

    private void registerListeners() {
        regionComboBox.addActionListener(e -> {
            String selected = (String) regionComboBox.getSelectedItem();
            controller.onRegionSelected(selected);
        });

        searchButton.addActionListener(e -> {
            String region = (String) regionComboBox.getSelectedItem();
            String subregion = (String) subregionComboBox.getSelectedItem();
            controller.onSearchByRegionAndSubregion(region, subregion);
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SearchByRegionState state = viewModel.getState();

        // Update the region dropdown box
        List<String> regions = state.getRegionOptions();
        if (regions != null) {
            regionComboBox.removeAllItems();
            for (String r : regions) {
                regionComboBox.addItem(r);
            }
        }

        // Update the subregion dropdown box
        List<String> subregions = state.getSubregionOptions();
        if (subregions != null) {
            subregionComboBox.removeAllItems();
            for (String s : subregions) {
                subregionComboBox.addItem(s);
            }
        }

        // Update the countries list
        List<Country> countries = state.getCountries();
        if (countries != null) {
            DefaultListModel<String> model = new DefaultListModel<>();
            for (Country c : countries) {
                model.addElement(c.getName() + " (" + c.getCca3() + ")");
            }
            countryList.setModel(model);
        }

        // Updata the error message
        if (state.getErrorMessage() != null) {
            errorLabel.setText(state.getErrorMessage());
        } else {
            errorLabel.setText("");
        }
    }
}