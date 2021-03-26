package com.haber.pdf;

import com.haber.utils.JFrameUtils;
import com.haber.bean.ParamBean;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    private JFrame choose;
    public static void main(String[] args) {
        new Main().getChoice();
    }

    public void getChoice(){
        choose = JFrameUtils.getChoiceJFrame(getjpg2pdfAction(),getpdf2jpgAction());
    }


    public ActionListener getjpg2pdfAction(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("选择jpg2pdf");
                ParamBean.softType = ParamBean.SOFTTYPEJPG2PDF;
                choose.dispose();
                String basePath = JFrameUtils.getFilePath();
                if(basePath == null || basePath.length()==0){
                    System.exit(0);
                }
                System.out.println(basePath);
                String result = JPG2PDF.toPdf(basePath,basePath+"_完成.pdf");
                JOptionPane.showMessageDialog(null,
                        result, "转换结果", JOptionPane.PLAIN_MESSAGE);
            }
        };
    }

    public ActionListener getpdf2jpgAction(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("选择pdf2jpg");
                ParamBean.softType = ParamBean.SOFTTYPEPDF2JPG;
                choose.dispose();
                String result = PDF2JPG.tojpg();
                JOptionPane.showMessageDialog(null,
                        result, "转换结果", JOptionPane.PLAIN_MESSAGE);
            }
        };
    }
}
