package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.search.by_region.SearchByRegionViewModel;
import javax.swing.*;
import java.awt.*;

/**
 * Search menu screen with 3 navigation buttons for each of the search use cases.
 */
public class SearchesView extends JPanel {

    private final String viewName = "searchesView";

    private final JButton languageButton = new JButton("Search by Language");
    private final JButton regionButton   = new JButton("Search by Region");
    private final JButton currencyButton = new JButton("Search by Currency");

    private final JButton backProfileButton = new JButton("Back to Profile"); // Added back button
    private final JButton backListButton = new JButton("Back to List"); // Added back button
    private final String profileViewName = "profile";
    private final String listsViewName = "lists";
    private final String languageViewName = "searchByLanguage";
    private final String currencyViewName = "searchByCurrency";
    private final String regionViewName   = new SearchByRegionViewModel().getViewName();

    public SearchesView(ViewManagerModel viewManagerModel) {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Choose a Search Option");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        languageButton.addActionListener(e -> {
            viewManagerModel.setState(languageViewName);
            viewManagerModel.firePropertyChange();
        });

        regionButton.addActionListener(e -> {
            viewManagerModel.setState(regionViewName);
            viewManagerModel.firePropertyChange();
        });

        currencyButton.addActionListener(e -> {
            viewManagerModel.setState(currencyViewName);
            viewManagerModel.firePropertyChange();
        });

        buttonPanel.add(languageButton);
        buttonPanel.add(regionButton);
        buttonPanel.add(currencyButton);

        add(buttonPanel, BorderLayout.CENTER);

        // add a "Back to List " button
        JPanel backPanel = new JPanel();
        backListButton.addActionListener(e -> {
            viewManagerModel.setState(listsViewName);
            viewManagerModel.firePropertyChange();
        });
        backProfileButton.addActionListener(e -> {
            viewManagerModel.setState(profileViewName);
            viewManagerModel.firePropertyChange();
        });
        backPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        backPanel.add(backListButton);
        backPanel.add(backProfileButton);
        add(backPanel, BorderLayout.SOUTH);

    }

    public String getViewName() {
        return viewName;
    }


}
