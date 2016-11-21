package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import interfaces.AcceptorInterface;
import interfaces.LearnerInterface;
import interfaces.RPCInterf;

public class Paxos {

	private int myN;
	private int highN;
	private int acceptN;
	private String acceptV;
	private String myV;
	private ArrayList<ServerEntity> servers;
	private int prepareOkCnt;
	private int acceptOkCnt;
	private int prepareRejectCnt;
	private int acceptRejectCnt;
	private boolean abort;

	String res = null;

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
		return 18409;
	}

	public int getMyN() {
		return myN;
	}

	public void setMyN(int myN) {
		this.myN = myN;
	}

	public int getHighN() {
		return highN;
	}

	public void setHighN(int highN) {
		this.highN = highN;
	}

	public int getAcceptN() {
		return acceptN;
	}

	public void setAcceptN(int acceptN) {
		this.acceptN = acceptN;
	}

	public String getAcceptV() {
		return acceptV;
	}

	public void setAcceptV(String acceptV) {
		this.acceptV = acceptV;
	}

	public String getMyV() {
		return myV;
	}

	public void setMyV(String myV) {
		this.myV = myV;
	}

	public boolean majorityPrepareOk() {
		return prepareOkCnt > (servers.size() / 2);
	}

	public boolean majorityAcceptOk() {
		return acceptOkCnt > (servers.size() / 2);
	}

	public boolean majorityPrepareReject() {
		return prepareRejectCnt > (servers.size() / 2);
	}

	public boolean majorityAcceptReject() {
		return acceptRejectCnt > (servers.size() / 2);
	}

	public void receivePrepareOk() {
		++prepareOkCnt;
	}

	public void receiveAcceptOk() {
		++acceptOkCnt;
	}

	public void receivePrepareReject() {
		++prepareRejectCnt;
	}

	public void receiveAcceptReject() {
		++acceptRejectCnt;
	}

	public boolean isAbort() {
		return abort;
	}

	public void begin(String op) {

//		System.out.println("Paxos begin");

		this.acceptV = null;
		this.myV = op;

		prepareOkCnt = 0;
		prepareRejectCnt = 0;
		acceptOkCnt = 0;
		acceptRejectCnt = 0;
		abort = false;

		myN = highN + 1;

	}

	public void prepare() {

//		System.out.println("Paxos prepare");

		try {
			for (ServerEntity serverEntity : servers) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							AcceptorInterface acceotor = (AcceptorInterface) Naming.lookup(
									"rmi://" + serverEntity.getAddress() + ':' + serverEntity.getPort() + "/Acceptor");
							acceotor.prepare(myN);
						} catch (MalformedURLException | RemoteException | NotBoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
			}
//			System.out.println("prepared");
			synchronized (this) {
				while (!majorityPrepareOk() && !majorityPrepareReject()) {
					wait(3000);
					if (!majorityPrepareOk() && !majorityPrepareReject()) {
						abort = true;
						return;
					}
				}
				if (majorityPrepareReject()) {
					abort = true;
					return;
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void accept() {

//		System.out.println("Paxos accept");

		if (abort) {
			return;
		}

		try {
			for (ServerEntity serverEntity : servers) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						AcceptorInterface acceotor;
						try {
							acceotor = (AcceptorInterface) Naming.lookup(
									"rmi://" + serverEntity.getAddress() + ':' + serverEntity.getPort() + "/Acceptor");
							acceotor.accept(myN, myV);
						} catch (MalformedURLException | RemoteException | NotBoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
			}
			synchronized (this) {
				while (!majorityAcceptOk() && !majorityAcceptReject()) {
					wait(3000);
					if (!majorityAcceptOk() && !majorityAcceptReject()) {
						abort = true;
						return;
					}
				}
				if (majorityAcceptReject()) {
					abort = true;
					return;
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String commit() {

//		System.out.println("Paxos commit");

		if (abort) {
			acceptV = null;
			return "-1 request aborted";
		}

		LearnerInterface learner;
		for (ServerEntity serverEntity : servers) {
			try {
				learner = (LearnerInterface) Naming
						.lookup("rmi://" + serverEntity.getAddress() + ':' + serverEntity.getPort() + "/Learner");
				res = learner.commit(acceptV);
			} catch (MalformedURLException | RemoteException | NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			new Thread(new Runnable() {
//				
//				@Override
//				public void run() {
//					try {
//					} catch (MalformedURLException | RemoteException | NotBoundException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					
//				}
//			}).start();
		}
		return res;
	}
}
