package com.sudoko.uicustom.panel;

import java.awt.Dimension;
import javax.swing.JPanel;

public class MainPanel extends JPanel{
    
    public MainPanel(final Dimension dimension){
        this.setSize(dimension);
        this.setPreferredSize(dimension);
    }
}