package hu.uniobuda.nik.btdemo;

import java.io.IOException;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;

/**
 * @author Sicz-MesziĂˇr JĂˇnos
 * 
 *	Ĺ�sosztĂˇly mindkĂ©t fĂ©l ugyanazon kezelĂ©se miatt. 
 *	KimenĹ‘/bejĂ¶vĹ‘ ĂĽzenetek --> Output/Input stream
 *  Ugyanolyan tĂ­pusĂş BluetoothSocket-bĹ‘l
 *  Csak az egyik oldalon Server-bĹ‘l lesz socket, a mĂˇsikon Client-bĹ‘l
 *  Thread elĹ‘nyĂ©t itt nem hasznĂˇljuk ki, majd az ebbĹ‘l leszĂˇrmazott szerver v. kliens fogja
 */
public class BTComm extends Thread {

	// Ez egyik oldalon a szerver lesz a mĂˇsikon a kliens
	BluetoothSocket socket;
	MessageReceiver onMessageReceiverListener;	
	boolean run = false;
	
	// KĂĽldĂ©s ----------------------------
	protected void sendMessage(String message){
		if(socket != null){
			try {
				OutputStream os = socket.getOutputStream();
				os.write(message.getBytes());
				os.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// FogadĂˇs ----------------------------
	protected void receiveMessage(){
		byte[] buffer = new byte[1024];
		int len;
		
		while(run){
			if(socket != null){
				try {
					// Kiolvas (mivel chat-rĹ‘lvan szĂł, kĂ©nyelmesen feltĂ©telezzĂĽk nem hosszabb 1024)					
					len =  socket.getInputStream().read(buffer);
					String message = new String(buffer, 0, len);
					// Ă‰rtesĂ­tĂ©s
					if(onMessageReceiverListener != null)
						onMessageReceiverListener.onMessageReceived(message);
				} catch (IOException e) {
					e.printStackTrace();
					closeSocket();
				}
				
			}
		}
	}
	
	public void closeSocket(){
		this.run = false;
		if(socket != null){
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// Setter & Getters ----------------------------	
	public void setOnMessageReceiverListener(MessageReceiver onMessageReceiverListener) {
		this.onMessageReceiverListener = onMessageReceiverListener;
	}
	
	// ---------------------------------------------
	// InterfĂ©sz a beĂ©rkezĹ‘ ĂĽzenet Ă©rtesĂ­tĂ©sĂ©hez
	interface MessageReceiver{
		void onMessageReceived(String message);
	}
	
	
	
}
