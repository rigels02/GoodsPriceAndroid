package org.rb.goodspriceandroid.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.rb.goodspriceandroid.model.Good;

/**
 *
 * @author Developer
 */
public class TcpServer {

    final static int PORT1 = 49152;
    final static int PORT2 = 65535;
    final static private int PORTRANGE = PORT2 - PORT1;
    final static String MAGSTR = "HiTCP!";
    final static long MAGNUM = 589412334;

    private int serverPort;

    private Status status;
    private String error;

    ServerSocket serverSocket = null;
    Socket socket = null;
    // received/sent goods count
    private long counter;
    private List<Good> goods;
    private DataInputStream din;
    private DataOutputStream dou;
    private ITcpEventSender tcpEventSender;

    public static boolean isAcceptablePort(int port) {
        return port >= PORT1 && port <= PORT2;
    }

    public static int getRandomPortNumber() {
        Random r = new Random();
        int port = PORT1 + r.nextInt(PORTRANGE);
        return port;
    }

    public TcpServer(int port, Status status) {

        this.goods = new ArrayList<>();
        this.serverPort = port;
        error = null;
        this.status = status;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    private boolean isHeaderReceived() throws IOException {
        String magStr1 = din.readUTF();
        long magNum = din.readLong();
        if ((!magStr1.equals(MAGSTR)) || (magNum != MAGNUM)) {
            String msg = "Handshaking problem!";
            Logger.getLogger(TcpServer.class.getName()).log(Level.SEVERE, null,
                    String.format("%s : magStr1= %s, magNum= %d", msg, magStr1, magNum));
            return false;
        }
        return true;
    }
    // --------------------

    public void setTcpEventSender(ITcpEventSender tcpEventSender) {
        this.tcpEventSender = tcpEventSender;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public List<Good> getGoods() {
        return goods;
    }

    public void setGoods(List<Good> goods) {

        this.goods = goods;
    }

    // ----------------
    public Status getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public InetAddress getServerHostAddress() throws UnknownHostException {
        return InetAddress.getLocalHost();
    }

    public String getServerHostAddressAsString() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    public void run() {

        int port = this.serverPort;

        try {
            serverSocket = new ServerSocket(port);

        } catch (Exception ex) {
            Logger.getLogger(TcpServer.class.getName()).log(Level.SEVERE, null, ex);
            returnError(ex.getMessage());
            return;
        }

        try {
            System.out.println(
                    String.format("Server %s port %d waiting for connection...", getServerHostAddress(), port));
            socket = serverSocket.accept();
            if (tcpEventSender != null) {
                tcpEventSender.connectionAccepted();
            }
            din = new DataInputStream(socket.getInputStream());
            dou = new DataOutputStream(socket.getOutputStream());

            if ((!isHeaderReceived()) || (!isHeaderSend())) {
                String msg = "Handshaking problem!";
                returnError(msg);
                return;
            }

            // send or read actions
            if (status == Status.RECEIVER) {
                if (acknowledgeReceive(din, dou)) {
                    receiveData(din);
                } else {
                    returnError("The other side is not sender as expected!");
                    return;
                }

            } else if (status == Status.SENDER) {
                if (acknowledgeToSend(din, dou)) {
                    writeData(dou);
                } else {
                    returnError("The other side is not receiver as expected!");
                    return;
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(TcpServer.class.getName()).log(Level.SEVERE, null, ex);
            returnError(ex.getMessage());
            return;

        }
        //status = Status.COMPLETED;

        returnCompleted();

    }

    private boolean isHeaderSend() {
        try {
            dou.writeUTF(MAGSTR);
            dou.writeLong(MAGNUM);
            dou.flush();
        } catch (IOException e) {
            String msg = "Handshaking problem! : ";
            Logger.getLogger(TcpServer.class.getName()).log(Level.SEVERE, null, msg + e.getMessage());
            return false;
        }

        return true;
    }

    private boolean acknowledgeToSend(DataInputStream din, DataOutputStream dou) throws IOException {
        dou.writeUTF("SEND2YOU");
        dou.flush();
        String token = din.readUTF();
        if (!token.equals("READY")) {

            return false;
        }
        return true;
    }

    private boolean acknowledgeReceive(DataInputStream din, DataOutputStream dou) throws IOException {
        dou.writeUTF("SEND2ME");
        dou.flush();
        String token = din.readUTF();
        if (!token.equals("READY")) {

            return false;
        }
        return true;
    }

    private void writeData(DataOutputStream dou) throws IOException {
        if (goods.size() > 0) {
            Logger.getLogger(TcpServer.class.getName()).log(Level.INFO, "sendig Data");
        }
        dou.writeLong(goods.size());
        dou.flush();
        for (int i = 0; i < goods.size(); i++) {
            Good.putToStream(dou, goods.get(i));
            dou.flush();
        }

    }

    private void receiveData(DataInputStream din) throws IOException {

        this.counter = din.readLong();
        if (counter > 0) {
            Logger.getLogger(TcpServer.class.getName()).log(Level.INFO, "receiving Data");
        }
        while (counter > 0) {
            Good good = Good.getFromStream(din);
            goods.add(good);
            counter--;
        }
        
    }

    private void closeAll() {
        try {
            if (din != null) {
                din.close();
            }
            if (dou != null) {
                dou.close();
            }
            if (socket != null) {
                socket.close();
            }

        } catch (IOException ex) {
            Logger.getLogger(TcpServer.class.getName()).log(Level.SEVERE, null, ex);

            return;
        }

    }

    private void returnError(String message) {
        error = message;
        status = Status.ERROR;
        closeAll();
    }

    private void returnCompleted() {
        closeAll();
        error = null;
        if (status == Status.RECEIVER) {
            System.out.println("Server reception completed");
        } else {
            System.out.println("Server sending completed");
        }
        status = Status.COMPLETED;

    }

    public static void main(String args[]) throws UnknownHostException {

        TcpServer server = new TcpServer(63219, Status.RECEIVER);
        InetAddress addr = server.getServerHostAddress();
        String saddr = server.getServerHostAddressAsString();
        int port = TcpServer.getRandomPortNumber();

        server.run();
        System.out.println("Status: " + server.getStatus());
        System.out.println("Received goods: " + server.getGoods());
        try {
            //Force to close from external source
            server.getServerSocket().close();
        } catch (IOException ex) {
            Logger.getLogger(TcpServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
