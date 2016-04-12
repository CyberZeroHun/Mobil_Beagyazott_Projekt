package hu.uniobuda.nik.btdemo;

import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	// Konstansok ---------------------
	public final static String BT_NAME = "bt_name";
	public final static UUID BT_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");

	// UI -----------------------------
	Button checkAdapter, discoverable, scan, serverStart, connect, send, stop;
	Spinner devices;
	EditText message;
	TextView chat;

	// Bluetooth handle ---------------
	BluetoothAdapter btAdapter;
	BTDeviceAdapter devicesAdapter; // Spinner-hez
	BTServer btServer;
	BTClient btClient;
	BTComm sender; // Amikor kĂĽldĂ¶k ĂĽzit, szerver vagyok vagy kliens? (ugye
					// mindkettĹ‘ lehet, itt az Ĺ‘s Ăşjabb elĹ‘nye)

	// Feedback
	Toast feedback;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// UI access --------------------
		checkAdapter = (Button) findViewById(R.id.checkAdapter);
		discoverable = (Button) findViewById(R.id.discoverable);
		scan = (Button) findViewById(R.id.scan);
		serverStart = (Button) findViewById(R.id.serverStart);
		connect = (Button) findViewById(R.id.connect);
		send = (Button) findViewById(R.id.send);
		stop = (Button) findViewById(R.id.buttonStop);
		devices = (Spinner) findViewById(R.id.devices);
		message = (EditText) findViewById(R.id.message);
		chat = (TextView) findViewById(R.id.chat);

		// Adapter access ----------------
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		devicesAdapter = new BTDeviceAdapter();
		devices.setAdapter(devicesAdapter);

		// Feedback
		feedback = Toast.makeText(this, "", Toast.LENGTH_SHORT);

		// Events ------------------------
		// ElĂ©rhetĹ‘-e az adatper
		checkAdapter.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				isBTAvailable();
			}
		});

		// FelderĂ­thetĹ‘vĂ© tesz
		discoverable.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (isBTAvailable()) {

					Intent i = new Intent(
							BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
					startActivity(i);
				}
			}
		});

		// FelderĂ­tĂ©s, szkennelĂ©s
		scan.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				IntentFilter filter = new IntentFilter(
						BluetoothDevice.ACTION_FOUND);
				registerReceiver(btReceiver, filter); // onDestroy-ban
														// leiratkozik
				btAdapter.startDiscovery();
			}
		});

		// Szerver indĂ­tĂˇsa (szerver oldal)
		serverStart.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				btServer = new BTServer(btAdapter, BT_NAME, BT_UUID);
				btServer.setOnMessageReceiverListener(new BTComm.MessageReceiver() {
					public void onMessageReceived(String message) {
						addChat(message, false);
					}
				});
				btServer.start();
				sender = btServer;
			}
		});

		// CsatlakozĂˇs (Kliens oldal)
		connect.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// KivĂˇlasztott eszkĂ¶z
				BluetoothDevice device = (BluetoothDevice) devices
						.getSelectedItem();
				if (device != null) {
					btAdapter.cancelDiscovery();
					btClient = new BTClient(device, BT_UUID);
					btClient.setOnMessageReceiverListener(new BTComm.MessageReceiver() {
						public void onMessageReceived(String message) {
							addChat(message, false);
						}
					});
					btClient.start();
					sender = btClient;
				}
			}
		});

		// Ăśzenet kĂĽldĂ©se, az Ĺ�S miatt mindegy, hogy szerver vagyok vagy
		// kliens
		send.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String sendMessage = message.getText().toString().trim();
				if (sender != null) {
					if (sendMessage.length() > 0) {
//						sender.sendMessage(sendMessage);
						if(btServer==null)
							btClient.sendMessage(sendMessage);
						else
							btServer.sendMessage(sendMessage);
						addChat(sendMessage, true);
						message.setText("");
					}
				}
			}
		});

		stop.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (btServer != null) {
					btServer.closeSocket();
				}
				if (btClient != null) {
					btClient.closeSocket();
				}
				if (btAdapter.isEnabled()) {
					btAdapter.disable();
				}
				if (sender != null)
					sender.closeSocket();
			}
		});
		if (btAdapter.isEnabled())
			btAdapter.disable();
		Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		startActivity(i);
	}

	@Override
	protected void onDestroy() {
		try {
			unregisterReceiver(btReceiver);
		} catch (IllegalArgumentException e) {
		}

		if (sender != null)
			sender.closeSocket();
		if (btClient != null) {
			btClient.closeSocket();
		}
		if (btServer != null) {
			btServer.closeSocket();
		}
		super.onDestroy();
	}

	private boolean isBTAvailable() {
		if (btAdapter == null) {
			// Bluetooth nem tĂˇmogatott
			feedback.setText("Nem tamogatott :-(");
			feedback.show();
			return false;
		} else if (!btAdapter.isEnabled()) {
			// BT nincs engedĂ©lyezve, kĂĽldĂĽnk egy kĂ©rĂ©st
			Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivity(i);
			return false;
		} else {
			return true;
		}
	}

	private void addChat(final String message, final boolean me) {
		runOnUiThread(new Runnable() {
			public void run() {
				chat.append("\n");
				if (me)
					chat.append(message);
				else
					chat.append(Html.fromHtml("<b>" + message + "</b>"));
			}
		});
	}

	private BroadcastReceiver btReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND)) {
				// TalĂˇlat
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				feedback.setText("Talalat: " + device.getName() + " ("
						+ device.getAddress() + ")");
				feedback.show();
				// ListĂˇhoz adĂˇs
				devicesAdapter.addDevice(device);
			}
		}
	};

	// -----------------------------------------------------------
	// Adapter a Bluetooth eszkĂ¶zĂ¶k megjelenĂ­tĂ©sĂ©hez a Spinnerben
	private class BTDeviceAdapter extends BaseAdapter {

		ArrayList<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();

		private void addDevice(BluetoothDevice device) {
			if (!this.devices.contains(device)) {
				this.devices.add(device);
				notifyDataSetChanged();
			}
		}

		public int getCount() {
			return devices.size();
		}

		public Object getItem(int position) {
			return devices.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null
					|| convertView.getTag() != BTDeviceAdapter.class) {
				convertView = View.inflate(parent.getContext(),
						R.layout.device_list_item, null);
				convertView.setTag(BTDeviceAdapter.class);
			}
			BluetoothDevice device = devices.get(position);
			TextView name = (TextView) convertView.findViewById(R.id.name);
			name.setText(device.getName());
			TextView address = (TextView) convertView
					.findViewById(R.id.address);
			address.setText(device.getAddress());
			return convertView;
		}

	}

}