package com.sudoko.uicustom.button;

import java.awt.event.ActionListener;
import javax.swing.JButton;

public class StartButton extends JButton{
    
    public StartButton(final ActionListener actionListener){
        this.setText("Iniciar jogo");
        this.addActionListener(actionListener);
    }
}