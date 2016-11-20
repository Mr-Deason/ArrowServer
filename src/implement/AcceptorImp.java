package implement;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;

import common.Logger;
import interfaces.AcceptorInterface;
import interfaces.ProposerInterface;
import server.Paxos;

public class AcceptorImp extends UnicastRemoteObject implements AcceptorInterface {

	private Paxos paxos;
	private Logger logger;
	
	public AcceptorImp(Paxos paxos, Logger logger) throws RemoteException {
		this.paxos = paxos;
		this.logger = logger;
	}
	@Override
	public void prepare(int n) throws RemoteException {
		String hostname;
		try {
			hostname = getClientHost();
			int port = paxos.findPort(hostname);
			ProposerInterface proposer = (ProposerInterface) Naming.lookup("rmi://" + hostname + ':' + port + "/Proposer");
			proposer.prepareOk(paxos.getMyN(), null);
		} catch (ServerNotActiveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void accept(int n, String v) throws RemoteException {
		String hostname;
		try {
			hostname = getClientHost();
			int port = paxos.findPort(hostname);
			ProposerInterface proposer = (ProposerInterface) Naming.lookup("rmi://" + hostname + ':' + port + "/Proposer");
			proposer.acceptOk(paxos.getMyN(), null);
		} catch (ServerNotActiveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
