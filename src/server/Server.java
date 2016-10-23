package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import common.Logger;

public class Server {

	private int port = 18409;
	private int rpcPort = 18410;
	private HashMap<String, String> map = null;

	private Logger logger = null;
	
	public static void main(String[] args) throws IOException {

		int port = 18409;
		int rpcPort = 18410;

		if (args.length != 0 && args.length != 1) {
			System.out.println("client can only accept 0 or 1 arguments !");
			System.exit(-1);
		}
		if (args.length == 1) {
			try {
				port = Integer.parseInt(args[0]);
				rpcPort = port + 1;
			} catch (NumberFormatException e) {
				System.out.println("arguments format error !");
				System.exit(-1);
			}
		}
		
		Server server = new Server(port, rpcPort);
		server.begin();
	}

	public Server(int port, int rpcPort) {
		this.port = port;
		this.rpcPort = rpcPort;
		
		map = new HashMap<String, String>();
		try {
			logger = new Logger("./server.log");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void begin() {
		
		new TCPServer(port, map, logger);
		new UDPServer(port, map, logger);
		new RPCServer(rpcPort, map, logger);
	}

}
