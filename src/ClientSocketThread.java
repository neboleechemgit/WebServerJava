import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Andrew on 6/5/2015.
 */

public class ClientSocketThread extends Thread {

    public static Long shortestExecution = null;
    public static Long longestExecution = null;

    private Socket client = null;

    public ClientSocketThread(Socket client) {
        this.client = client;
    }

    public void run()
    {

        try {

            Logging.putLoggingQueue("OPEN",client.getRemoteSocketAddress().toString());

            long start = System.currentTimeMillis();

            System.out.println(Thread.currentThread().getName() + " has started");

            InputStream input = client.getInputStream();
            OutputStream output = client.getOutputStream();

            Request request = new Request(input);

            if (request.parse() == -1) {
                long end = System.currentTimeMillis();
                System.out.println(Thread.currentThread().getName() + " execution: " + (end - start) + "ms");

                System.out.println("Input stream is null, stop using google chrome");
            }

            // create Response object
            Response response = new Response(output);
            response.setRequest(request);
            response.sendStaticResource();

            // Close the socket
            client.close();

            long end = System.currentTimeMillis();
            Logging.putLoggingQueue("FINISH", end-start + " ms");

            System.out.println(Thread.currentThread().getName() + " execution: " + (end - start) + "ms");

            if (longestExecution == null || shortestExecution == null) {
                longestExecution = end - start;
                shortestExecution = end - start;
            }
            else if ((end - start) > longestExecution)
                longestExecution = end - start;
            else if ((end - start) < shortestExecution)
                shortestExecution = end -start;




        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}



