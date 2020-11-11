import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.MongoCredential;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ConnectMongo {
  public static void main(String[] args) {

//  String userName = "hendy-1";
  String userName = "dpayment";
  String database= "dpayment";
  String host = "mongodb-01.dev1-wallet.lokal";
//  String password = "hendy";
  String password = "dpayment";
  try {
    MongoClientOptions options = MongoClientOptions.builder()
        .readPreference(ReadPreference.nearest())
        .maxConnectionIdleTime(60000)
        .connectionsPerHost(50)
        .connectTimeout(10000)
        .socketTimeout(10000)
        .build();

    List<MongoCredential> credentials = new ArrayList<MongoCredential>();
    credentials.add(MongoCredential.createScramSha256Credential(userName,
        database,
        password.toCharArray()));

    String array[] = host.split(",");
    List<ServerAddress> serverAddresses = new ArrayList<ServerAddress>();
    for (String str : array) {
      serverAddresses.add(new ServerAddress(str, 27017));
    }
    MongoClient mongoClient = new MongoClient(serverAddresses, credentials, options);
    MongoDatabase mongoDatabase = mongoClient.getDatabase(database);
    System.out.println("jumlah document");

    for (String coll: mongoDatabase.listCollectionNames()) {
      System.out.println(coll);
    }

    MongoCollection<Document> collection = mongoDatabase.getCollection("test");

    System.out.println("total data : " + collection.countDocuments());

    MongoCursor<Document> cursor = collection.find().iterator();
    try {
      while (cursor.hasNext()) {
        System.out.println(cursor.next().toJson());
      }
    } finally {
      cursor.close();
    }

    mongoClient.close();
  }catch (Exception e){
    e.printStackTrace();
  }

}

}
