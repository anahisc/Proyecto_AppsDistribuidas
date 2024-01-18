import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.SourceDataLine;

public class TrackerConnection extends UnicastRemoteObject implements TorrentTrack {
    public ArrayList<TorrentPeer> peers;
    public List<String> kys;
    public List<String> files;

    public TrackerConnection() throws RemoteException{
        peers = new ArrayList<TorrentPeer>();
        kys = new ArrayList<String>();
        files = new ArrayList<String>();
    }
    @Override
    public synchronized void addPeer(TorrentPeer peer, String ky, String filename) throws RemoteException{
        this.peers.add(peer);
        this.kys.add(ky);
        this.files.add(filename);
        System.out.println("Se ha anadido: " + ky);
    }
    @Override
    public synchronized void saludo(String mensaje)throws RemoteException{
        int i =0;
        while(i<peers.size()){
            peers.get(i++).saludo(mensaje);
        }
    }
    @Override
    public String downFile(String filename)throws RemoteException{
       String pInf="";
       for (String k : kys) {
            String[] trama;
            trama = k.split(":");
            String cID = trama[0];
            String hIP = trama[1];
            String pt = trama[2];
            String fil = trama[3];
            if(fil.equals(filename)){
                pInf=cID+":"+hIP+":"+pt+":"+fil;
                break;
            }
       }
       return pInf;
    }
    @Override
    public  List<String> viewPeers() throws RemoteException{
        return kys;
    }
    @Override
    public List<String> getFiles() throws RemoteException{
        return files;
    }
}
