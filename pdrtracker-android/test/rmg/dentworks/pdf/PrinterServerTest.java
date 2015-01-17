package rmg.pdrtracker.pdf;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class PrinterServerTest {


    public final static void main(String[] argv) throws Exception {

        try {
            FileOutputStream fout = new FileOutputStream("/tmp/tmp.txt");
            ServerSocket serverSocket = new ServerSocket(9100);
            Socket clientSocket = serverSocket.accept();
            InputStream in = clientSocket.getInputStream();

            byte[] bytes = new byte[1024];
            int numRead;
            while ((numRead = in.read(bytes)) != -1) {
                StringBuilder sbuf = new StringBuilder();
                for (int i = 0; i < numRead; i++) {
                    sbuf.append((char) bytes[i]);
                }
                fout.write(sbuf.toString().getBytes());
            }

            fout.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
