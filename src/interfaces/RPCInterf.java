package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
/*
 * interface of RPC operation
 */
public interface RPCInterf extends Remote {

	public String get(String key) throws RemoteException;
	
	public int put(String key, String value) throws RemoteException;
	
	public int delete(String key) throws RemoteException;
	
}
