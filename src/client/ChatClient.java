package client;

import java.io.*;
import java.net.*;


public class ChatClient extends ClientBasic {
	
	PrintWriter sender;
	BufferedReader getter;
	ListenerThread listener;
	
	public ChatClient(String serverIP, int port, String username, Client parentThread) throws IOException {
		this.serverIP = serverIP;
		this.port = port;
		this.parentThread = parentThread;
		this.socket = new Socket(serverIP, port);
		this.username = username;
		sender = new PrintWriter(socket.getOutputStream(), true);
		getter = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		sender.println(username);
		listener = new ListenerThread();
		listener.start();
	}

	@Override
	public void send(String... message) {
		System.out.println("cc send!");
		sender.println(message[0]);
		sender.flush();
	}

	@Override	
	public void run() {
		try {
			while (!stopThread) {
				// ...
			}
			listener.shutdown();
			sender.println("[OFFLINE]");
			sender.close();
			getter.close();
			socket.close();
		} catch (Exception e) {
			return;
		}
	}

  
	private class ListenerThread extends Thread {
		private boolean stop = false;
		public void run() {
			while (!stop) {
				try {
					String message = getter.readLine();
					if (message == null) continue;
					// System.out.println(message);
					parentThread.receiveMessage(message);
				} catch (IOException e) {
					// e.printStackTrace();
				}
			}
		}
		public void shutdown() { stop = true; }
	}
}