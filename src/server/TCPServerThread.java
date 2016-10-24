package server;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
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
			// get socket I/O stream
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			while (true) {
				
				// receive request
				String line = reader.readLine();
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
				
				// send response
				writer.write(res + "\n");
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

}
