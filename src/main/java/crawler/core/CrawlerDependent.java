package crawler.core;

import crawler.database.DbClient;
import crawler.download.ImageDownloader;
import crawler.model.DocumentInfo;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Girish Saraswat
 * This is used imlement things those are dependent to Crawler.
 */
public class CrawlerDependent {

    private static final Logger LOGGER = Logger.getLogger ( CrawlerDependent.class );
    /* Mock user agent */
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private static final int SUCCESS = 200;
    private static final String YOU_TUBE_LOGO = "200px-YouTube_Logo_2017.svg.png";
    private final ImageDownloader imageDownloader;
    private List<String> links;
    private List<String> images;
    private Document htmlDocument;


    public CrawlerDependent() {
        imageDownloader = new ImageDownloader ();
        links = new LinkedList<String> ();
        images = new LinkedList<String> ();
    }

    /**
     * To implement crawling
     *
     * @param url : This is a url link of you tube wiki page.
     * @return This will return a true/false literal on basis of things.
     */
    public boolean crawl(String url) {
        try {
            Connection connection = Jsoup.connect ( url ).userAgent ( USER_AGENT );
            htmlDocument = connection.get ();
            /* indicating that everything is great. */
            if (connection.response ().statusCode () == SUCCESS) {
                LOGGER.info ( "\n**Visiting** Received web page at " + url );
            }
            if (!connection.response ().contentType ().contains ( "text/html" )) {
                LOGGER.info ( "**Failure** Retrieved something other than HTML" );
                return false;
            }
            Elements linksOnPage = htmlDocument.select ( "a[href]" );
            Elements imagesOnPage = htmlDocument.select ( "img" );
            String header = htmlDocument.select ( "#firstHeading" ).text ();
            LOGGER.info ( "Found (" + linksOnPage.size () + ") links" );
            for (Element link : linksOnPage) {
                this.links.add ( link.absUrl ( "href" ) );
            }

            String youTubeImagePath = "";
            for (Element image : imagesOnPage) {
                this.images.add ( image.attr ( "src" ) );
                int match = image.attr ( "src" ).indexOf ( YOU_TUBE_LOGO );
                if (match != -1) {
                    youTubeImagePath = "https:" + image.attr ( "src" );
                }
            }
            imageDownloader.imageDownloadFromUrl ( youTubeImagePath );
            renderObject ( youTubeImagePath, header );

            return true;
        } catch (Exception e) {
            /* We were not successful in our HTTP request*/
            LOGGER.error ( String.format ( "Http request is not successful : %s", e.getMessage () ) );

            return false;
        }
    }

    public boolean searchForWord(String searchWord) {
        /* Defensive coding. This method should only be used after a successful crawl.*/
        if (this.htmlDocument == null) {
            LOGGER.info ( "ERROR! Call crawl() before performing analysis on the document" );
            return false;
        }
        LOGGER.info ( "Searching for the word " + searchWord + "..." );
        String bodyText = this.htmlDocument.body ().text ();
        return bodyText.toLowerCase ().contains ( searchWord.toLowerCase () );
    }

    public List<String> getLinks() {
        return this.links;
    }

    /**
     * This method is used to fill information to DocumentInfo and save to db.
     *
     * @param filePath : This is a image path of you tube logo.
     * @param header   :   This is header name of you tube wiki page.
     */
    private void renderObject(String filePath, String header) {
        try {
            /* Create new DocumentInfo Object. */
            DocumentInfo documentInfo = new DocumentInfo ();
            documentInfo.setUploadImagePath ( filePath );
            documentInfo.setHeader ( header );
            documentInfo.setCreated ( DateTime.now ().getMillis () );
            documentInfo.setUpdated ( DateTime.now ().getMillis () );

            /* Save data into DB */
            DbClient.save ( documentInfo );

        } catch (Exception e) {
            LOGGER.error ( String.format ( "Exception occurs in crawling info saving in DB : %s", e.getMessage () ) );
        } finally {
            DbClient.MONGO_CLIENT.close ();
        }

    }

}
