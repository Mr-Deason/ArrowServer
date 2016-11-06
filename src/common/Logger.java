package common;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

	RandomAccessFile log = null;

	public Logger(String filename) throws IOException {
		log = new RandomAccessFile(filename, "rw");
		log.seek(log.length());
	}

	public void append(String msg) throws IOException {
		append(new Date(), msg);
	}
	
	public void append(Date date, String msg) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
		String str = '[' + sdf.format(date) + "] " + msg + "\r\n";
		System.out.print(str);
		//log.writeBytes(str);
	}

	public void close() throws IOException {
		log.close();
	}
}
