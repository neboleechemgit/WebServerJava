import java.io.*;

public class Response {

//    private static final int BUFFER_SIZE = 1024;
    Request request;
    OutputStream output;

    public Response(OutputStream output) {
        this.output = output;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    private void prepareString(String response, String codeMessage, int length) {

        String headers = "HTTP/1.1 " + codeMessage + "\r\n" +
                "Server: Andrei/2015-6-7\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: " + length + "\r\n" +
                "Connection: close\r\n\r\n";
        String result = headers + response;

        try {
            output.write(result.getBytes());
            output.flush();
            output.close();

            Logging.putLoggingQueue("RESPONSE", "Response has been sent");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void sendStaticResource() throws IOException {

        FileInputStream fis = null;
        System.out.println(WebServer.WEB_ROOT);

        try {

            File file = new File(WebServer.WEB_ROOT, request.getUri());

            String ifAPI = new API(request.getUri()).parse();

            if ( !ifAPI.equals("-1") ) {

                prepareString(ifAPI, "200 OK", ifAPI.length());
            }
            else if (file.exists()) {

                int s = (int) file.length();
                char[] buf = new char[s];
                FileReader reader = new FileReader(file);
                reader.read(buf);
                reader.close();

                prepareString(new String(buf), "200 OK", s);
            }
            else {
                prepareString("<h1>File Not Found</h1>", "404 File Not Found", 23);
            }

            output.flush();
            output.close();

        }
        catch (Exception e) {
            // thrown if cannot instantiate a File object
            System.out.println(e.toString() );
        }
        finally {
            if (fis!=null)
                fis.close();
        }
    }
}