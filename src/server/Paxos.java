package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import interfaces.AcceptorInterface;
import interfaces.LearnerInterface;
import interfaces.RPCInterf;

public class Paxos{

	private int myN;
	private int highN;
	private String op;
	private ArrayList<ServerEntity> servers;
	private int prepareCnt;
	private int acceptCnt;
	
	public Paxos(ArrayList<ServerEntity> servers) {
		this.servers = servers;
		this.myN = 1;
		this.highN = 1;
	}
	
	public ArrayList<ServerEntity> getServers() {
		return servers;
	}
	
	public int findPort(String host) {
		for (ServerEntity serverEntity : servers) {
			if (serverEntity.getAddress().equals(host)) {
				return serverEntity.getPort();
			}
		}
		return -1;
	}
	
	public void setMyN(int myN) {
		this.myN = myN;
	}

	public void setHighN(int highN) {
		this.highN = highN;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public int getMyN() {
		return myN;
	}
	
	public int getHighN() {
		return highN;
	}
	
	public String getOp() {
		return op;
	}
	
	public boolean majorityPrepareOk() {
		return prepareCnt > (servers.size()/2);
	}
	
	public boolean majorityAcceptOk() {
		return acceptCnt > (servers.size()/2);
	}
	
	public void receivePrepareOk() {
		++prepareCnt;
	}
	
	public void receiveAcceptOk() {
		++acceptCnt;
	}
	
	public void prepare() {
		
		prepareCnt = 0;
		
		myN = highN + 1;

		AcceptorInterface acceotor = null;
		for (ServerEntity serverEntity : servers) {
			try {
				acceotor = (AcceptorInterface) Naming.lookup("rmi://" + serverEntity.getAddress() + ':' + serverEntity.getPort() + "/Acceptor");
				acceotor.prepare(myN);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void accept(String op) {

		acceptCnt = 0;
		
		this.op = op;
		
		AcceptorInterface acceotor = null;
		for (ServerEntity serverEntity : servers) {
			try {
				acceotor = (AcceptorInterface) Naming.lookup("rmi://" + serverEntity.getAddress() + ':' + serverEntity.getPort() + "/Acceptor");
				acceotor.accept(myN, op);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void commit(ArrayList<ServerEntity> servers) {

		LearnerInterface learner = null;
		for (ServerEntity serverEntity : servers) {
			try {
				learner = (LearnerInterface) Naming.lookup("rmi://" + serverEntity.getAddress() + ':' + serverEntity.getPort() + "/Learner");
				learner.commit(op);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
