package com.sudoko.uicustom.button;

import java.awt.event.ActionListener;
import javax.swing.JButton;

public class ResetButton extends JButton{
    
    public ResetButton(final ActionListener actionListener){
        this.setText("Reiniciar jogo");
        this.addActionListener(actionListener);
    }
}