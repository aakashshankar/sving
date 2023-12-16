package com.client.components;

import javax.swing.*;

public class LogoutButton extends JButton {

    public LogoutButton() {
        ImageIcon logoutIcon = new ImageIcon("src/main/resources/log-out.png");
        setIcon(logoutIcon);
        setBorder(BorderFactory.createEmptyBorder());
        setContentAreaFilled(false);
    }
}
