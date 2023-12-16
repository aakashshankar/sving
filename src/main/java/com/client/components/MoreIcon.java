package com.client.components;

import javax.swing.*;

public class MoreIcon extends JButton {

        public MoreIcon() {
            ImageIcon moreIcon = new ImageIcon("src/main/resources/more-icon.png");
            setIcon(moreIcon);
            setBorder(BorderFactory.createEmptyBorder());
            setContentAreaFilled(false);
        }
}
