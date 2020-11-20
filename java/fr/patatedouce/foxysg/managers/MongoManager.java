package fr.patatedouce.foxysg.managers;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import fr.patatedouce.foxysg.FoxySG;
import fr.patatedouce.foxysg.utils.chat.Color;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.Collections;

@Getter
public class MongoManager {
    @Getter
    public static MongoManager instance;
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> statsCollection;

    public MongoManager() {
        instance = this;
        try {
            if (FoxySG.getInstance().getConfiguration("config").getBoolean("MONGODB.AUTHENTICATION.ENABLED")) {
                final MongoCredential credential = MongoCredential.createCredential(
                        FoxySG.getInstance().getConfiguration("config").getString("MONGODB.AUTHENTICATION.USERNAME"),
                        FoxySG.getInstance().getConfiguration("config").getString("MONGODB.AUTHENTICATION.DATABASE"),
                        FoxySG.getInstance().getConfiguration("config").getString("MONGODB.AUTHENTICATION.PASSWORD").toCharArray()
                );
                mongoClient = new MongoClient(new ServerAddress(FoxySG.getInstance().getConfiguration("config").getString("MONGODB.ADDRESS"), FoxySG.getInstance().getConfiguration("config").getInt("MONGODB.PORT")), Collections.singletonList(credential));
            } else {
                mongoClient = new MongoClient(FoxySG.getInstance().getConfiguration("config").getString("MONGODB.ADDRESS"), FoxySG.getInstance().getConfiguration("config").getInt("MONGODB.PORT"));
            }
            mongoDatabase = mongoClient.getDatabase(FoxySG.getInstance().getConfiguration("config").getString("MONGODB.DATABASE"));
            statsCollection = mongoDatabase.getCollection("Statistics");
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(Color.translate("&cconnexion failed avec MongoDB"));
            Bukkit.getServer().getPluginManager().disablePlugin(FoxySG.getInstance());
        }
    }
}
