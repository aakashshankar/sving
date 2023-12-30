package com.client.components;

import javax.swing.*;
import java.util.Objects;

public class LogoutButton extends JButton {

    public LogoutButton() {
        ImageIcon logoutIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/log-out.png")));
        setIcon(logoutIcon);
        setBorder(BorderFactory.createEmptyBorder());
        setContentAreaFilled(false);
    }
}
