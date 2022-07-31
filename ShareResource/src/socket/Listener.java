package socket;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.security.*;
import java.security.cert.CertificateException;

public class Listener {
    private final ServerSocket listener;
    private final StorageInterface storage;

    /**
     * crate a plain socket - no encryption
     *
     * @param port port in which the socket will be operated
     * @throws IOException              when the given port is in use already
     * @throws IllegalArgumentException when the given type of the storage is incorrect
     */
    public Listener(
            int port,
            StorageType storageType
    ) throws IOException, IllegalArgumentException {
        this.listener = new ServerSocket(port);
        switch (storageType) {
            case ONE_TO_ONE:
                storage = new OneToOneStorage();
                break;
            case ONE_TO_MANY:
                storage = new OneToManyStorage();
                break;
            default:
                throw new IllegalArgumentException("The storage type param is incorrect");
        }
    }

    /**
     * create a ssl socket
     *
     * @param keyStoreFile
     * @param keyStoreFilePassword
     * @param port
     * @throws KeyStoreException
     * @throws IOException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws UnrecoverableKeyException
     * @throws KeyManagementException
     */
    public Listener(
            String keyStoreFile,
            String keyStoreFilePassword,
            int port,
            StorageType storageType
    ) throws KeyStoreException,
            IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        KeyStore keyStore;
        keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(keyStoreFile), keyStoreFilePassword.toCharArray());

        KeyManagerFactory keyManagerFactory;
        keyManagerFactory = KeyManagerFactory.getInstance("X509");
        keyManagerFactory.init(keyStore, keyStoreFilePassword.toCharArray());

        TrustManagerFactory trustManagerFactory;
        trustManagerFactory = TrustManagerFactory.getInstance("X509");
        trustManagerFactory.init(keyStore);

        SSLContext sslContext;
        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

        SSLServerSocketFactory sslServerSocketFactory;
        sslServerSocketFactory = sslContext.getServerSocketFactory();
        this.listener = sslServerSocketFactory.createServerSocket(port);

        switch (storageType){
            case ONE_TO_ONE:
                storage = new OneToManyStorage();
                break;
            case ONE_TO_MANY:
                storage = new OneToManyStorage();
                break;
            default:
                throw new IllegalArgumentException("Not a valid option for storage");
        }
    }

    //todo include ssl socket

    /**
     * accept an incoming socket to the listener
     *
     * @return the accepted socket
     * @throws IOException when fail to accept the socket
     */
    public Socket accept() throws IOException {
        return new Socket(listener.accept());
    }

    /**
     * close this listener and all the sockets which accepted by this listener.
     *
     * @throws IOException when this socket is failed to close, some memory will be leaked since some socket will not be closed
     */
    public void close() throws IOException {
        listener.close();
        for (Socket socket : storage) {
            socket.close();
        }
    }

    public void putSocket(String key, Socket socket) throws IllegalArgumentException {
        this.storage.add(key, socket);
    }

    public Socket getSocket(String key) throws NullPointerException {
        return storage.get(key);
    }

    public Socket getSocket(String key, int id) {
        try {
            return storage.get(key, id);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Socket removeSocket(Socket socket) {
        return storage.remove(socket);
    }
}
