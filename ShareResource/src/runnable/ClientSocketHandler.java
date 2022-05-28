package runnable;

import chain.Chain;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import socket.Socket;

import java.io.IOException;

/**
 * A class control the behaviour of {@link Socket} which run on the side connect to the {@link socket.Listener}.<br>
 * step 1: write out the name of this socket.<br>
 * step 2: read the id which sent back by the listener.<br>
 * step 3: loop to read the request from the server. for every request receive from the server, a {@link Thread} will be
 * spawn to handle it.<br>
 * step 4: the perpetual loop is broke due to server disconnected to this socket or this socket disconnected itself,
 * close the socket, and exit the progam.
 */
public abstract class ClientSocketHandler implements Runnable {
    private final Socket socket;
    private final String moduleName;

    public ClientSocketHandler(Socket socket, String moduleName) {
        this.socket = socket;
        this.moduleName = moduleName;
    }

    @Override
    public void run() {
        // declare socket and store necessary information to / from DataStream
        try { // declare the name of this module to the DataStream
            socket.write(moduleName);
        } catch (IOException e) {
            System.err.println("Cannot declare name of the socket to the DataStream");
            e.printStackTrace();
            try { // close the socket due to fail to send necessary information
                socket.close();
            } catch (IOException ignore) {
            }
        }
        try { // get the instance number of this module from the DataStream
            saveInstanceNumber(Integer.parseInt(socket.read()));
        } catch (IOException e) {
            System.err.println("Cannot read the instance number sent back by DataStream");
            e.printStackTrace();
            try { // close socket due to fail to read necessary information
                socket.close();
            } catch (IOException ignore) {
            }
        } catch (NumberFormatException e) {
            System.err.println("the instance number sent back is not a number");
            e.printStackTrace();
            try { // close socket due to fail to read necessary information
                socket.close();
            } catch (IOException ignore) {
            }
        }
        // repeated loop to read / process data from DataStream
        while (true) {
            // read from socket
            String receivedPacket;
            try { // read from the socket
                receivedPacket = socket.read();
            } catch (IOException e) {
                System.err.println("Fail to read from socket");
                e.printStackTrace();
                break;
            }
            // if the data read from socket is null, then the socket is closed
            if (receivedPacket == null) break;
            // create a runnable to handle message just closed from the socket
            Runnable resolveRequestRunnable;
            resolveRequestRunnable = () -> { // declare new runnable to process request as a sub-thread
                Gson gson = new Gson(); // new json due to different thread, need separate variable, avoid data of one thread mushing with another
                JsonObject processObject = gson.fromJson(receivedPacket, JsonObject.class); // converting package from String to json format
                boolean isResolved = getResolveChain(processObject).resolve();
                if (!isResolved) { // if the chain is not resolved
                    processObject.get("header").getAsJsonObject().addProperty("status" , false); // set status to false
                    getRejectChain(processObject).resolve();
                }
            };
            // start a runnable for each request
            new Thread(resolveRequestRunnable).start();
        }
        //after the loop, just closse the socket.
        try {
            socket.close();
        }catch (IOException e){
            System.err.println("DataStream is closed");
            e.printStackTrace();
        }
        // force exit if any other thread is running (module running listener as separated thread) cease all at once.
        System.exit(1);
    }

    /**
     * action to save the instance number to use later on message which will be sent to the DataStream
     *
     * @param instNumb assigned identification number given by DataStream
     */
    protected abstract void saveInstanceNumber(int instNumb);

    /**
     * the resolve chain to process the request
     *
     * @param processObject received Object
     * @return boolean value represent that the chain is successfully resolved or not
     */
    protected abstract Chain getResolveChain(JsonObject processObject);

    /**
     * if the resolve chain is fail to process the request, this chain will be run
     *
     * @param processObject received object and half processed by resolve chain
     * @return boolean value represent that the chain is successfully resolved or not
     */
    protected abstract Chain getRejectChain(JsonObject processObject);
}
