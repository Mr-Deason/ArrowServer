package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/*
 * interface of RPC operation
 */
public interface ProposerInterface extends Remote  {

	public void prepareOk(int n, String v) throws RemoteException;
	
	public void prepareReject() throws RemoteException;
	
	public void acceptOk(int n, String v) throws RemoteException;
	
	public void acceptReject() throws RemoteException;
}
