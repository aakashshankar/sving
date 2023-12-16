package com.client.components;

import javax.swing.*;

public class BackButton extends JButton {

        public BackButton() {
            ImageIcon backIcon = new ImageIcon("src/main/resources/back-button.png");
            setIcon(backIcon);
            setBorder(BorderFactory.createEmptyBorder());
            setContentAreaFilled(false);
        }

}
