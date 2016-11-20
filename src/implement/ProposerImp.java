package implement;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import common.Logger;
import interfaces.ProposerInterface;
import server.Paxos;

public class ProposerImp extends UnicastRemoteObject implements ProposerInterface {

	private Paxos paxos;
	private Logger logger;
	
	protected ProposerImp(Paxos paxos, Logger logger) throws RemoteException {
		this.paxos = paxos;
		this.logger = logger;
	}

	@Override
	public void prepareOk(int n, String v) throws RemoteException {
		if (v != null) {
			paxos.setOp(v);
		}
		paxos.receivePrepareOk();
		if (paxos.majorityPrepareOk()) {
			
		}
	}

	@Override
	public void prepareReject() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void acceptOk(int n, String v) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void acceptReject() throws RemoteException {
		// TODO Auto-generated method stub

	}

}
