import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackerImp{
    /* public List<PeerConnection>  peers; 

    public TrackerImp() throws RemoteException{
        peers=new ArrayList<>();
    }
    
    @Override
    public void saludo(String menaje) throws RemoteException{
        System.out.println("Hola soy el cliente");
    } */
    /* @Override
    public String addPeer(String clienteID, String hostIP, int port) throws RemoteException{
        /* PeerConnection peer = new PeerConnection(clienteID, hostIP, port);
        //System.out.println("Entra");
        System.out.println(peer.toString());
        int flag =0;
        peers.add(peer);
        return "Ha sido anadido"; */
        /* if(peers.isEmpty()){
            peers.add(peer);
            return "Ha sido anadido";
        }else{
            for (PeerConnection p : peers) {
                if(peer.getClientID() == p.getClientID()){
                    flag =1;
                    break;
                }
            }
            if(flag==0){
                peers.add(peer);
                return "Ha sido anadido";
            }else{
                return "Ya fue anadido antes";
            }
        } */
        
    //} */
    /* @Override
    public List<String> viewPeers() throws RemoteException{
        List<String> pL = new ArrayList();
        for (PeerConnection p : peers) {
            pL.add(p.getClientID()+"-"+p.getHostIP()+"-"+Integer.toString(p.getPort()));
        }
        return pL;
    } */
    public static void main(String[] args) throws RemoteException {
        try {
            TrackerConnection server = new TrackerConnection();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("TorrentTrack", server);
            System.out.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

}