package hu.uniobuda.nik.btdemo;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

/**
 * @author Rendszergazda Kiens is különszálon végzi a csatlakozás
 *         folyamatát, mert ez időbe telhet Lassú, rossz a "rálátás",
 *         stb...
 */
public class BTClient extends BTComm {

	public BTClient(BluetoothDevice device, UUID uuid) {
		try {
			this.socket = device.createRfcommSocketToServiceRecord(uuid);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			socket.connect();
			Log.d("bt", "Kliens kapcsolódott a távoli szerverhez.");
			run = true;
			super.receiveMessage(); // megvan az aktív socket, kezdünk vele
									// valamit (kliens oldal)
		} catch (IOException e) {
			e.printStackTrace();
			closeSocket();
		}
		super.run();
	}

}
