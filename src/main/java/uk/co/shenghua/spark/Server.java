package uk.co.shenghua.spark;

import javax.servlet.ServletOutputStream;
import java.io.*;

import static spark.Spark.*;

/**
 * Hello world!
 */
public class Server {
    /**
     * @param args args[0] should be the absolute path of keystore.jks,
     *             args[1] should be the absolute path of the file to be downloaded
     *             e.g. /projectPath/keystore.jks /projectPath/fileToDownload
     */
    public static void main(String[] args) {

        secure(args[0], "password", null, null);
        String filePath = args[1];

        after((request, response) -> response.header("Content-Encoding", "gzip"));

        get("/", (request, response) -> "Hello World");
        get("/hello", (request, response) -> "Hello");
        get("/download", (request, response) -> {

            ServletOutputStream outputStream = response.raw().getOutputStream();

            try {
                File fileToDownload = new File(filePath);
                InputStream inputStream = new BufferedInputStream(new FileInputStream(fileToDownload));
                DataInputStream dataInputStream = new DataInputStream(inputStream);
                response.raw().setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileToDownload.getName()));
                response.raw().setContentLength((int) fileToDownload.length());

                int i = 0;
                while (dataInputStream.available() > 0) {
                    outputStream.write(dataInputStream.read());
                    if (++i % 10 == 0) {
                        outputStream.flush();
                    }
                }

                outputStream.flush();
                outputStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return response;
        });

    }
}
