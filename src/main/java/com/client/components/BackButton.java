package com.client.components;

import javax.swing.*;
import java.util.Objects;

public class BackButton extends JButton {

        public BackButton() {
            ImageIcon backIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/back-button.png")));
            setIcon(backIcon);
            setBorder(BorderFactory.createEmptyBorder());
            setContentAreaFilled(false);
        }

}
