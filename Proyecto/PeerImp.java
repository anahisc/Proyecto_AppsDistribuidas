import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import javax.swing.SingleSelectionModel;
public class PeerImp implements Runnable{
    public String ipAdd;
    public int port;
    public PeerConnection peerServ;
    
    public PeerImp(String ipAdd, int port, PeerConnection peerServ) throws RemoteException{
        this.ipAdd = ipAdd;
        this.port = port;
        this.peerServ = peerServ;
    }
    /* public PeerImp() throws RemoteException{
        super();
    }
    @Override
    public void saludo(String usId, String ip,int port) throws RemoteException{
        System.out.println("Hola soy el cliente "+usId+" con ip:"+ip+ " en el puerto "+Integer.toString(port));
    } */
    public void run(){
        try {
            Registry registry = LocateRegistry.createRegistry(port);
            registry.bind("TorrentPeer", peerServ);
            System.out.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
        
    }
    public static void main(String[] args) throws RemoteException, NotBoundException, IOException {
        //CONEXION A TRACKER
        Registry registry = LocateRegistry.getRegistry(args[3], 1099);
        TorrentTrack server = (TorrentTrack) registry.lookup("TorrentTrack");
        //SERVIDOR PROPIO
        PeerConnection peerC = new PeerConnection(args[0],args[1],Integer.parseInt(args[2]),server,args[4]);
        new Thread(new PeerImp(args[1],Integer.parseInt(args[2]),peerC)).start();
        new Thread(peerC).start();
        /*try{
            PeerConnection Peer = new PeerConnection();
            Peer.setClientID(args[0]);
            Peer.setHostIP(args[1]);
            Peer.setPort(Integer.parseInt(args[2]));
            System.out.println(Peer.toString());
           
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
            TorrentTrack stub = (TorrentTrack) registry.lookup("TorrentTrack");

            Peer.setServer(stub);
            String rq=Peer.getServer().addPeer(Peer.getClientID(),Peer.getHostIP(), Peer.getPort());
            //System.out.println(rq);
            List<String> pL = Peer.getServer().viewPeers();
            for (String m : pL) {
                System.out.println(m);
            }
            /* 
            Scanner teclado = new Scanner(System.in);
            int answ = 0;
            //do{
                System.out.println("1. Enviar");
                System.out.println("2. Recibir");
                System.out.println("3. Salir");
                System.out.println("Su opcion:");
                answ=Integer.parseInt(teclado.nextLine());
                if(answ==1){
                    String usId;
                    System.out.println("ID del usuario a enviar");
                    usId=teclado.nextLine();
                    if(usId.equals("A")){
                        Registry registryA = LocateRegistry.getRegistry("127.0.0.1",5000);
                        TorrentPeer stubA = (TorrentPeer) registry.lookup("TorrentPeer");
                        stubA.saludo("B", "127.0.0.1", 5050);
                    }else if(usId.equals("B")){
                        Registry registryB = LocateRegistry.getRegistry("127.0.0.1",5050);
                        TorrentPeer stubB = (TorrentPeer) registry.lookup("TorrentPeer");
                        stubB.saludo("A", "127.0.0.1", 5000);
                    }
                }else if(answ==2){
                     //REGISTRO DE SERVER 
                    Registry registryS = LocateRegistry.createRegistry(Integer.parseInt(args[2]));
                    registryS.bind("TorrentPeer", peerS);    
                    System.out.println("Server ready");   
                }
            //}while(answ>0 && answ<3);
        }catch(Exception e){
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
        */
        
    }
}
