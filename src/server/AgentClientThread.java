package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;


import common.Logger;
import common.Operation;

public class AgentClientThread extends Thread {

	public Socket socket;
	public Logger logger;
	private ArrayList<ServerEntity> servers = null;
	private int serverCnt = 3;
	public int ackCnt;
	
	private String resString;

	public AgentClientThread(Socket socket, Logger logger, ArrayList<ServerEntity> servers) {
		this.socket = socket;
		this.logger = logger;
		this.servers = servers;
		start();
	}

	@Override
	public void run() {

		try {
			// get socket I/O stream
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			while (true) {

				// receive request
				String line = reader.readLine();
				logger.append("[INFO] received request \"" + line + "\" from <" + socket.getInetAddress() + '>');
				try {
					Operation op = new Operation(line);
					if (op.isGet()) {
						int index = new Random().nextInt(servers.size());
						ServerEntity server = servers.get(index);

						synchronized (this) {
							ackCnt = 1;
						}
						new AgentServerThread(this, server.getAddress(), server.getPort(), index, op);
						
						System.out.println("attemp to connect server <" + server.getAddress() + ':'+ server.getPort() + ">");

						synchronized (this) {
							while (ackCnt > 0) {
								wait();
							}
						}
						System.out.println("phase 1");
						
					} else {

						synchronized (this) {
							ackCnt = servers.size();
						}
						ArrayList<AgentServerThread> serverThreads = new ArrayList<AgentServerThread>();

						for (int i = 0; i < servers.size(); ++i) {
							ServerEntity serverEntity = servers.get(i);
							serverThreads.add(new AgentServerThread(this, serverEntity.getAddress(),
									serverEntity.getPort(), i, op));
						}
						
						synchronized (this) {
							while (ackCnt > 0) {
								wait();
							}
							ackCnt = servers.size();
						}

//						while (ackCnt > 0) {
//							sleep(200);
////							System.out.println("phase 1 wait " + ackCnt);
//						}
						System.out.println("phase 1");
						
						ackCnt = servers.size();
						for (int i = 0; i < servers.size(); ++i) {
							go(serverThreads.get(i));
						}

						synchronized (this) {
							while (ackCnt > 0) {
								wait();
							}
						}
//						while (ackCnt > 0) {
//
//							sleep(200);
////							System.out.println("phase 2 wait " + ackCnt);
//						}
						System.out.println("phase 2");
					}
					logger.append("[INFO] request from <" + socket.getInetAddress() + "> finished");
				} catch (Exception e) {
					resString = "-1 " + e.getMessage();
					logger.append("[ERROR] request from <" + socket.getInetAddress() + ">: " + e.getMessage());
					e.printStackTrace();
				}

				// send response
				writer.write(resString + "\n");
				writer.flush();
			}
		} catch (SocketException e) {
			try {
				logger.append("[INFO] client <" + socket.getInetAddress() + "> exception: " + e.getMessage());
				System.out.println("client <" + socket.getInetAddress() + "> exception: " + e.getMessage());
			} catch (IOException e1) {
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void response(String res) {
		resString = res;
	}

	public void go(AgentServerThread aThread) {
		synchronized (aThread) {
			aThread.canGo = true;
			aThread.notifyAll();
		}
	}
	public void timeout() {

	}
}
