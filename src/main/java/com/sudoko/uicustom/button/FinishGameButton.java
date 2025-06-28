package com.sudoko.uicustom.button;

import java.awt.event.ActionListener;
import javax.swing.JButton;

public class FinishGameButton extends JButton{
    
    public FinishGameButton(final ActionListener actionListener){
        this.setText("Concluir");
        this.addActionListener(actionListener);
    }
}