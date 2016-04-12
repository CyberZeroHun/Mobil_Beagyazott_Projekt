package hu.uniobuda.nik.btdemo;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.util.Log;

/**
 * 
 * @author Rendszergazda
 *	Azért kell a háttérszál, mert a bss.accept(); blocking hole
 *  Megáll azon a ponton a program futása amíg nem érkezik kliens
 */
public class BTServer extends BTComm {

	BluetoothServerSocket bss; // Ez még kimondottan server socket
	
	public BTServer(BluetoothAdapter adapter, String name, UUID uuid) {
		try {
			this.bss = adapter.listenUsingRfcommWithServiceRecord(name, uuid);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		this.run = true;
		while(run){
			try {
				this.socket = bss.accept(); // itt lehetne timeout-ot adni is
				Log.d("bt", "Kliens megerkezett.");
				receiveMessage();	// megvan az aktív socket, kezdünk vele valamit (szerver oldal)
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		super.run();
	}
	
	// Míg klien odalról elég a socket-et zárni, addig itt a server socket-et is ajánlott
	@Override
	public void closeSocket() {
		super.closeSocket();	// ős meghívása, sima socket zárása
		try {
			bss.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
