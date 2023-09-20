package helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;

public class BaseHelper {
    private static final Logger logger = LoggerFactory.getLogger(BaseHelper.class);

    public int generateRandomPortNumber(){
        int portNumber = 0;
        boolean isValidPortNumber = false;
        while (!isValidPortNumber) {
            portNumber = (int) (Math.random() * 10000);
            if (portNumber <= 0 || portNumber > 65535) {
                logger.warn("Invalid port number: " + portNumber);
                continue;
            }
            try (ServerSocket ignored = new ServerSocket(portNumber)) {
                isValidPortNumber = true;
            } catch (Exception e) {
                logger.warn("Port " + portNumber + " is already in use");
            }
        }
        return portNumber;
    }
}
