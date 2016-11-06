package common;

import java.util.HashMap;

public class Operation {

	private String op = null;
	private String key = null;
	private String value = null;
	private String cmd = null;

	private static int cnt = 0;

	public Operation(String str) throws Exception {
		String args[] = str.split(",");
		if (args.length < 2 || args.length > 3) {
			throw new Exception("malformed request!");
		}
		op = args[0].toUpperCase().trim();
		if (args.length == 2) {
			if (!op.equals("GET") && !op.equals("DELETE")) {
				throw new Exception("malformed request!");
			}
			key = args[1].trim();
			cmd = op + ',' + key;
		} else {
			if (!op.equals("PUT")) {
				throw new Exception("malformed request!");
			}
			key = args[1].trim();
			value = args[2].trim();
			cmd = op + ',' + key + ',' + value;
		}
	}

	public String exec(HashMap<String, String> map) throws Exception {
		++cnt;
		synchronized (map) {

			if (cnt == 1) {
				// Thread.sleep(10000);
			}
			if (op.equals("GET")) {
				String res = map.get(key);
				if (res == null) {
					throw new Exception("not found!");
				}
				return "0 " + res;
			}
			if (op.equals("PUT")) {
				map.put(key, value);
				return "0 put successfully";
			}
			if (op.equals("DELETE")) {
				String res = map.get(key);
				if (res == null) {
					throw new Exception("not found!");
				}
				map.remove(key);
				return "0 delete successfully";
			}
			return "0";
		}
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

	@Override
	public String toString() {
		return cmd;
	}
}
