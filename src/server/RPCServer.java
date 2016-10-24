package server;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Date;
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
			logger.append("[INFO] starting RPC server at port " + port + "...");
			System.out.println("starting RPC server at port " + port + "...");
			RPCInterf server = new RPCImp(map);
			LocateRegistry.createRegistry(port);
			Context context = new InitialContext();
			context.rebind("rmi://localhost:" + port + "/RPCServer", server);
			logger.append("[INFO] RPC server started in \"rmi://localhost:" + port + "/RPCServer" + "\" waiting for client...");
			System.out.println("RPC server started in \"rmi://localhost:" + port + "/RPCServer" + "\" waiting for client...");
			
		} catch (Exception e) {
			try {
				logger.append("[ERROR] " + e.getMessage());
			} catch (IOException e1) {
				System.out.println(e.getMessage());
			}
		}
	}
}
