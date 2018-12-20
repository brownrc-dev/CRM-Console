package net.nsreverse.crm.java.utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class DatabaseUtils {
    private static MongoClient client;
    private static MongoDatabase database;
    private static MongoCollection agentCollection;
    private static MongoCollection customerCollection;
    private static MongoCollection releaseNotesCollection;
    private static MongoCollection supportTicketsCollection;

    public static MongoClient getClient() {
        if (client == null) {
            client = MongoClients.create("mongodb://nsreverse:Ro600620@ds125912.mlab.com:25912/heroku_fgdqql2m");
        }

        return client;
    }

    private static MongoDatabase getDatabase() {
        if (database == null) {
             database = getClient().getDatabase("heroku_fgdqql2m");
        }

        return database;
    }

    public static MongoCollection<Document> getAgentCollection() {
        if (agentCollection == null) {
            agentCollection = getDatabase().getCollection("agents");
        }

        return agentCollection;
    }

    public static MongoCollection<Document> getCustomerCollection() {
        if (customerCollection == null) {
            customerCollection = getDatabase().getCollection("customers");
        }

        return customerCollection;
    }

    public static MongoCollection<Document> getReleaseNotesCollection() {
        if (releaseNotesCollection == null) {
            releaseNotesCollection = getDatabase().getCollection("releaseNotes");
        }

        return releaseNotesCollection;
    }

    public static MongoCollection<Document> getSupportTicketsCollection() {
        if (supportTicketsCollection == null) {
            supportTicketsCollection = getDatabase().getCollection("supportTickets");
        }

        return supportTicketsCollection;
    }
}
