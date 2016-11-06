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

	private boolean canGo = false;

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
				fa.ack(serverIndex);
			} else {

				if (res.equals("0")) {
					fa.ack(serverIndex);
					while (!canGo) {

					}

					writer.write("GO\n");
					writer.flush();

					res = reader.readLine();

					fa.response(res);
					fa.ack(serverIndex);
				}
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void go() {
		canGo = true;
	}
}
