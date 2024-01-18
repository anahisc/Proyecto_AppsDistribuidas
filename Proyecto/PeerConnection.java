import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.List;
import java.io.*;
import java.nio.charset.StandardCharsets;
public class PeerConnection extends UnicastRemoteObject implements TorrentPeer,Runnable{
    public String clientID;
    public String hostIP;
    public int port;
    public TorrentTrack server;
    public String filename;


    @Override
    public String toString() {
        return "PeerConnection [clientID=" + clientID + ", hostIP=" + hostIP + ", port=" + port + "]";
    }


    public PeerConnection(String clientID, String hostIP, int port, TorrentTrack server, String filename)throws RemoteException{
        this.clientID=clientID;
        this.hostIP=hostIP;
        this.port=port;
        this.server=server;
        this.filename = filename;
        server.addPeer(this,this.clientID+":"+this.hostIP+":"+this.port+":"+this.filename,this.filename);
    }

    public String getClientID() {
        return clientID;
    }

    public String getHostIP() {
        return hostIP;
    }

    public int getPort() {
        return port;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public void setHostIP(String hostIP) {
        this.hostIP = hostIP;
    }

    public void setPort(int port) {
        this.port = port;
    }
    public void saludo(String mensaje) throws RemoteException{
        System.out.println(mensaje);
    }
    public int sizeFile(String filename) throws RemoteException{
        File file = new File(filename);
        long fileSize = file.length();
        long chunkSize = fileSize / 10;
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            byte[] buffer = new byte[(int) chunkSize];
            int bytesRead = 0;
            int c = 0;
            while ((bytesRead = bis.read(buffer)) != -1) {
                c++;
            }
            return c;
        }catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
            return -1;
        }
    }
    @Override
    public byte[] sendData(String filename) throws RemoteException{
        /* File file = new File(filename);
        long fileSize = file.length();
        long chunkSize = fileSize / 10;
        byte[] buffer = new byte[(int) chunkSize];
        byte[] bufferAux = new byte[(int) chunkSize];
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            int bytesRead = 0;
            int c = 0;
            while ((bytesRead = bis.read(buffer)) != -1) {
                if(c==indx){
                    bufferAux= buffer;
                }
                c++;
            }
        }catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
        return bufferAux; */
        File file = new File(filename);
        byte[] contenidoArchivo = new byte[(int) file.length()];

        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(contenidoArchivo);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contenidoArchivo;
    }
    public void run(){
        try{
            //ENTRADA DE TECLADO
            Scanner teclado = new Scanner(System.in);
            //MENU
            int answ=0;
            while(true){
                System.out.println("1. Ver Peers");
                System.out.println("2. Ping");
                System.out.println("3. Ver Archivos");
                System.out.println("4. Descargar Archivo");
                System.out.println("5. Salir");
                System.out.println("Su opcion:");
                answ=Integer.parseInt(teclado.nextLine());
                if(answ==1){
                    System.out.println("Aqui se veran lo otros clientes");
                    List<String> peerInf = server.viewPeers();
                    for (String pi : peerInf) {
                        System.out.println(pi);
                    }
                }else if(answ==2){
                    //REGISTRO DE SERVER
                    /* String mensaje; 
                    Scanner t = new Scanner(System.in);
                    mensaje = t.nextLine();
                    server.saludo(clientID+":"+mensaje); */  
                    Scanner t1 = new Scanner(System.in);
                    Scanner t2 = new Scanner(System.in);
                    System.out.println("Direccion IP:");
                    String ip = t1.nextLine();
                    System.out.println("Puerto:");
                    int puerto = Integer.parseInt(t2.nextLine());
                    Registry registry = LocateRegistry.getRegistry(ip, puerto);
                    TorrentPeer serverP = (TorrentPeer) registry.lookup("TorrentPeer");
                    serverP.saludo(clientID+":"+hostIP+":"+port);
                }else if(answ==3){
                    System.out.println("Estos son los archivos disponibles en la red");
                    List<String> files = server.getFiles();
                    for (String f : files) {
                        System.out.println(f);
                    }
                }else if(answ==4){
                    Scanner t1 = new Scanner(System.in);
                    System.out.println("Escriba el nombre del archivo:");
                    String ff = t1.nextLine();
                    String fff=server.downFile(ff);
                    String[] trama = fff.split(":");
                    Registry registry = LocateRegistry.getRegistry(trama[1], Integer.parseInt(trama[2]));
                    TorrentPeer serverP = (TorrentPeer) registry.lookup("TorrentPeer");
                    //System.out.println(serverP.sizeFile(trama[3]));
                    int numChunk = serverP.sizeFile(trama[3]);
                    try (FileOutputStream fos = new FileOutputStream(trama[3], true)) {
                        fos.write(serverP.sendData(trama[3]));
                    } catch (IOException e) {
                        System.err.println("Error writing file: " + e.getMessage());
                    }

                }else if(answ==5){
                    System.exit(0);
                }
            }

        }catch(Exception e){
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
        
    }

    
    
}
