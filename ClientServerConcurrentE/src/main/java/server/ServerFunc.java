package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.*;
import java.util.concurrent.*;
import static client.Main.println;

public class ServerFunc {
    private final String dbFileName;
    private final String address;
    private final int port;
    private Map<String, String> db = new LinkedTreeMap<>();
    private ExecutorService executor;
    private ServerSocket serverSocket;

    public ServerFunc(String dbFileName, String address, int port) {
        this.dbFileName = dbFileName;
        this.address = address;
        this.port = port;
    }

    //start listening of new incoming connections
    public void start() {
        println("Server started");
        restoreDB();

        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        try {
            serverSocket = new ServerSocket(port, 50, InetAddress.getByName(address));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (!serverSocket.isClosed()) {
            try {
                executor.submit(new ServerSession(serverSocket.accept(), db, this));
            } catch (IOException e) {
                println(e.getMessage());
            }
        }
        stop();
    }

    //stop listening of new incoming connections, stop every thread and exit
    public void stop() {
        saveDB();
        executor.shutdownNow();
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        println("Server stopped");
        System.exit(0);
    }

    //restore DB from the json file
    private void restoreDB() {
        File file = new File(dbFileName);
        if(!file.exists() || file.isDirectory()) {
            println("Database file does not exist");
            return;
        }

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(dbFileName))) {
            Map<String, String> tmpdb = new Gson().fromJson(bufferedReader, Map.class);
            db.clear();
            db = tmpdb;
            println("Database restored");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //save DB to the json file
    public boolean saveDB() {
        File file = new File(dbFileName);
        //System.out.println(file.getAbsolutePath());
        try (FileWriter fw = new FileWriter(file)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            fw.write(gson.toJson(db));
            println("Database saved");
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}


