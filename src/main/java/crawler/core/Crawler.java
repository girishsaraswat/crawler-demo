package crawler.core;

import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Girish Saraswat
 * This is used to start crawling.
 */
public class Crawler {
    private static final Logger LOGGER = Logger.getLogger ( Crawler.class );

    /* Fields */
    private static final int MAX_PAGES_TO_SEARCH = 10;
    private Set<String> pagesVisited;
    private List<String> pagesToVisit;

    /**
     * Default Constructor
     */
    public Crawler() {
        pagesVisited = new HashSet<String> ();
        pagesToVisit = new LinkedList<String> ();
    }

    /**
     * Used to get new url from pages to visit.
     *
     * @return This will return a url which is found in web page.
     */
    private String nextUrl() {
        String nextUrl;
        do {
            nextUrl = this.pagesToVisit.remove ( 0 );
        } while (this.pagesVisited.contains ( nextUrl ));
        this.pagesVisited.add ( nextUrl );
        return nextUrl;
    }

    /**
     * Used to search a word from visited page url
     *
     * @param url        : This is coming from web page to visit
     * @param searchWord : This is coming from user.
     */
    public void search(String url, String searchWord) {
        while (this.pagesVisited.size () < MAX_PAGES_TO_SEARCH) {
            String currentUrl;
            CrawlerDependent leg = new CrawlerDependent ();
            if (this.pagesToVisit.isEmpty ()) {
                currentUrl = url;
                this.pagesVisited.add ( url );
            } else {
                currentUrl = this.nextUrl ();
            }
            /*Lots of stuff happening here. Look at the crawl method in*/
            leg.crawl ( currentUrl );
            /*CrawlerDependent*/
            boolean success = leg.searchForWord ( searchWord );
            if (success) {
                LOGGER.info ( String.format ( "**Success** Word %s found at %s", searchWord, currentUrl ) );
                break;
            }
            this.pagesToVisit.addAll ( leg.getLinks () );
        }
        LOGGER.info ( "\n**Done** Visited " + this.pagesVisited.size () + " web page(s)" );
    }

}
