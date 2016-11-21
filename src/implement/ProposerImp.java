package implement;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import common.Logger;
import interfaces.ProposerInterface;
import server.Paxos;

public class ProposerImp extends UnicastRemoteObject implements ProposerInterface {

	private Paxos paxos;
	private Logger logger;
	
	public ProposerImp(Paxos paxos, Logger logger) throws RemoteException {
		this.paxos = paxos;
		this.logger = logger;
	}

	@Override
	public void prepareOk(int n, String v) throws RemoteException {
		if (v != null) {
			paxos.setAcceptV(v);
		}
		paxos.receivePrepareOk();
		synchronized(paxos) {
			if (paxos.majorityPrepareOk()) {
				paxos.notifyAll();
			}
		}
	}

	@Override
	public void prepareReject() throws RemoteException {
		paxos.receivePrepareReject();
		synchronized (paxos) {
			if (paxos.majorityPrepareReject()) {
				paxos.notifyAll();
			}
		}
	}

	@Override
	public void acceptOk() throws RemoteException {
		paxos.receiveAcceptOk();
		synchronized(paxos) {
			if (paxos.majorityAcceptOk()) {
				paxos.notifyAll();
			}
		}
	}

	@Override
	public void acceptReject() throws RemoteException {
		paxos.receiveAcceptReject();
		synchronized(paxos) {
			if (paxos.majorityAcceptReject()) {
				notifyAll();
			}
		}
	}

}
