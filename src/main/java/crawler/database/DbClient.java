package crawler.database;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import crawler.model.DocumentInfo;
import org.bson.Document;

import java.util.List;

/**
 * @author Girish Saraswat
 * Used to perform DB operations
 */
public class DbClient {
    private static final String MONGO_URL = "mongodb://localhost:27017";
    public static final MongoClient MONGO_CLIENT = new MongoClient ( new MongoClientURI ( MONGO_URL ) );
    public static final MongoDatabase MONGO_DATABASE = MONGO_CLIENT.getDatabase ( Collection.CRAWLER );

    /**
     * Persist data into DB
     *
     * @param object : document info
     */
    public static void save(DocumentInfo object) {
        Gson gson = new Gson ();
        MongoCollection<Document> mongoCollection = DbClient.MONGO_DATABASE.getCollection ( "document_info" );
        Document document = Document.parse ( gson.toJson ( object ) );
        mongoCollection.insertOne ( document );
    }

    /**
     * Get all information of document
     *
     * @return This will return a list of document info
     */
    public static List<Document> getInfos() {
        MongoCollection<Document> mongoCollection = DbClient.MONGO_DATABASE.getCollection ( "document_info" );
        Iterable<Document> documentInfos = mongoCollection.find ();
        return Lists.newArrayList ( documentInfos );
    }

    private interface Collection {
        String CRAWLER = "crawler";
    }

}
