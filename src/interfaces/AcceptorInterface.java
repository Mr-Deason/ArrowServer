package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AcceptorInterface extends Remote {

	public void prepare(int n)throws RemoteException;
	
	public void accept(int n, String v)throws RemoteException;
	
}
