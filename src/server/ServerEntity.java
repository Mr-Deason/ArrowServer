package server;

public class ServerEntity {

	private String address;
	private int port;
	
	public ServerEntity(String address, int port) {
		super();
		this.address = address;
		this.port = port;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
}