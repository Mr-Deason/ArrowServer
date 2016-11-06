package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


import common.Logger;

public class AgentServer {

	private int port = 18409;

	private Logger logger = null;

	private ArrayList<ServerEntity> servers = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		AgentServer agent = new AgentServer();
		agent.start();
	}

	public AgentServer() {

		try {
			logger = new Logger("./agent.log");
		} catch (IOException e) {
			e.printStackTrace();
		}
		servers = new ArrayList<ServerEntity>();
	}

	public void start() {

		ServerSocket server = null;
		try {
			server = new ServerSocket(port);
			logger.append(new Date(), "[INFO] Server agent started, waiting...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Waiting...");
		Socket socket = null;

		while (true) {
			try {
				socket = server.accept();
				System.out.println("<" + socket.getInetAddress() + "> connected...");
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				String str = reader.readLine();
				String[] res = str.split(" ");
				if (res[0].equals("register")) {
					servers.add(new ServerEntity(socket.getInetAddress().getHostAddress(), Integer.parseInt(res[1])));
					System.out.println("Server <" + socket.getInetAddress() + "> connected...");
					writer.write("0\n");
					writer.flush();
					socket.close();
				} else {
					new AgentClientThread(socket, logger, servers);
				}
			} catch (Exception e) {
				try {
					logger.append("[ERROR] Exception: " + e.getMessage());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
}
