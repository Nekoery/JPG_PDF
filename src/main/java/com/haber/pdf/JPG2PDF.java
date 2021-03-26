package com.haber.pdf;


import com.haber.CallBack;
import com.haber.utils.FileUtils;
import com.haber.utils.JFrameUtils;
import com.lowagie.text.*;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JPG2PDF {

    public static String toPdf(CallBack callBack, String imageFolderPath, String pdfPath) {
        try {
            // 输入流
            FileOutputStream fos = new FileOutputStream(pdfPath);
            // 创建文档
            Document doc = new Document(null, 0, 0, 0, 0);
            //doc.open();
            // 写入PDF文档
            PdfWriter.getInstance(doc, fos);
            // 读取图片流
            BufferedImage img = null;
            // 实例化图片
            Image image = null;
            // 获取图片文件夹对象
            List<String> fileList =  FileUtils.getPathList(imageFolderPath,FileUtils.getFormatList());
            if(fileList.size() == 0){
                return "选择文件夹内无jpg或png图片!";
            }
            callBack.display();
            for(String filePath:
            fileList){
                System.out.println("转换JPG路径:" + filePath);
                File file = new File(filePath);
                // 读取图片流
                img = ImageIO.read(file);
                // 根据图片大小设置文档大小
                doc.setPageSize(new Rectangle(img.getWidth(), img.getHeight()));
                // 实例化图片
                image = Image.getInstance(filePath);
                // 添加图片到文档
                doc.open();
                doc.add(image);
            }
            // 关闭文档
            doc.close();
            callBack.hidedis();
            return "创建完成!文件在当前目录";
        } catch (IOException e) {
            e.printStackTrace();
            return "IO转换异常!";
        } catch (DocumentException e) {
            e.printStackTrace();
            return "文件创建异常!";
        }
    }
}
