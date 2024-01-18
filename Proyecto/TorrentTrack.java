import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
public interface TorrentTrack extends Remote{
    public void saludo(String mensaje) throws RemoteException;
    public void addPeer(TorrentPeer peer,String ky, String name) throws RemoteException;
    //public TorrentPeer viewPeers() throws RemoteException;
    public List<String> viewPeers() throws RemoteException;
    public List<String> getFiles() throws RemoteException;
    public String downFile(String filename) throws RemoteException;
}
