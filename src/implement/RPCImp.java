package implement;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import interfaces.RPCInterf;

public class RPCImp extends UnicastRemoteObject implements RPCInterf{
	
	HashMap<String, String> map = null;

	public RPCImp(HashMap<String, String> map) throws RemoteException {
		this.map = map;
	}

	public String get(String key) throws RemoteException {
		return map.get(key);
	}
	
	public int put(String key, String value) throws RemoteException {
		map.put(key, value);
		return 0;
	}
	
	public int delete(String key) throws RemoteException {
		if (!map.containsKey(key)) {
			return -1;
		}
		map.remove(key);
		return 0;
	}
}
