package fr.sparadrap.ecf.view.swingview;

import fr.sparadrap.ecf.view.swingview.menu.MainMenu;

import javax.swing.*;
import java.awt.*;


public class MainFrame extends JFrame {

    public MainFrame() {
        this.setTitle("Sparadrap - Gestion Pharmacie");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1600, 900);
        this.setMinimumSize(new Dimension(1200, 700));
        this.setLocationRelativeTo(null);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel contentPane = new JPanel();
        this.setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());

        MainPanel mainPanel = new MainPanel();
        MainMenu menuPanel = new MainMenu(mainPanel);
        // Header avec titre de l'application
        JPanel headerPanel = createHeaderPanel();

        contentPane.add(menuPanel, BorderLayout.WEST);
        contentPane.add(mainPanel, BorderLayout.CENTER);
        contentPane.add(headerPanel, BorderLayout.NORTH);

    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());

        header.setPreferredSize(new Dimension(0, 60));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        header.setBackground(Color.blue);

        JLabel titleLabel = new JLabel("SPARADRAP - Système de Gestion Pharmacie");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JLabel versionLabel = new JLabel("Version 1.0");

        versionLabel.setForeground(Color.WHITE);

        header.add(titleLabel, BorderLayout.WEST);
        header.add(versionLabel, BorderLayout.EAST);

        return header;
    }


}
