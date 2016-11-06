package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import com.sun.xml.internal.ws.Closeable;

import common.Logger;
import common.Operation;

public class Server {

	private int port = 18408;
	private int agentPort = 18409;
	private String agentHost = null;
	private HashMap<String, String> map = null;

	private Logger logger = null;

	public static void main(String[] args) throws IOException {

		// default TCP and UDP port is 18409
		int port = 18408;
		String agentHost = "127.0.0.1";

		// verify arguments
		if (args.length > 2) {
			System.out.println("Cannot accept more than 2 arguments !");
			System.exit(-1);
		}

		if (args.length == 1) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				agentHost = args[0];
			}
		} else if (args.length == 2) {
			try {
				agentHost = args[0];
				port = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				System.out.println("arguments format error !");
				System.exit(-1);
			}
		}

		Server server = new Server(port, agentHost);
		server.begin();
	}

	public Server(int port, String agentHost) {
		this.port = port;
		this.agentHost = agentHost;

		map = new HashMap<String, String>();

		try {
			logger = new Logger("./server.log");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void begin() {
		register();

		try {
			ServerSocket server = new ServerSocket(port);
			Socket socket = null;
			while (true) {
				socket = server.accept();

				System.out.println("<" + socket.getInetAddress() + "> connected...");
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				
				String res = null;
				while (true) {
					try {

						String str = reader.readLine();
						System.out.println("receive \"" + str + "\"");
						if (str.equals("quit")) {
							break;
						}
						Operation operation = new Operation(str);
						if (operation.isGet()) {
							res = operation.exec(map);
							writer.write(res + "\n");
							writer.flush();
						}else {
							writer.write("0\n");
							writer.flush();
							
							str = reader.readLine();
							if (str.equals("GO")) {
								res = operation.exec(map);
								writer.write(res + "\n");
								writer.flush();
							}
						}
					} catch (IOException e) {
						break;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void register() {

		Socket socket = null;
		BufferedReader reader = null;
		BufferedWriter writer = null;

		try {
			// logger.append("[INFO] TCP client started");
			// logger.append("[INFO] connect to " + hostname + ':' + port +
			// "...");

			// connect to server using TCP socket
			socket = new Socket(agentHost, agentPort);
			// logger.append("[INFO] connect successfully!");

			// get socket I/O stream
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			// receive input and send to server
			writer.write("register " + port + "\n");
			writer.flush();
			logger.append("[INFO] registering on agent server");

			// read response from server
			String res = reader.readLine();
			logger.append("[INFO] receive response \"" + res + "\" from server");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
