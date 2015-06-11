import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Logging {

    public enum loggingCodes {
        START(100, "Server start working"),
        BADCONFIG(101, "Config is corrupted"),
        OPEN(200,"Connection open with"),
        REQUEST(201, "Client request contains"),
        RESPONSE(202, "Response"),
        FINISH(203, "Thread executed successfully"),
        NULL_INPUTSTREAM(401, "<WARNING> HTTP headers is missing");

        private final int value;
        private final String description;

        private loggingCodes(int value, String description) {
            this.description = description;
            this.value = value;
        }

        @Override
        public String toString() {
            return " " + value + " " + description + " ";
        }
    }

    private static BlockingQueue<String> loggingQueue = new ArrayBlockingQueue<String>(1024);

    public static void putLoggingQueue(String logMessage, String logEnding) throws InterruptedException {

        String result = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z").format(new Date()) + " " +
        "[" + Thread.currentThread().getName() + "]" + loggingCodes.valueOf(logMessage) + logEnding;



        loggingQueue.put(result);

    }

    public static void writeLog() throws InterruptedException {
        while (true) {



            String logging = loggingQueue.take();

            try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("WebServerLog.log", true)))) {
                out.println(logging);
            }catch (IOException e) {
            }

        }
    }

    public static String getServerRuntimeStatistics() {

        return "<pre>" +
                "Server Statistics\r\n" +
                "OS: " + System.getProperty("os.name") + System.getProperty("os.version") + "\r\n" +
                "Startup: " + WebServer.STARTINGTIME.getTime() + "\r\n" +
                "Uptime: " + upTime() + "\r\n" +
                "Memory: " + "\r\n" +
                "\t" + "MAX: " + Runtime.getRuntime().maxMemory()/1048576 + " MB\r\n" +
                "\t" + "FREE: " + Runtime.getRuntime().freeMemory()/1048576 + " MB\r\n" +
                "\t" + "TOTAL: " + Runtime.getRuntime().totalMemory()/1048576 + " MB\r\n" +
                "Longest thread execution: " + ClientSocketThread.longestExecution + " ms\r\n" +
                "Shortest thread execution: " + ClientSocketThread.shortestExecution + " ms\r\n" +
                "</pre>";
    }

    static private String upTime() {

        Duration duration = Duration.ofMillis(Calendar.getInstance().getTimeInMillis()
                - WebServer.STARTINGTIME.getTimeInMillis());
        return String.format("uptime: %d days %02d:%02d:%02d",
                duration.toDays(),
                duration.minusDays(duration.toDays()).toHours(),
                duration.minusHours(duration.toHours()).toMinutes(),
                duration.minusMinutes(duration.toMinutes()).getSeconds());

    }
}
