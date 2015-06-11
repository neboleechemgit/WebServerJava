import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.io.IOException;
import java.io.File;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.*;

public class WebServer {

    public static final String WEB_ROOT =
            System.getProperty("user.dir") + File.separator  + "webroot";

    public static final Calendar STARTINGTIME = Calendar.getInstance();

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {

        WebServer server = new WebServer();
        server.await();
    }

    public void await() throws IOException, InterruptedException, ExecutionException {

        Map<String, String> configMap = null;

        try {
            configMap = WebConfigXML.ReadXMLFile();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        ServerSocket serverSocket = null;

        int port = 8080;

        try {
            port = Integer.parseInt(configMap.get("port"));
            serverSocket =  new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        Thread loggingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Logging.writeLog();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        loggingThread.start();

        ExecutorService executor = Executors.newCachedThreadPool();

        Logging.putLoggingQueue("START","on " + port + " port");

        // Loop waiting for a request
        while (true) {
            executor.submit(new ClientSocketThread(serverSocket.accept()));
        }
    }
}
