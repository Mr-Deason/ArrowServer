package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
/*
 * interface of RPC operation
 */
public interface RPCInterf extends Remote {

	public String request(String op) throws RemoteException;
	
}
