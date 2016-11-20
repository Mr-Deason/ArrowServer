package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;

import common.Logger;

public class TCPServer extends Thread {

	private static int port;
	private static HashMap<String, String> map = null;
	
	private Logger logger = null;
	
	public TCPServer(int port, HashMap<String, String> map, Logger logger) {
		this.port = port;
		this.map = map;
		this.logger = logger;
		
		this.start();
	}
	@Override
	public void run() {
		try {
			ServerSocket server = null;
			logger.append(new Date(), "[INFO] starting TCP server at port " + port + "...");
			System.out.println("starting TCP server at port " + port + "...");
			
			//  create TCP server socket
			server = new ServerSocket(port);
			logger.append(new Date(), "[INFO] TCP server started, waiting for client...");
			System.out.println("TCP server started, waiting for client...");
			Socket socket = null;

			while (true) {
				try {
					socket = server.accept();
					logger.append(new Date(), "[INFO] client <" + socket.getInetAddress() + "> connected...");
					System.out.println("client <" + socket.getInetAddress() + "> connected...");
					
					// create new thread for every connection
					new TCPServerThread(socket, map, logger);
				} catch (Exception e) {
					logger.append("[ERROR] Exception: " + e.getMessage());
				} 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
