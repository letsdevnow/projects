package server;

public class Main {
    private final static String DB_FILE = "src\\main\\resources\\data\\db.json";
    private final static String ADDRESS = "127.0.0.1";
    private final static int PORT = 23456;

    public static void main(String[] args) throws InterruptedException {
        ServerFunc serverFunc = new ServerFunc(DB_FILE, ADDRESS, PORT);
        serverFunc.start();
        //TimeUnit.SECONDS.sleep(100);
    }
}
