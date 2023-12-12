package com.client.components;

import org.apache.commons.lang3.StringUtils;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class PlaceholderTextAreaPane extends JEditorPane {

    private String placeholderText;
    private boolean isPlaceholderVisible;

    public PlaceholderTextAreaPane(String placeholderHtml) {
        super();
        this.setContentType("text/html");
        this.placeholderText = placeholderHtml;
        this.isPlaceholderVisible = true;
        this.setEditable(false);
        this.setText(placeholderHtml);
        this.setForeground(Color.GRAY);
        this.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (isPlaceholderVisible && !StringUtils.isNotEmpty(getText())) {
                    setText("");
                    setForeground(Color.BLACK);
                    isPlaceholderVisible = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText(placeholderText);
                    setForeground(Color.GRAY);
                    isPlaceholderVisible = true;
                }
            }
        });
    }

    public void setMarkdownText(String markdownText) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdownText);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        this.setText(renderer.render(document));
    }
}
