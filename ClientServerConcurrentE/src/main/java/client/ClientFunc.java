package client;

import client.exception.KeyNotFoundException;
import com.google.gson.Gson;
import common.ServerRequest;
import common.ServerResponse;
import lombok.Getter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Objects;

public class ClientFunc {
    final String ADDRESS = "127.0.0.1";
    final int PORT = 23456;

    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;

    @Getter
    boolean connected = false;

    //connect to server
    public boolean connect() {
        try {
            socket = new Socket(InetAddress.getByName(ADDRESS), PORT);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream  = new DataOutputStream(socket.getOutputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
            connected = false;
        }
        return connected;
    }

    //disconnect from server, optional stop server
    public void disconnect(boolean toStopServer) {
        if (connected) {
            ServerRequest serverRequest = new ServerRequest();
            serverRequest.setType(toStopServer ? "STOP" : "DISCONNECT");
            String strServerRequest = new Gson().toJson(serverRequest);

            try {
                dataOutputStream.writeUTF(strServerRequest.replace("\n", "").replace("\t", ""));
                //System.out.println("Sent: \n" + strServerRequest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connected = false;
    }

    //get value from server by ID
    public String getValueById(String key) throws KeyNotFoundException {
        ServerRequest serverRequest = new ServerRequest("GET", key);
        ServerResponse serverResponse = new ServerResponse();
        try {
            serverResponse = executeCommandOnServer(serverRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (serverResponse.getValue() != null) {
            return serverResponse.getValue();
        } else {
            throw new KeyNotFoundException("Server response - the key has not been found in DB");
        }
    }

    //set value for the key on server
    public boolean setValueById(String key, String value) {
        ServerRequest serverRequest = new ServerRequest("SET", key, value);
        ServerResponse serverResponse;
        try {
            serverResponse = executeCommandOnServer(serverRequest);
            return Objects.equals(serverResponse.getResponse(), "OK");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //delete value from server by ID
    public boolean deleteValueById(String key) {
        ServerRequest serverRequest = new ServerRequest("DELETE", key);
        ServerResponse serverResponse;
        try {
            serverResponse = executeCommandOnServer(serverRequest);
            return Objects.equals(serverResponse.getResponse(), "OK");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //execute command, return response from the server
    private ServerResponse executeCommandOnServer(ServerRequest serverRequest) throws IOException {
        if (!connected) throw new IOException();

        String strServerRequest = new Gson().toJson(serverRequest);
        String strServerResponse = "";
        try {
            dataOutputStream.writeUTF(strServerRequest.replace("\n", "").replace("\t", ""));
            System.out.println("Sent: " + strServerRequest);
            strServerResponse = dataInputStream.readUTF(); // response message
            System.out.println("Received: " + strServerResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Gson().fromJson(strServerResponse, ServerResponse.class);
    }

}
