package com.sudoko.uicustom.button;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;

import com.sudoko.service.Difficulty;

public class DifficultyButton extends JButton implements ActionListener{

    private final Difficulty[] difficulties = Difficulty.values();
    private int currentIndex;
    private ActionListener externalActionListener;
    
    public DifficultyButton(){
        this.currentIndex = 0;
        updateButtonText();
        this.addActionListener(this);
    }

    public void setExternalActionListener(ActionListener listener){
        this.externalActionListener = listener;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        currentIndex = (currentIndex + 1) % difficulties.length;
        updateButtonText();

        ActionEvent newEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, getDifficulty().name());
        if (externalActionListener != null){
            externalActionListener.actionPerformed(newEvent);
        }
    }

    private void updateButtonText(){
        this.setText(getDifficulty().name());
    }

    public Difficulty getDifficulty() {
        return difficulties[currentIndex];
    }
}