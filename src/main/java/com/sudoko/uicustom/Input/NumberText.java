package com.sudoko.uicustom.Input;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.sudoko.model.Space;
import com.sudoko.service.EventEnum;
import com.sudoko.service.EventListener;

import static com.sudoko.service.EventEnum.CLEAR_SPACE;

public class NumberText extends JTextField implements EventListener{
    private final Space space;

    public NumberText(final Space space){
        this.space = space;
        var dimension = new Dimension(50, 50);
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setVisible(true);
        this.setFont(new Font("Arial", Font.PLAIN, 25));
        this.setHorizontalAlignment(CENTER);
        this.setFocusable(false);
        this.setEnabled(true); 

        applyInitialStyling();
        this.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(final DocumentEvent e){ changeSpace(); }
            @Override
            public void removeUpdate(final DocumentEvent e){ changeSpace(); }
            @Override
            public void changedUpdate(final DocumentEvent e){ changeSpace(); }
            private void changeSpace(){
                if (getText().isEmpty()){
                    space.clearSpace();
                } else {
                    try {
                        space.setActual(Integer.parseInt(getText()));
                    } catch (NumberFormatException ex) {
                        System.err.println("Erro de formato de número no campo");
                        space.clearSpace();
                    }
                }
            }
        });
    }

    @Override
    public void update(final EventEnum eventType){
        if (eventType.equals(CLEAR_SPACE)){
            if (!space.isFixed()){
                this.setText("");
                space.clearSpace();
            } else {
                Integer actualValue = space.getActual();
                if (actualValue != null && actualValue != 0){
                    this.setText(actualValue.toString());
                } else {
                    this.setText("");
                } 
            }
        }
    }

    private void applyInitialStyling() {
        if (space.isFixed()) {
            Integer actualValue = space.getActual();
            this.setText((actualValue != null && actualValue != 0) ? actualValue.toString() : "");
            this.setEditable(false);
            this.setForeground(Color.BLACK);
            this.setBackground(new Color(230, 230, 230));
        } else {
            this.setText("");
            this.setForeground(Color.BLUE);
            this.setBackground(Color.WHITE);
            this.setEditable(true);
        }
    }

    public void setGameActive(boolean active) {
        this.setFocusable(active);
    }
}