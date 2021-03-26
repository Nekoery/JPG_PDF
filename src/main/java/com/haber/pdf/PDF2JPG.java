package com.haber.pdf;

import com.haber.CallBack;
import com.haber.utils.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class PDF2JPG {


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

    public static String tojpg(CallBack callBack, String choosePath){
        try{
            List<String> formateList = new ArrayList<String>();
            formateList.add(".pdf");
            List<String> filePathList = FileUtils.getPathList(choosePath,formateList);
            if(filePathList.size() == 0){
                return "所选文件格式错误或文件夹内无pdf文件";
            }else {
                String [] paths = filePathList.toArray(new String[filePathList.size()]);
                String path = null;
                try {
                    path = (String) JOptionPane.showInputDialog(null,"请选择转换文件","选择文件",
                            JOptionPane.QUESTION_MESSAGE,null,paths,paths[0]);
                }catch (Exception e){
                    return "取消了转换";
                }
                if(path == null || path.length() == 0){
                    return "取消了转换";
                }
                callBack.display();
                if(path.equals("全部")){
                    for (String eachPath:
                            paths) {
                        if(eachPath.equals("全部")){
                            continue;
                        }
                        File file = new File(eachPath);
                        setup(file.getPath(),file.getParent());
                    }
                }else{
                    File file = new File(path);
                    setup(file.getPath(),file.getParent());
                }
                callBack.hidedis();
                return "转换完成，jpg文件与pdf在相同文件夹";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "Error!未知错误";
        }

    }

}
