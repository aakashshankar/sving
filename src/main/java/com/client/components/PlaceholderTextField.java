package com.client.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.RoundRectangle2D;

public class PlaceholderTextField extends JTextField implements FocusListener {

    private static final int ARC_WIDTH = 16; // Adjust the roundness
    private static final int ARC_HEIGHT = 16;
    private String placeholder;

    public PlaceholderTextField(String placeholder) {
        setFont(new Font("Segoe UI", Font.PLAIN, 18));
        this.placeholder = placeholder;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (getText().isEmpty() && ! (FocusManager.getCurrentKeyboardFocusManager().getFocusOwner() == this)) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ARC_WIDTH, ARC_HEIGHT);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getDisabledTextColor());
            g2.drawString(placeholder, getInsets().left, g.getFontMetrics().getMaxAscent() + getInsets().top);

            g2.dispose();
        }

    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Paints the rounded border
        g2.setColor(Color.GRAY);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ARC_WIDTH, ARC_HEIGHT);

        g2.dispose();
    }

    @Override
    public boolean contains(int x, int y) {
        // If the mouse coordinates are within the rounded rectangle
        return new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), ARC_WIDTH, ARC_HEIGHT).contains(x, y);
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public void focusGained(FocusEvent e) {
        setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
    }

    @Override
    public void focusLost(FocusEvent e) {

    }
}