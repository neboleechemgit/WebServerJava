import java.io.InputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class Request {

    private InputStream input;
    private String uri;

    public Request(InputStream input) {
        this.input = input;
    }

    public int parse() throws IOException, InterruptedException {
        // Read a set of characters from the socket
//        StringBuffer request = new StringBuffer(2048);


        int i;
        byte[] buffer = new byte[2048];
        try {
            i = input.read(buffer);
        }
        catch (IOException e) {
            e.printStackTrace();
            i = -1;
        }


        if (i == -1) {
            Logging.putLoggingQueue("NULL_INPUTSTREAM", "");
            return -1;
        }


        String request = new String(buffer, "UTF-8");

        request = request.substring(0,i);

        System.out.print(URLDecoder.decode(request, "UTF-8"));

        uri = parseUri(request.toString());

        return 0;
    }

    private String parseUri(String requestString) throws UnsupportedEncodingException, InterruptedException {
        int index1, index2;
        index1 = requestString.indexOf(' ');
        if (index1 != -1) {
            index2 = requestString.indexOf(' ', index1 + 1);
            if (index2 > index1) {
                String url = URLDecoder.decode(requestString.substring(index1 + 1, index2),"UTF-8");
                Logging.putLoggingQueue("REQUEST", url);
                return url;
            }
        }
        return null;
    }

    public String getUri() {
        return uri;
    }

}