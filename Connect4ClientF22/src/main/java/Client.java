// --------------------------------------------------

/*
 * Author: Zaid Awaidah
 * 		   Alex Castillo
 * 
 * UIC, Fall 2022
 * CS 342
 *
 * Project 3 - Client
 * 
*/

// --------------------------------------------------

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

//--------------------------------------------------

public class Client extends Thread {
	// -------------------------

	/* Locals */
	private Socket socketClient;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Consumer<Serializable> callback;

	public String IPAddress = "127.0.0.1";
	public int port = 5555;
	public CFourInfo data;
	public int player;

	// -------------------------

	/* Constructor */
	Client(String IPAddress, int port, Consumer<Serializable> call) {
		this.IPAddress = IPAddress;
		this.port = port;
		callback = call;
	}

	// -------------------------

	/* Client run */
	public void run() {
		try {
			socketClient = new Socket(IPAddress, port);
			out = new ObjectOutputStream(socketClient.getOutputStream());
			in = new ObjectInputStream(socketClient.getInputStream());
			socketClient.setTcpNoDelay(true);

		} catch (Exception e) {
			e.printStackTrace();
		}

		while (true) {
			try {
				CFourInfo data_new = (CFourInfo) in.readObject();

				// update stored client data
				this.data = data_new;

				callback.accept(data_new);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	// -------------------------

	/*
	 * send
	 * 
	 * sends updates to the server
	 */
	public void send(CFourInfo data) {

		try {
			out.writeObject(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// -------------------------
}

//--------------------------------------------------
