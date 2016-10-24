package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.Date;
import java.util.HashMap;

import common.Logger;
import common.Operation;

public class UDPServer extends Thread {

	private static int port;
	private static HashMap<String, String> map = null;

	private Logger logger = null;

	public UDPServer(int port, HashMap<String, String> map, Logger logger) {
		this.port = port;
		this.map = map;
		this.logger = logger;

		this.start();
	}

	@Override
	public void run() {

		try {

			logger.append("[INFO] starting UDP server at port " + port + "...");
			System.out.println("starting UDP server at port " + port + "...");
			
			// create UDP datagram socket
			DatagramSocket socket = new DatagramSocket(port);
			logger.append("[INFO] UDP server started, waiting for client...");
			System.out.println("UDP server started, waiting for client...");
			
			byte[] receive = null;
			byte[] send = null;
			while (true) {
				receive = new byte[1024];
				DatagramPacket packet = new DatagramPacket(receive, receive.length);

				// receive datagram packet
				socket.receive(packet);
				String request = new String(packet.getData()).trim();
				String response = null;

				logger.append("[INFO] reveived datagram request \"" + request + "\" from client <" + packet.getAddress()
						+ ">");
				try {
					Operation op = new Operation(request);
					response = op.exec(map);
					logger.append("[INFO] request from <" + packet.getAddress() + "> finished");
				} catch (Exception e) {
					response = "-1 " + e.getMessage();
					logger.append("[ERROR] request from <" + packet.getAddress() + ">: " + e.getMessage());
				}
				
				// send response
				send = response.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(send, send.length, packet.getAddress(),
						packet.getPort());
				socket.send(sendPacket);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
