package com.haber.utils;

import com.lowagie.text.Image;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class JFrameUtils {

    public static JFrame getChoiceJFrame(ActionListener jpg2pdfAction,ActionListener pdf2jpgAction){
        JFrame f =new JFrame("请选择程序功能");//定义一个窗体对象f
        f.setSize(400,200);//窗体屏幕长和宽
        f.setLocationRelativeTo(null);//窗口置于屏幕中央
        //f.setLocation(300, 300);//窗口位置
        //f.setResizable(false);//设置窗体是否可以调整大小
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//点击窗口的关闭按钮时，程序关闭
        //否则程序仍然在后台运行
        f.setLayout(null);//创建一个新的流布局管理器，
        //具有指定的对齐方式以及指定的水平和垂直间隙
        JButton jpg2pdf = new JButton("图片转PDF");//定义一个按钮对象a
        JButton pdf2jpg =new JButton("PDF转JPG");
        jpg2pdf.setBounds(50,25,100,100); //坐标 + 宽高
        pdf2jpg.setBounds(200,25,100,100); //坐标 + 宽高
        jpg2pdf.addActionListener(jpg2pdfAction);//为按钮添加一个实现ActionListener接口的对象
        pdf2jpg.addActionListener(pdf2jpgAction);
        f.add(jpg2pdf);//将组件a添加至容器
        f.add(pdf2jpg);//将组件b添加至容器
        f.setVisible(true);//设置窗体可见，否则看不见界面
        return f;
    }

    public static String getFilePath(){
        String path = null;
        JFileChooser fileChooser = new JFileChooser();
        FileSystemView fsv = FileSystemView.getFileSystemView();  //获取系统视图
        fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
        fileChooser.setDialogTitle("请选择要转换的文件或者文件夹...");
        fileChooser.setApproveButtonText("确定");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);//FILES_ONLY 只文件,DIRECTORIES_ONLY 只文件夹,
        if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(null)) {
            return fileChooser.getSelectedFile().getPath();
        }
        return null;
    }

    public static class LoadingFrame extends JFrame {
        public JLabel jLabel= new JLabel();
        public JLabel jLabel2 = new JLabel();
        LoadingFrame(){
            setSize(600,200);
            setLocationRelativeTo(null);
            setTitle("正在转换");
            setLayout(new FlowLayout());
            add(jLabel);
            add(jLabel2);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
        }
    }

    public static LoadingFrame getLoadingFrame(){
        return new LoadingFrame();
    }

    public static void main(String[] args) {
        List<String> fileList =  FileUtils.getPathList("E:\\test",FileUtils.getFormatList());
        JFrameUtils.LoadingFrame loadingFrame;
        loadingFrame = JFrameUtils.getLoadingFrame();

        for(String filePath:
                fileList){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            loadingFrame.jLabel.setText("正在转换:");
            loadingFrame.jLabel2.setText(filePath);
        }



//        for (int i = 0 ; i < 10000 ; i++){
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            loadingFrame.jLabel2.setText(String.valueOf(i));
//        }
    }


}
