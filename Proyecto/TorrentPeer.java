import java.rmi.Remote;
import java.rmi.RemoteException;
public interface TorrentPeer extends Remote{
    public void saludo(String mensaje) throws RemoteException;
    public int sizeFile(String filename) throws RemoteException;
    public byte[] sendData(String filename) throws RemoteException;
}
