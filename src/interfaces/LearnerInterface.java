package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/*
 * interface of RPC operation
 */
public interface LearnerInterface extends Remote  {

	public String commit(String op) throws RemoteException;

}
