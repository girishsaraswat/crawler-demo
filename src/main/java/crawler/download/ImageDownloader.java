package crawler.download;

import org.apache.log4j.Logger;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * @author Girish Saraswat
 * This is used to download images
 */
public class ImageDownloader {

    private static final Logger LOGGER = Logger.getLogger ( ImageDownloader.class );
    public static String folderPath = "/usr/local/etc/crawl_download";

    public void imageDownloadFromUrl(String url) throws Exception {
        /*Extract the name of the image from the src attribute*/
        InputStream in = null;
        OutputStream out = null;

        int indexName = url.lastIndexOf ( '/' );
        if (indexName == url.length ()) {

            url = url.substring ( 1, indexName );

        }

        indexName = url.lastIndexOf ( '/' );
        String name = url.substring ( indexName, url.length () );
        LOGGER.info ( "Image name: " + name );
        try {
        /*Open a URL Stream*/
            URL newUrl = new URL ( url );
            in = newUrl.openStream ();
            out = new BufferedOutputStream ( new FileOutputStream ( folderPath + name ) );
            for (int b; (b = in.read ()) != -1; ) {

                out.write ( b );

            }
        } catch (Exception e) {
            LOGGER.error ( e.getMessage () );
        } finally {

            if (out != null)
                out.close ();

            if (in != null)
                in.close ();
        }


    }

}
