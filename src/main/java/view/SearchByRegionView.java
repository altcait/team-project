package view;

import javax.swing.*;
import java.awt.*;

public class SearchByRegionView extends JPanel {
    public static final String viewName = "searchByRegion";

    public SearchByRegionView() {
        setLayout(new BorderLayout());
        add(new JLabel("Search by Region Screen (placeholder)", SwingConstants.CENTER), BorderLayout.CENTER);
    }

    public String getViewName() {
        return viewName;
    }
}
