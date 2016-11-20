package server;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;

import javax.naming.Context;
import javax.naming.InitialContext;

import common.Logger;
import implement.RPCImp;
import interfaces.RPCInterf;

public class Acceptor extends Thread {

	private Paxos paxos;
	
	private int port;
	
	private Logger logger = null;
	
	public Acceptor(Paxos paxos, int port, Logger logger) {
		this.paxos = paxos;
		this.port = port;
		this.logger = logger;
		this.start();
	}
	
	@Override
	public void run() {
		try {
			logger.append("[INFO] starting RPC server at port " + port + "...");
			System.out.println("starting RPC server at port " + port + "...");
			RPCInterf server = new RPCImp(map);
			
			//register RPC server
			LocateRegistry.createRegistry(port);
			Context context = new InitialContext();
			context.rebind("rmi://localhost:" + port + "/Proposer", server);
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
