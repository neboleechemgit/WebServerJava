import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrew on 6/6/2015.
 */
public class API {

    private String uri = null;

    API (String uri) {
        this.uri = uri;

    }

    public String parse() throws IOException, NoSuchAlgorithmException {

        String isApi = uri.substring(1,6);

        System.out.println(isApi);

        String filePath = getParameter("file");

        switch (isApi) {

            case "api-1":
                return apiOne(filePath);
            case "api-2":
                return apiTwo(filePath);
            case "api-3":
                this.apiThree();
            case "api-4":
                return apiFour();
        }

        return "-1";
    }

    public String getParameter(String parameter)
    {

        int index = uri.indexOf("?");

        if (index != -1) {

            String[] params = uri.substring(index + 1, uri.length()).split("&");
            Map<String, String> map = new HashMap<String, String>();
            for (String param : params) {
                int index2 = param.indexOf("=");
                if (index2 != -1) {
                    String name = param.split("=")[0];
                    String value = param.split("=")[1];
                    map.put(name, value);
                }
            }

            return map.get(parameter);

        }

        return null;

    }

    private String fileParamHandler(String command) {

        if (command.equals(null))
            return -1 + "";

        command = command.replace("/", File.separator);
        command = command.replace("\\", File.separator);

        if (("" + command.charAt(0)).equals(File.separator) &&
                ("" + command.charAt(1)).equals(File.separator)) {
            return "." + command.substring(1, command.length());
        } else {
            return command.substring(0, command.length());
        }

    }

    private String apiFour() {

        return Logging.getServerRuntimeStatistics();

    }

    private void apiThree() {

    }

    private String apiTwo(String command) throws IOException, NoSuchAlgorithmException {

        String path = fileParamHandler(command);

        if(!path.equals("-1")) {

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(Files.readAllBytes(Paths.get(path)));
            byte[] digest = md.digest();

            BigInteger bi = new BigInteger(1, digest);
            return "MD5: " + String.format("%0" + (digest.length << 1) + "X", bi);
        }

        return "-1";

    }

    private String apiOne(String command) {

        String functionResult = fileParamHandler(command);

        long size = -1;

        if(!functionResult.equals("-1")) {

            System.out.println(functionResult);

            File file = new File(functionResult);
            if (file.exists() && !file.isDirectory()) {
                size = file.length();
            }
            else return "-1";

            return "File size: " + size + " bytes.";

        }

        return "-1";

    }

}
