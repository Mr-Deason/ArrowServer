package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import common.Logger;
import implement.RPCImp;
import interfaces.RPCInterf;

public class RPCServer extends Thread{
	private int port;
	private HashMap<String, String> map = null;
	
	private Logger logger = null;
	
	public RPCServer(int port, HashMap<String, String> map, Logger logger) {
		this.port = port;
		this.map = map;
		this.logger = logger;
		this.start();
	}

	@Override
	public void run() {
		try {
			RPCInterf server = new RPCImp(map);
			LocateRegistry.createRegistry(port); 
			Context context = new InitialContext();
			context.rebind("rmi://localhost:" + port + "/RPCServer", server);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
