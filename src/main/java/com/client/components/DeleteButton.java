package com.client.components;

import javax.swing.*;
import java.util.Objects;

public class DeleteButton extends JButton {

    public DeleteButton() {
        ImageIcon deleteIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/delete-button.png")));
        setIcon(deleteIcon);
        setBorder(BorderFactory.createEmptyBorder());
        setContentAreaFilled(false);
    }
}
