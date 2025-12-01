package view;

import javax.swing.*;
import java.awt.*;

public class SearchByCurrencyView extends JPanel {
    public static final String viewName = "searchByCurrency";

    public SearchByCurrencyView() {
        setLayout(new BorderLayout());
        add(new JLabel("Search by Currency Screen (placeholder)", SwingConstants.CENTER), BorderLayout.CENTER);
    }

    public String getViewName() {
        return viewName;
    }
}
