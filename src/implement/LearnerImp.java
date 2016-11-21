package implement;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import common.Logger;
import common.Operation;
import interfaces.LearnerInterface;
import server.Paxos;

public class LearnerImp extends UnicastRemoteObject implements LearnerInterface {

	private Paxos paxos;
	private Logger logger;
	private HashMap<String, String> map;
	
	public LearnerImp(Paxos paxos, HashMap<String, String> map, Logger logger) throws RemoteException {
		this.paxos = paxos;
		this.map = map;
		this.logger = logger;
	}

	@Override
	public String commit(String op) throws RemoteException {

		Operation operation;
		try {
			
			logger.append("[INFO] commit");
			
			paxos.setAcceptV(null);
			operation = new Operation(op);
			return operation.exec(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
