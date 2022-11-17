import java.net.*;
import java.io.*;

public class AzzardoServer extends Thread {
	public final static int SERVER_PORT = 6969;
	public final static String KEY = "chiavesegreta";
	private ServerSocket server;
    
	public AzzardoServer(int port) throws IOException {
		server = new ServerSocket(port);
		server.setSoTimeout(1000); // 1000ms = 1s
	}

	public void run() {
		Socket connection = null;

		while (!Thread.interrupted()) {
			try {
				connection = server.accept(); // attesa richiesta connessione da parte del client (attesa massima 1s)
				Thread client = new ClientThread(connection, KEY);
				client.start();
			}
			catch (SocketTimeoutException exception) { }
			catch (IOException exception) { }
		}
		// chiusura socket di ascolto del server 
		try {
			server.close();
		}
		catch (IOException exception) {}
	}

	public static void main(String[] args) {
		int c;
				
		try {
			AzzardoServer server;

			server = new AzzardoServer(SERVER_PORT);
			server.start();
			c = System.in.read();
			server.interrupt();
			server.join();
		}
		catch (IOException exception) {
			System.err.println("Errore!");
		} catch (InterruptedException exception) {
			System.err.println("Fine!");
		}
	}
}
