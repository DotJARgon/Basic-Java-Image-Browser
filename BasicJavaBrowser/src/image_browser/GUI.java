package image_browser;

import image_scrapper.BasicScrapper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

public class GUI extends JPanel implements MouseListener, MouseWheelListener {

    private int scrolly, h, w;
    private ArrayList<ImageGrid> request, dispose;

    private ArrayList<String> arr;
    private Iterator<String> iter;

    private ArrayList<Stack<ImageGrid>> grid = new ArrayList<>();
    private int[] stackSize = new int[5];

    private static class ImageGrid {
        private static final Color EMPTY = new Color(10, 10, 10);
        private int x, y, w, h;
        private BufferedImage bf;

        public ImageGrid(int x, int y, BufferedImage bf) {
            this.bf = bf;
            setParameters();
            this.x = x;
            this.y = y;
        }

        public void draw(Graphics2D g, ArrayList<ImageGrid> request, ArrayList<ImageGrid> dispose, int scrolly, int height) {
            if(w == 0 || y == 0) {return;} //just skip
            if(y > scrolly || y+h < scrolly + height) {
                if(bf == null) {
                    request.add(this);
                    g.setColor(EMPTY);
                    g.fillRect(x, y-scrolly, w, h);
                }
                else {
                    g.drawImage(bf, null, x, y-scrolly);
                }
            }
            else {
                if(bf != null) {
                    double min = Math.min(Math.abs(y-scrolly), Math.abs((y+h)-(scrolly+height)));
                    if(min < 300) {
                        dispose.add(this);
                    }
                }
            }
        }

        public void setParameters() {
            if(bf != null) {
                w = bf.getWidth();
                h = bf.getHeight();
            }
        }

        public BufferedImage getImage() {
            return bf;
        }

        public void resizeWidth(int width) {
            if(bf != null) {
                double proportion = ((double) width)/w;
                bf = resizeImage(bf, width, (int) (h*proportion));
                setParameters();
            }
        }

        public void disposeIfNotNull() {
            if(bf != null) {
                bf = null;
            }
        }
        public void setImageIfNull(BufferedImage bf) {
            if(this.bf == null) {
                this.bf = bf;
                setParameters();
            }
        }

        private static BufferedImage resizeImage(BufferedImage img, int newW, int newH) {
            Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
            BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = dimg.createGraphics();
            g2d.drawImage(tmp, 0, 0, null);
            g2d.dispose();

            return dimg;
        }
    }

    public GUI() throws IOException {
        arr = BasicScrapper.scrape("java");
        iter = arr.iterator();
        setBackground(Color.BLACK);
        grid.add(new Stack<>());
        grid.add(new Stack<>());
        grid.add(new Stack<>());
        grid.add(new Stack<>());
        grid.add(new Stack<>());

        this.h = getHeight();
        this.w = (getWidth() - 30)/3;
        this.scrolly = 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        h = getHeight();
        w = getWidth()/6;
        try {
            draw((Graphics2D) g);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void draw(Graphics2D g) throws IOException {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        for(Stack<ImageGrid> column : grid) {
            for(ImageGrid image : column) {
                image.draw(g, request, dispose, scrolly, h);
            }
        }

        boolean load = true;

        while(load) {
            load = false;

            for(int i = 0; i < 5 && iter.hasNext(); i++) {
                if(stackSize[i] < scrolly + h) {
                    ImageGrid ig = new ImageGrid(i*w, stackSize[i], ImageIO.read(new URL(iter.next())));
                    ig.resizeWidth(w);
                    grid.get(i).add(ig);
                    stackSize[i] += ig.getImage().getHeight();
                    load = true;
                }
            }
        }
    }

    public int rand() {
        return (int) (Math.random()*getHeight());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        repaint();
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

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        scrolly += e.getWheelRotation()*20;
        repaint();
    }


}
