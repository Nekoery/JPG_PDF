package com.haber.pdf;

import javax.swing.*;
import java.awt.*;

class MyFrame extends JFrame {
    public JLabel jLabel= new JLabel();
    public JLabel jLabel2 = new JLabel();
    public JLabel jLabel3 = new JLabel();
    MyFrame(){
        setSize(500,100);
        setLocationRelativeTo(null);
        setTitle("正在转换");
        setLayout(new FlowLayout());
        add(jLabel);
        add(jLabel2);
        setVisible(true);
    }
}
