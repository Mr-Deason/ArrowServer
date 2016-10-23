package server;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;

import common.Logger;
import common.Operation;

public class TCPServerThread extends Thread {

	Socket socket;
	HashMap<String, String> map;

	private Logger logger = null;
	
	public TCPServerThread(Socket socket, HashMap<String, String> map, Logger logger) {
		this.socket = socket;
		this.map = map;
		this.logger = logger;
		
		this.start();
	}

	@Override
	public void run() {

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			while (true) {
				String line = reader.readLine();
				System.out.println("Client: " + line);
				logger.append("[INFO] received request \"" + line + "\" from <" + socket.getInetAddress() + '>');
				String res = null;
				try {
					Operation op = new Operation(line);
					res = op.exec(map);
					logger.append("[INFO] request from <" + socket.getInetAddress() + "> finished");
				}catch (Exception e) {
					res = "-1 " + e.getMessage();
					logger.append("[ERROR] request from <" + socket.getInetAddress() + ">: " + e.getMessage());
				}
				
				writer.write(res + "\n");
				writer.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
