package view;

import javax.swing.*;
import java.awt.*;

public class SearchByLanguageView extends JPanel {
    public static final String viewName = "searchByLanguage";

    public SearchByLanguageView() {
        setLayout(new BorderLayout());
        add(new JLabel("Search by Language Screen (placeholder)", SwingConstants.CENTER), BorderLayout.CENTER);
    }

    public String getViewName() {
        return viewName;
    }
}
