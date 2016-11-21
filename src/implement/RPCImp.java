package implement;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import common.Logger;
import common.Operation;
import interfaces.LearnerInterface;
import interfaces.ProposerInterface;
import interfaces.RPCInterf;
import server.Paxos;
import server.Server;
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
//		try {
//			System.out.println("receive request from <" + getClientHost() + ">: " + op);
//		} catch (ServerNotActiveException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		paxos.begin(op);
		paxos.prepare();
		paxos.accept();
		return paxos.commit();
	}
}
