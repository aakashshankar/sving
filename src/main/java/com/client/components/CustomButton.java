package com.client.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class CustomButton extends JButton {
    private static final Color BUTTON_COLOR = new Color(33, 150, 243); // This is a blue color similar to Material Design Blue 500
    private static final Color HOVER_COLOR = new Color(100, 200, 229); // This is a blue color similar to Material Design Blue 600

    private boolean hover = false;

    public CustomButton(String text, int fontSize) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, fontSize)); // Font style and size
        setBorderPainted(false); // No border painting by default
        setMargin(new Insets(5, 15, 5, 15)); // Margin for the text
        addHoverEffect();
    }

    private void addHoverEffect() {
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                hover = true;
                repaint();
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                hover = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(hover ? HOVER_COLOR : BUTTON_COLOR); // Button color
        // Draw the rounded rectangle background
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
        super.paintComponent(g2);
        g2.dispose();
    }
}

