package com.haber.utils;

import com.lowagie.text.Image;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.*;
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

//    public static LoadingFrame getLoadingFrame(){
//        return new LoadingFrame();
//    }


    public static class LoadingPanel extends JComponent implements MouseListener {
        /**
         *
         */
        private static final long serialVersionUID = 1L;
        protected Area[] ticker = null;
        protected Thread animation = null;
        protected boolean started = false;
        protected int alphaLevel = 0;
        protected int rampDelay = 300;
        protected float shield = 0.70f;
        protected String text = "";
        protected int barsCount = 14;
        protected float fps = 15.0f;

        protected RenderingHints hints = null;

        public LoadingPanel() {
            this("");
        }

        public LoadingPanel(String text) {
            this(text, 14);
        }

        public LoadingPanel(String text, int barsCount) {
            this(text, barsCount, 0.70f);
        }

        public LoadingPanel(String text, int barsCount, float shield) {
            this(text, barsCount, shield, 15.0f);
        }

        public LoadingPanel(String text, int barsCount, float shield, float fps) {
            this(text, barsCount, shield, fps, 300);
        }

        public LoadingPanel(String text, int barsCount, float shield, float fps, int rampDelay) {
            this.text = text;
            this.rampDelay = rampDelay >= 0 ? rampDelay : 0;
            this.shield = shield >= 0.0f ? shield : 0.0f;
            this.fps = fps > 0.0f ? fps : 15.0f;
            this.barsCount = barsCount > 0 ? barsCount : 14;

            this.hints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            this.hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            this.hints.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        }

        public void setText(String text) {
            repaint();
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void start() {
            addMouseListener(this);
            setVisible(true);
            ticker = buildTicker();
            animation = new Thread(new Animator(true));
            animation.start();
        }

        public void stop() {
            if (animation != null) {
                animation.interrupt();
                animation = null;
                animation = new Thread(new Animator(false));
                animation.start();
            }
        }

        public void interrupt() {
            if (animation != null) {
                animation.interrupt();
                animation = null;

                removeMouseListener(this);
                setVisible(false);
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            if (started) {
                int width = getWidth();
                int height = getHeight();

                double maxY = 0.0;

                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHints(hints);

                g2.setColor(new Color(255, 255, 255, (int) (alphaLevel * shield)));
                g2.fillRect(0, 0, width, height);

                for (int i = 0; i < ticker.length; i++) {
                    int channel = 224 - 128 / (i + 1);
                    g2.setColor(new Color(channel, channel, channel, alphaLevel));
                    g2.fill(ticker[i]);

                    Rectangle2D bounds = ticker[i].getBounds2D();
                    if (bounds.getMaxY() > maxY) {
                        maxY = bounds.getMaxY();
                    }
                }

                if (text != null && text.length() > 0) {
                    FontRenderContext context = g2.getFontRenderContext();
                    TextLayout layout = new TextLayout(text, getFont(), context);
                    Rectangle2D bounds = layout.getBounds();
                    g2.setColor(getForeground());
                    layout.draw(g2, (float) (width - bounds.getWidth()) / 2,
                            (float) (maxY + layout.getLeading() + 2 * layout.getAscent()));
                }
            }
        }

        private Area[] buildTicker() {
            Area[] ticker = new Area[barsCount];
            Point2D.Double center = new Point2D.Double((double) getWidth() / 2, (double) getHeight() / 2);
            double fixedAngle = 2.0 * Math.PI / (barsCount);

            for (double i = 0.0; i < barsCount; i++) {
                Area primitive = buildPrimitive();

                AffineTransform toCenter = AffineTransform.getTranslateInstance(center.getX(), center.getY());
                AffineTransform toBorder = AffineTransform.getTranslateInstance(45.0, -6.0);
                AffineTransform toCircle = AffineTransform.getRotateInstance(-i * fixedAngle, center.getX(), center.getY());

                AffineTransform toWheel = new AffineTransform();
                toWheel.concatenate(toCenter);
                toWheel.concatenate(toBorder);

                primitive.transform(toWheel);
                primitive.transform(toCircle);

                ticker[(int) i] = primitive;
            }

            return ticker;
        }

        private Area buildPrimitive() {
            Rectangle2D.Double body = new Rectangle2D.Double(6, 0, 30, 12);
            Ellipse2D.Double head = new Ellipse2D.Double(0, 0, 12, 12);
            Ellipse2D.Double tail = new Ellipse2D.Double(30, 0, 12, 12);

            Area tick = new Area(body);
            tick.add(new Area(head));
            tick.add(new Area(tail));

            return tick;
        }

        protected class Animator implements Runnable {
            private boolean rampUp = true;

            protected Animator(boolean rampUp) {
                this.rampUp = rampUp;
            }

            @Override
            public void run() {
                Point2D.Double center = new Point2D.Double((double) getWidth() / 2, (double) getHeight() / 2);
                double fixedIncrement = 2.0 * Math.PI / (barsCount);
                AffineTransform toCircle = AffineTransform.getRotateInstance(fixedIncrement, center.getX(), center.getY());

                long start = System.currentTimeMillis();
                if (rampDelay == 0) {
                    alphaLevel = rampUp ? 255 : 0;
                }

                started = true;
                boolean inRamp = rampUp;

                while (!Thread.interrupted()) {
                    if (!inRamp) {
                        for (int i = 0; i < ticker.length; i++) {
                            ticker[i].transform(toCircle);
                        }
                    }

                    repaint();

                    if (rampUp) {
                        if (alphaLevel < 255) {
                            alphaLevel = (int) (255 * (System.currentTimeMillis() - start) / rampDelay);
                            if (alphaLevel >= 255) {
                                alphaLevel = 255;
                                inRamp = false;
                            }
                        }
                    } else if (alphaLevel > 0) {
                        alphaLevel = (int) (255 - (255 * (System.currentTimeMillis() - start) / rampDelay));
                        if (alphaLevel <= 0) {
                            alphaLevel = 0;
                            break;
                        }
                    }

                    try {
                        Thread.sleep(inRamp ? 10 : (int) (1000 / fps));
                    } catch (InterruptedException ie) {
                        break;
                    }
                    Thread.yield();
                }

                if (!rampUp) {
                    started = false;
                    repaint();

                    setVisible(false);
                    removeMouseListener(LoadingPanel.this);
                }
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

    }

    public static JFrame getLoadingFrame(){
        JFrame frame = new JFrame();
        frame.setTitle("正在转换");
        frame.setSize(350,350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);//窗口置于屏幕中央
        frame.setResizable(false);//设置窗体是否可以调整大小
        // ...
        LoadingPanel glasspane = new LoadingPanel();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        glasspane.setBounds(50, 100, (dimension.width) / 6, (dimension.height) / 6);
        frame.setGlassPane(glasspane);
        glasspane.setText("转换中请稍后");
        glasspane.start();//开始动画加载效果
        frame.setVisible(true);
        return frame;
    }

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JFrame loadingFrame = JFrameUtils.getLoadingFrame();
            }
        }).start();
    }


}
