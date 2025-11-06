package fr.sparadrap.ecf.view.swingview;

import javax.swing.*;
import java.awt.*;


public class MainPanel extends JPanel {
    public MainPanel() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.black);
        this.setBorder(BorderFactory.createEmptyBorder(
                3, 3,
                3, 3
        ));
    }

    public void showView(JComponent component) {
        this.removeAll();
        this.add(component, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }


}
