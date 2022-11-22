import java.net.*;
import java.io.*;

public class ClientThread extends Thread {
	String key; 
	private Socket connection;
	private InputStream input; private OutputStreamWriter output;
		
	public ClientThread(Socket connection, String key) throws IOException {
		this.connection = connection;
		this.key = key;
		input = this.connection.getInputStream();
		output = new OutputStreamWriter(this.connection.getOutputStream());
	}
	
	// metodo che a partire dalla stringa di comando ricevuta dal client restituisce la stringa di risposta
	private String response(String command) {
		String component[];

		component = command.split(","); // separazione della stringa nelle tre component
		if (component.length == 1) {
			if (component[0].equalsIgnoreCase("MONTEPREMI")) {
				if (Partita.getInstance().isStarted()) {
					return Partita.getBalance().toString();
				} else {
					return "NOREADY";
				}
			}
		}
		if (component.length == 2) {
			if (component[0].equalsIgnoreCase("STOP")) {
				if(Partita.getInstance().isStarted() == true) {
					if (!component[1].equals(this.key)) {
						return "ERROR";
					}
					Partita.getInstance().stop();
					return "OK";
				}
				return "NOREADY";
			}
		}
		if (component.length == 3) {
			// determinazione dell’operazione da eseguire
			if (component[0].equalsIgnoreCase("START")) {
				System.out.println(this.key + ":" + component[1]);
				if (!component[1].equals(this.key)) {
					return "ERROR";
				}
				int balance;
				try {
					balance = Integer.parseInt(component[2]);
				} catch (NumberFormatException e) { return "ERROR"; }
				Partita.getInstance().start(balance);
				return "OK";
			}
			if (component[0].equalsIgnoreCase("PUNTATA")) {
				if(Partita.getInstance().isStarted() == true) {
					int stake, number;
					try {
						stake = Integer.parseInt(component[1]);
						number = Integer.parseInt(component[2]);
					} catch (NumberFormatException e) { return "ERROR"; }
					String result = Partita.getInstance().bet(stake, number);
					return result;
				}
				return "NOREADY";
			}
		}

		return "ERROR";
	}
	
	public void run()  {
		int n, i;
		String result; String character;
		byte [] buffer = new byte[1024];
		StringBuffer command = new StringBuffer();

		try {
			while ((n = input.read(buffer)) != -1) { // ciclo di ricezione dei dati dal client
				if (n > 0) {
					for (i=0; i<n; i++) { // ricerca dei caratteri di terminazione 
						if (buffer[i] == '\r' || buffer[i] == '\n') { // commando ricevuto -> esecuzione
							result = response(command.toString());
							output.write(result+"\r\n"); output.flush(); // invio al client del risultato
							command = new StringBuffer(); // inizializzazione del comando
							break;
						} else { character = new String(buffer, i, 1, "ISO-8859-1"); command.append(character); }
					}
				}
			}
		}
		catch (IOException exception) { }
 
		try {
			System.out.println("Connessione chiusa!");
			input.close(); output.close();
			connection.shutdownInput(); connection.shutdownOutput(); connection.close(); 
		}
		catch (IOException _exception) { }
	}
}
