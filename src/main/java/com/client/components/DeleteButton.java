package com.client.components;

import javax.swing.*;

public class DeleteButton extends JButton {

    public DeleteButton() {
        ImageIcon deleteIcon = new ImageIcon("src/main/resources/delete-button.png");
        setIcon(deleteIcon);
        setBorder(BorderFactory.createEmptyBorder());
        setContentAreaFilled(false);
    }
}
