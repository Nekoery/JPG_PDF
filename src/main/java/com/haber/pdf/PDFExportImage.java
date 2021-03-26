package com.haber.pdf;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class PDFExportImage {


    private static final List excludePage = Arrays.asList(0);
    private static final boolean isCut = true;

    public static void setup(String filePath, String outDirPath) throws IOException {
        File file = new File(filePath);
        PDDocument document = PDDocument.load(file);
        PDFRenderer renderer = new PDFRenderer(document);
        int pageTotal = document.getNumberOfPages();
        System.out.println("页数：" + pageTotal);
        File outDir = new File(outDirPath);
        if (!outDir.exists()) {
            outDir.mkdirs();
        }
        if (!outDir.isDirectory()) {
            System.err.println("请填写正确的输出路径");
            System.exit(0);
        }
        int pageName = 0;
        for (int pageIndex = 0; pageIndex < pageTotal; pageIndex++) {
            System.out.println("正在转换第 " + pageIndex + " 页");
            BufferedImage image = renderer.renderImageWithDPI(pageIndex, 700, ImageType.RGB);
            if (!isCut && pageIndex != (pageTotal - 1) && !excludePage.contains(pageIndex)) {
                String fileName1 = outDir + "/" + file.getName() + "-" + (pageName++) + ".jpg";
                cut(image, fileName1, 0, 0, image.getWidth() / 2, image.getHeight());
                String fileName2 = outDir + "/" + file.getName() + "-" + (pageName++) + ".jpg";
                cut(image, fileName2, image.getWidth() / 2, 0, image.getWidth() / 2, image.getHeight());
            } else {
                String fileName = outDir + "/" + file.getName() + "-" + (pageName++) + ".jpg";
                ImageIO.write(image, "jpg", new File(fileName));
            }
        }
        document.close();
    }

    public final static void cut(BufferedImage bufferedImage, String result,
                                 int x, int y, int width, int height) {
        try {
            // 读取源图像
            int srcWidth = bufferedImage.getWidth(); // 源图宽度
            int srcHeight = bufferedImage.getHeight(); // 源图高度
            if (srcWidth > 0 && srcHeight > 0) {
                Image image = bufferedImage.getScaledInstance(srcWidth, srcHeight,
                        Image.SCALE_DEFAULT);
                // 四个参数分别为图像起点坐标和宽高
                // 即: CropImageFilter(int x,int y,int width,int height)
                ImageFilter cropFilter = new CropImageFilter(x, y, width, height);
                Image img = Toolkit.getDefaultToolkit().createImage(
                        new FilteredImageSource(image.getSource(),
                                cropFilter));
                BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                g.drawImage(img, 0, 0, width, height, null); // 绘制切割后的图
                g.dispose();
                // 输出为文件
                ImageIO.write(tag, "jpg", new File(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public static String getPath(String path,String format){
        String temp = new String();
        if(path == null)
            return "";
        File file = new File(path);
        if(file.isDirectory()){
            File [] files = file.listFiles();
            for (File eachFile:
                    files) {
                temp += getPath(eachFile.getPath(),format);
            }
        }else if(file.isFile()&&file.getName().endsWith(format)){
            temp = file.getPath()+";";
        }
        return temp;
    }
    public static void main(String[] args) throws IOException {
        MyFrame myFrame = null;
        String choosePath =  getFilePath(); //获取文件所在位置
        String allPath ="全部;";
        allPath += getPath(choosePath,".pdf"); //获取文件或文件夹下所有的pdf文件
        if(allPath.equals("全部;")){
            JOptionPane.showMessageDialog(null,
                    "所选文件格式错误或文件夹内无pdf文件", "错误", JOptionPane.PLAIN_MESSAGE);
            System.exit(0);
        } else {
            String [] paths = allPath.split(";");   //将文件进行分批处理
            String path = null;
            try {
                path = (String) JOptionPane.showInputDialog(null,"请选择转换文件","选择文件",
                        JOptionPane.QUESTION_MESSAGE,null,paths,paths[0]);
            }catch (Exception e){
                JOptionPane.showMessageDialog(null,
                        "取消了转换", "提示", JOptionPane.PLAIN_MESSAGE);
                System.exit(0);
            }
            if(path.equals("全部")){
                myFrame = new MyFrame();
                for (String eachPath:
                        paths) {
                    if(eachPath.equals("全部")){
                        continue;
                    }
                    myFrame.jLabel.setText("正在转换:");
                    myFrame.jLabel2.setText(eachPath);
                    File file = new File(eachPath);
                    setup(file.getPath(),file.getParent());
                }
                myFrame.dispose();
            }else{
                myFrame = new MyFrame();
                myFrame.jLabel.setText("正在转换:");
                myFrame.jLabel2.setText(path);
                File file = new File(path);
                setup(file.getPath(),file.getParent());
                myFrame.dispose();
            }
        }
        JOptionPane.showMessageDialog(null,
                "转换完成，jpg文件与pdf在相同文件夹", "转换成功", JOptionPane.PLAIN_MESSAGE);
        System.exit(0);
    }




}
