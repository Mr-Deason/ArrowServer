package server;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import common.Logger;

public class Server {

	private int port = 18409;
	private int rpcPort = 18410;
	private HashMap<String, String> map = null;
	private ArrayList<ServerEntity> servers = null;

	private Logger logger = null;
	
	public static void main(String[] args) throws IOException {

		//default TCP and UDP port is 18409
		//default RPC port is 18410
		int port = 18409;
		int rpcPort = 18410;
		
		//verify arguments
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
		
		servers = new ArrayList<ServerEntity>();
		File hosts = new File("./hosts.txt");
		try {
			Scanner scanner = new Scanner(hosts);
			while(scanner.hasNextLine()) {
				String[] ss = scanner.nextLine().split(" ");
				servers.add(new ServerEntity(ss[0], Integer.parseInt(ss[1])));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		new RPCServer(rpcPort, servers, logger);
		new Proposer(rpcPort+1, logger);
		new Acceptor(rpcPort+2, logger);
		new Learner(rpcPort+3, map, logger);
	}

}
