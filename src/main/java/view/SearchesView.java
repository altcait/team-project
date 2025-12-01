package view;

import interface_adapter.ViewManagerModel;

import javax.swing.*;
import java.awt.*;

/**
 * Search menu screen with 3 navigation buttons for each of the search use cases.
 */
public class SearchesView extends JPanel {

    private final String viewName = "searchesView";

    private final JButton languageButton = new JButton("Search by Language");
    private final JButton regionButton = new JButton("Search by Region");
    private final JButton currencyButton = new JButton("Search by Currency");

    public SearchesView(ViewManagerModel viewManagerModel) {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Choose a Search Option");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        languageButton.addActionListener(e -> {
            viewManagerModel.setState("searchByLanguage");
            viewManagerModel.firePropertyChange();
        });

        regionButton.addActionListener(e -> {
            viewManagerModel.setState("searchByRegion");
            viewManagerModel.firePropertyChange();
        });

        currencyButton.addActionListener(e -> {
            viewManagerModel.setState("searchByCurrency");
            viewManagerModel.firePropertyChange();
        });

        buttonPanel.add(languageButton);
        buttonPanel.add(regionButton);
        buttonPanel.add(currencyButton);

        add(buttonPanel, BorderLayout.CENTER);

    }

    public String getViewName() {
        return viewName;
    }
}
