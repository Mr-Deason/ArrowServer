package common;

import java.util.HashMap;

public class Operation {
	
	private String op = null;
	private String key = null;
	private String value = null;
	
	public Operation(String str) throws Exception {
		String args[] = str.split(",");
		if (args.length < 2 || args.length > 3) {
			throw new Exception("malformed request!");
		}
		op = args[0].toUpperCase().trim();
		if (args.length == 2 ) {
			if (!op.equals("GET") && !op.equals("DELETE")) {
				throw new Exception("malformed request!");
			}
			key = args[1].trim();
		} else {
			if (!op.equals("PUT")) {
				throw new Exception("malformed request!");
			}
			key = args[1].trim();
			value = args[2].trim();
		}
	}
	
	public String exec(HashMap<String, String> map) throws Exception{
		if (op.equals("GET")) {
			String res = map.get(key);
			if (res == null) {
				throw new Exception("not found!");
			}
			return "0 " + res;
		}
		if (op.equals("PUT")) {
			map.put(key, value);
			return "0";
		}
		if (op.equals("DELETE")) {
			String res = map.get(key);
			if (res == null) {
				throw new Exception("not found!");
			}
			map.remove(key);
			return "0";
		}
		return "0";
	}


	public String getOp() {
		return op;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}
	
	public boolean isGet() {
		return op.equals("GET");
	}
	public boolean isPut() {
		return op.equals("PUT");
	}
	public boolean isDelete() {
		return op.equals("DELETE");
	}
	
}
