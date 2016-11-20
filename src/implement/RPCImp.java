package implement;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import common.Logger;
import common.Operation;
import interfaces.RPCInterf;
import server.Paxos;
import server.Proposer;
import server.ServerEntity;
import sun.net.www.content.text.plain;

/*
 * implement of RPC operation
 */
public class RPCImp extends UnicastRemoteObject implements RPCInterf {

	Logger logger;
	Paxos paxos;

	public RPCImp(Paxos paxos, Logger logger) throws RemoteException {
		this.paxos = paxos;
		this.logger = logger;
	}

	public String request(String op) throws RemoteException{
		Operation operation = null;
		try {
			operation = new Operation(op);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (operation.isGet()) {
			int index = new Random().nextInt(paxos.getServers().size());
			//
		}
		else {
			
		}
		return null;
	}
}
