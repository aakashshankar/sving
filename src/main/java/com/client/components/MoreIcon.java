package com.client.components;

import javax.swing.*;
import java.util.Objects;

public class MoreIcon extends JButton {

        public MoreIcon() {
            ImageIcon moreIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/more-icon.png")));
            setIcon(moreIcon);
            setBorder(BorderFactory.createEmptyBorder());
            setContentAreaFilled(false);
        }
}
