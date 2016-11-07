package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import common.Operation;

public class AgentServerThread extends Thread {

	public String serverAdd = null;
	public int serverPort;
	private int serverIndex;
	
	public AgentClientThread fa;

	private Operation operation;

	public boolean canGo = false;

	public AgentServerThread(AgentClientThread fa, String serverAdd, int serverPort, int serverIndex,
			Operation operation) {
		super();
		this.fa = fa;
		this.serverAdd = serverAdd;
		this.serverPort = serverPort;
		this.serverIndex = serverIndex;
		this.operation = operation;
		start();
	}

	@Override
	public void run() {
		try {
			Socket socket = new Socket(serverAdd, serverPort);

			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			writer.write(operation.toString() + '\n');
			writer.flush();
			
			String res = reader.readLine();
			if (operation.isGet()) {
				fa.response(res);
				ack();
//				System.out.println(serverAdd + " ACK");
			} else {

				if (res.equals("0")) {
					ack();
//					System.out.println(serverAdd + " ACK");
					synchronized (this) {
						while (!canGo) {
							wait();
						}
					}

					writer.write("GO\n");
					writer.flush();
//					System.out.println(serverAdd + " GO");

					res = reader.readLine();

//					System.out.println(serverAdd + " response: " + res);
					fa.response(res);
					ack();
//					System.out.println(serverAdd + " ACK");
				}
			}
			writer.write("quit\n");
			writer.flush();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	public synchronized void go() {
//		canGo = true;
//		System.out.println("GO");
//	}
	
	public void ack() {
		synchronized (fa) {
			--fa.ackCnt;
			fa.notifyAll();
		}
	}
	
}
