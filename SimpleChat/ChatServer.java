import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;

public class ChatServer {
	
	/* массив клиентов подключенных к серверу */
	ArrayList<PrintWriter> clientOutputStreams;
	
	public static void main(String[] args) {
		ChatServer server = new ChatServer();
		server.go();
	}
	
	public void go() {
		clientOutputStreams = new ArrayList<PrintWriter>();
		
		try {
			ServerSocket serverSocket = new ServerSocket(5000);
			
			while(true) {
				/* создаётся сокет при подключении клиента */
				Socket clientSocket = serverSocket.accept();
				/* мы записываем данные полученные из сокета клиента и сохраняем их */
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
				/* добавляем эти данные в массив */
				clientOutputStreams.add(writer);
				
				/* создаём новый поток, который будет обрабатывать сообщения клиента, а затем рассылать их всем остальным подключенным клиентам */
				Thread thread = new Thread(new ClientHandler(clientSocket));
				thread.start();
				System.out.println("got a connection!");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}  
	}
	
	public class ClientHandler implements Runnable {
		BufferedReader reader;
		Socket sock;
		InetAddress ip;
		
		/* конструктор в качестве параметра получает сокет клиента, затем переписывает информацию из этого сокета в другой сокет и рассылает всем клиентам */
		public ClientHandler(Socket clientSocket) {
			try {
				sock = clientSocket;
				ip = sock.getInetAddress();
				InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(isReader);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
		public void run() {
			String message;
			try {
				while((message = reader.readLine()) != null) {
					System.out.println("read " + message);
					tellEveryone(message,ip);
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public void tellEveryone(String message, InetAddress ip) {
		Iterator iterator = clientOutputStreams.iterator();
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("E yyyy.MM.dd hh.mm.ss");
		System.out.println(iterator);
		while(iterator.hasNext()) {
			try {
				PrintWriter writer = (PrintWriter) iterator.next();
				writer.println(ip + "    " + dateFormat.format(date));
				writer.println(message);
				writer.println("---------------------");
				writer.flush();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
}