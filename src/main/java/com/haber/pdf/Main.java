package com.haber.pdf;

import com.haber.CallBack;
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
        CallBack callBack = getCallBack();
        choose = JFrameUtils.getChoiceJFrame(getjpg2pdfAction(callBack),getpdf2jpgAction(callBack));
    }

    public CallBack getCallBack(){
        return new CallBack() {
            JFrame loadingFrame;
            @Override
            public void getResult(int result) {

            }

            @Override
            public void display() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                       loadingFrame = JFrameUtils.getLoadingFrame();
                    }
                }).start();
            }

            @Override
            public void hidedis() {
                loadingFrame.dispose();
            }

        };
    }


    public ActionListener getjpg2pdfAction(final CallBack callBack){
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
                String result = JPG2PDF.toPdf(callBack,basePath,basePath+"_完成.pdf");
                JOptionPane.showMessageDialog(null,
                        result, "转换结果", JOptionPane.PLAIN_MESSAGE);
                System.exit(0);
            }
        };
    }

    public ActionListener getpdf2jpgAction(final CallBack callBack){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("选择pdf2jpg");
                ParamBean.softType = ParamBean.SOFTTYPEPDF2JPG;
                choose.dispose();
                String basePath = JFrameUtils.getFilePath();
                if(basePath == null || basePath.length()==0){
                    System.exit(0);
                }
                String result = PDF2JPG.tojpg(callBack,basePath);
                JOptionPane.showMessageDialog(null,
                        result, "转换结果", JOptionPane.PLAIN_MESSAGE);
                System.exit(0);
            }
        };
    }
}
