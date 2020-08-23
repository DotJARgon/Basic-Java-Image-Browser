package image_scrapper;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class BasicScrapper {

    public static ArrayList<String> scrape(String query) throws IOException {
        Document doc = Jsoup.connect("https://www.google.com/search?site=imghp&tbm=isch&source=hp&q=" + query + "&gws_rd=cr").userAgent("Mozilla/5.0").get();
        //above uses JSoup to connect to google and search for the given query
        Elements elements = doc.select(".t0fcAb[src]");
        //we select the class of what is the encrypted url???, I'm not too sure, but I found it is indeed the url for the image
        //and then we select the src argument, which is what points to the url of the image

        ArrayList<String> urls = new ArrayList<>();

        for(Element element : elements) {
            String url = element.toString().split("src=")[1];
            urls.add(url.substring(1, url.length()-2)); //this removes the quotation marks and the last '>'
        }

        return urls;
    }

//    public static void displayImage(String urlPath) throws IOException {
//        URL url = new URL(urlPath);
//        BufferedImage image = ImageIO.read(url);
//        JLabel label = new JLabel(new ImageIcon(image));
//        JFrame f = new JFrame();
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        f.getContentPane().add(label);
//        f.pack();
//        f.setLocation(200, 200);
//        f.setVisible(true);
//    }

}
