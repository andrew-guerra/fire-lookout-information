package libs.crawler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LookoutCrawler {
    public static void extractStateLookoutData(String url) {
        String[] links = extractLookoutPageLinks(url);
        String[] stateNames = extractLookoutStateNames(url);


        for(int i = 0; i < Math.min(links.length, stateNames.length); i++) {
            String filename = stateNames[i].toLowerCase().replace(" ", "-") + ".csv";
            File file = new File("lookout-data\\" + filename);
            
            try {
                PrintWriter writer = new PrintWriter(file);

                for(String row : extractLookoutData(links[i])) {
                    writer.println(row);
                }

                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static String[] extractLookoutData(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements tableRows = doc.getElementsByTag("tr");
            String[] rows = new String[tableRows.size()];

            for(int i = 0; i < tableRows.size(); i++) {
                Elements rowEntries = tableRows.get(i).getElementsByTag("td");
                StringBuilder rowStringBuilder = new StringBuilder();

                for(Element rowEntry : rowEntries) {
                    rowStringBuilder.append(rowEntry.text());
                    rowStringBuilder.append(", ");
                }

                rows[i] = rowStringBuilder.toString();
            }

            return rows;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String[0];
    }

    public static void extractBaseLookoutData(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements tableRows = doc.getElementsByTag("table").get(3).getElementsByTag("tr");

            for(Element tableRow : tableRows) {
                Elements rowEntries = tableRow.getElementsByTag("td");
                StringBuilder rowStringBuilder = new StringBuilder();

                for(Element rowEntry : rowEntries) {
                    rowStringBuilder.append(rowEntry.text());
                    rowStringBuilder.append(", ");
                }

                System.out.println(rowStringBuilder.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String[] extractLookoutPageLinks(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements tableRows = doc.getElementsByTag("table").get(3).getElementsByTag("tr");
            List<Element> validTableRows = tableRows.subList(1, tableRows.size());

            String[] links = new String[validTableRows.size()];

            for(int i = 0; i < validTableRows.size(); i++) {
                links[i] = validTableRows.get(i).getElementsByTag("a").first().absUrl("href");
            }

            return links;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String[0];
    }

    public static String[] extractLookoutStateNames(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements tableRows = doc.getElementsByTag("table").get(3).getElementsByTag("tr");
            List<Element> validTableRows = tableRows.subList(1, tableRows.size());

            String[] links = new String[validTableRows.size()];

            for(int i = 0; i < validTableRows.size(); i++) {
                links[i] = validTableRows.get(i).getElementsByTag("a").first().text();
            }

            return links;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String[0];
    }

    public static void main(String[] args) {
        extractStateLookoutData("https://www.firelookout.org/worldwide-lookout-library.html");
    }
}