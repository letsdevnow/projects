package server;

import com.google.gson.Gson;
import common.ServerRequest;
import common.ServerResponse;
import lombok.AllArgsConstructor;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@AllArgsConstructor
public class ServerSession implements Runnable {
    private final Socket socket;
    private final Map<String, String> db;
    private final ServerFunc serverFunc;

    //locks for handler method
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    //implements method which response on new incoming connections in separate thread
    @Override
    public void run() {
        System.out.println("Server task started, thread: " + Thread.currentThread().getName());

        try (
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            while (!socket.isClosed()) {
                String inMsg = input.readUTF(); // reading a message
                System.out.println("Received: " + inMsg);

                ServerRequest serverRequest = new Gson().fromJson(inMsg, ServerRequest.class);

                //stop the server
                if(serverRequest.getType().equals("STOP")) {
                    output.writeUTF(new Gson().toJson(new ServerResponse("OK")));
                    socket.shutdownInput();
                    socket.close();
                    serverFunc.stop();
                }
                //disconnect with the client
                else if(serverRequest.getType().equals("DISCONNECT")) {
                    output.writeUTF(new Gson().toJson(new ServerResponse("OK")));
                    socket.shutdownInput();
                    socket.close();
                }
                //execute other command
                else {
                    output.writeUTF(commandHandler(serverRequest));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //handle requests from client
    private String commandHandler(ServerRequest serverRequest) throws IOException {
        String returnStr;
        ServerResponse dbResponse;

        switch (serverRequest.getType()) {
            case "SET" -> {
                synchronized (writeLock) {
                    db.put(serverRequest.getKey(), serverRequest.getValue());
                }
                dbResponse = new ServerResponse("OK");
            } case "GET" -> {
                synchronized (readLock) {
                    returnStr = db.get(serverRequest.getKey());
                }
                if (returnStr == null) {
                    dbResponse = new ServerResponse("ERROR", null, "No such key");
                } else {
                    dbResponse = new ServerResponse("OK", returnStr);
                }
            }
            case "DELETE" -> {
                synchronized (writeLock) {
                    if (db.remove(serverRequest.getKey()) != null) {
                        dbResponse = new ServerResponse("OK");
                    } else {
                        dbResponse = new ServerResponse("ERROR", null, "No such key");
                    }
                }

            }
            default -> dbResponse = new ServerResponse("ERROR", null, "Incorrect command");
        }

        //serverFunc.saveDB();
        //TimeUnit.SECONDS.sleep(10);
        return new Gson().toJson(dbResponse);
    }
}