package crawler;

import crawler.core.Crawler;
import org.apache.log4j.Logger;

import java.util.Scanner;

/**
 * Application Entry Class.
 *
 * @author Girish Saraswat
 */
public class CrawlerDemoTest {
    private static final Logger LOGGER = Logger.getLogger ( CrawlerDemoTest.class );

    public static void main(String[] args) {
        /*https://en.wikipedia.org/wiki/YouTube*/
        Scanner scanner = new Scanner ( System.in );
        LOGGER.info ( "Enter website url : " );
        String url = scanner.next ();

        Crawler crawlerDemo = new Crawler ();
        crawlerDemo.search ( url, "youtube" );
    }
}
