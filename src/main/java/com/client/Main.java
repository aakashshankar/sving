package com.client;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main extends JFrame {

    private CardLayout cardLayout;

   private JPanel cardPanel;

    public Main() throws HeadlessException, IOException {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        cardPanel.add(new Login(this), "login");
        cardPanel.add(new Register(this), "register");

        add(cardPanel);


    }

    public void updateCardPanel(Component component, String cardName) {
        cardPanel.add(component, cardName);
    }

    public void removeTopPanel() {
        cardPanel.remove(cardPanel.getComponentCount() - 1);
    }

    public void switchTo(String cardName) {
        cardLayout.show(cardPanel, cardName);
    }


    public static void main(String[] args) throws IOException {
        System.setProperty( "apple.awt.application.appearance", "system" );
        FlatMacDarkLaf.setup();

        Main main = new Main();
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setVisible(true);
    }
}
