


package in.chipmonk.havellsolarapp;


import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.*;
import android.bluetooth.BluetoothGatt;
import android.widget.Button;


import android.bluetooth.*;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import android.widget.TextView;

import android.util.*;
/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
public class DeviceScanActivity extends Activity {
    private BluetoothGatt gatt;
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    private BluetoothDevice device_global;
    boolean check = false;
    private Button connect;
    private TextView DeviceTextview;
    private TextView messages;

    private EditText input;
    private Queue<BluetoothGattCharacteristic> readQueue;
    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    private android.bluetooth.le.ScanSettings settings;
    private List<android.bluetooth.le.ScanFilter> filters;
    private BluetoothGattCharacteristic tx;
    private BluetoothGattCharacteristic rx;
    private BluetoothGattCharacteristic disManuf;
    private BluetoothGattCharacteristic disModel;
    private BluetoothGattCharacteristic disHWRev;
    private BluetoothGattCharacteristic disSWRev;

    public static UUID UART_UUID = UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E");
    public static UUID TX_UUID = UUID.fromString("6E400002-B5A3-F393-E0A9-E50E24DCCA9E");
    public static UUID RX_UUID = UUID.fromString("6E400003-B5A3-F393-E0A9-E50E24DCCA9E");
    // UUID for the BTLE client characteristic which is necessary for notifications.
    public static UUID CLIENT_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActionBar().setTitle(R.string.title_devices);


        setContentView(R.layout.devicescanactivity);

        // Grab references to UI elements.

        //temporary
      //  messages = (TextView) findViewById(R.id.devicetextid);
     //   input = (EditText) findViewById(R.id.deviceeditid);
        mHandler = new Handler();
        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            //  Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        // Checks if Bluetooth is supported on the device.
        Toast.makeText(this,"device scanactivity  dsfsdf", Toast.LENGTH_SHORT).show();
        if (mBluetoothAdapter == null) {
            // Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // Toast.makeText(this,"device scanactivity", Toast.LENGTH_SHORT).show();
        settings = new android.bluetooth.le.ScanSettings.Builder()
                .setScanMode(android.bluetooth.le.ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();
        filters = new ArrayList<android.bluetooth.le.ScanFilter>();
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        //  setListAdapter(mLeDeviceListAdapter);
        scanLeDevice(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("DEBUG_STMT: Entered on resume");
        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
        // Initializes list view adapter.
        settings = new android.bluetooth.le.ScanSettings.Builder()
                .setScanMode(android.bluetooth.le.ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();
        filters = new ArrayList<android.bluetooth.le.ScanFilter>();
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        //setListAdapter(mLeDeviceListAdapter);
        Toast.makeText(this,"ScanDeviceFromOnResume", Toast.LENGTH_SHORT).show();
        scanLeDevice(true);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        Toast.makeText(this,"OnActivityResult Called", Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
        Toast.makeText(this,"OnPause Called", Toast.LENGTH_SHORT).show();
        mLeDeviceListAdapter.clear();
    }


    // Device scan callback.
    private android.bluetooth.le.ScanCallback mLeScanCallback =
            new android.bluetooth.le.ScanCallback() {


                @Override
                public void onScanResult(int callbackType, android.bluetooth.le.ScanResult result) {
                    System.out.println("scan result");
                    Log.i("callbackType", String.valueOf(callbackType));
                    Log.i("result", result.toString());

                    System.out.println("rajat entered");
                    System.out.println(result.toString());
//                    Toast.makeText(this,"sdflfjs", Toast.LENGTH_SHORT).show();



                    BluetoothDevice btDevice = result.getDevice();


                    //connectToDevice(btDevice);
                }

                @Override
                public void onBatchScanResults(List<android.bluetooth.le.ScanResult> results) {
                    for (android.bluetooth.le.ScanResult sr : results) {
                        System.out.println("ScanResult - Results:");
                        System.out.println( sr.toString());
                    }
                }
                @Override
                public void onScanFailed(int errorCode) {
                    System.out.println("Scan Failed");
                    System.out.println("Error Code: " + errorCode);
                }
            };

    private void scanLeDevice(final boolean enable) {
        check=false;
        Toast.makeText(this, "BLE SCAN", Toast.LENGTH_SHORT).show();
        System.out.println("Enter in BLE SCAN");
        int apiVersion = android.os.Build.VERSION.SDK_INT;
        if (apiVersion > android.os.Build.VERSION_CODES.KITKAT){
            android.bluetooth.le.BluetoothLeScanner scanner = mBluetoothAdapter.getBluetoothLeScanner();
            // scan for devices
            Toast.makeText(this, "Now scanning start:", Toast.LENGTH_SHORT).show();
            scanner.startScan(new android.bluetooth.le.ScanCallback() {
                @Override
                public void onScanResult(int callbackType, android.bluetooth.le.ScanResult result) {
                    // get the discovered device as you wish
                    // this will trigger each time a new device is found
                    System.out.println("Found a device: We are done");

                    BluetoothDevice device = result.getDevice();


//                    TextView t = (TextView)findViewById(R.id.devicetextid);
//                    t.setText(device.toString());

                    device_global=device;
                    check=true;
                    writeLine("Name of device "+device.getName());
                    writeLine("Id of device "+device.toString());
                    System.out.println("Name  of device "+device.getName());
                    System.out.println("Shibu Device Details:"+device.getType()+ "...Device Bluetooth Id:"+ device.toString());
                    gatt = device.connectGatt(getApplicationContext(), false, callback);



                }
            });
            Toast.makeText(this, "scanning in progress:", Toast.LENGTH_SHORT).show();
        } else {

        }



    }




    private BluetoothGattCallback callback = new BluetoothGattCallback() {
        // Called whenever the device connection state changes, i.e. from disconnected to connected.
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                writeLine("Connected!");
                // Discover services.
                if (!gatt.discoverServices()) {
                    writeLine("Failed to start discovering services!");
                }
            }
            else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                writeLine("Disconnected!");
            }
            else {
                writeLine("Connection state changed.  New state: " + newState);
            }
        }

        // Called when services have been discovered on the remote device.
        // It seems to be necessary to wait for this discovery to occur before
        // manipulating any services or characteristics.
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {


                writeLine("Service discovery completed!");
            }
            else {
                writeLine("Service discovery failed with status: " + status);
            }
            // Save reference to each characteristic.

            //writeLine(gatt.getServices().toString());
            List<BluetoothGattCharacteristic> characteristics=null;
            List<BluetoothGattService> services = gatt.getServices();
            for (BluetoothGattService service : services) {
                characteristics = service.getCharacteristics();
                System.out.println("value here service :"+service.getUuid().toString());
                writeLine("service :"+service.getUuid().toString());
                for(BluetoothGattCharacteristic character :characteristics){
                    if(character!=null){

                        gatt.setCharacteristicNotification(character,true);

                        System.out.println("property is "+character.getProperties());

                        System.out.println("value here charac : "+character.getUuid().toString());
                        writeLine("charac : "+character.getUuid().toString());}
                }

            }


//
            for (BluetoothGattService service : services) {
                characteristics = service.getCharacteristics();
                System.out.println("reading service :"+service.getUuid().toString());
                writeLine("service :"+service.getUuid().toString());
                for(BluetoothGattCharacteristic character :characteristics){
                    if(character!=null){

                        //  gatt.readCharacteristic (character);
                        //     gatt.writeCharacteristic(character);

                        // System.out.println("reading here charac : "+character.getStringValue(0));
                        writeLine("charac : "+character.getUuid().toString());}
                }

            }




            tx = gatt.getService(UUID.fromString("4914dd84-8b51-42fd-884b-06bc9e5b96af")).getCharacteristic(UUID.fromString("05daea97-20d6-46ce-8bcd-4f3e1efe2208"));



            rx = gatt.getService(UART_UUID).getCharacteristic(RX_UUID);



            // Setup notifications on RX characteristic changes (i.e. data received).
            // First call setCharacteristicNotification to enable notification.
            if (!gatt.setCharacteristicNotification(rx, true)) {
                writeLine("Couldn't set notifications for RX characteristic!");
            }
            // Next update the RX characteristic's client descriptor to enable notifications.
            if (rx.getDescriptor(CLIENT_UUID) != null) {

                BluetoothGattDescriptor desc = rx.getDescriptor(CLIENT_UUID);
                desc.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                if (!gatt.writeDescriptor(desc)) {
                    writeLine("Couldn't write RX client descriptor value!");
                }
            }
            else {
                writeLine("Couldn't get RX client descriptor!");
            }
        }

        // Called when a remote characteristic changes (like the RX characteristic).
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            writeLine("Received: " + characteristic.getStringValue(0));
            System.out.println("Received: " + characteristic.getStringValue(0));
        }
        @Override
        public void  onCharacteristicRead (BluetoothGatt gatt,
                                           BluetoothGattCharacteristic characteristic,
                                           int status){

            System.out.println("Read value is : " + characteristic.getStringValue(0));
            writeLine("Read value is : " + characteristic.getStringValue(0));
        }

        @Override
        public void  onCharacteristicWrite (BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic,
                                            int status){

            System.out.println("write value one is : " + characteristic.getStringValue(0));


            writeLine("write  value is : " + characteristic.getStringValue(0));
        }

    };



    public void sendClick(View view) {
        String message = input.getText().toString();
        message= "read batti:";

        System.out.println("ans is "+tx);


        if (tx == null || message == null || message.isEmpty()) {
            // Do nothing if there is no device or message to send.
            writeLine("tx is null");
            return;
        }
        // Update TX characteristic value.  Note the setValue overload that takes a byte array must be used.
        tx.setValue(message.getBytes(Charset.forName("UTF-8")));

        System.out.println("before read read message "+message);
        if (gatt.writeCharacteristic(tx)) {

            System.out.println("write read read message is called "+message);
            writeLine(message);
        }
        else {
            writeLine("Couldn't write TX characteristic!");
        }
    }

    private void writeLine(final CharSequence text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messages.append(text);
                messages.append("\n");
            }
        });
    }


    // Adapter for holding devices found through scanning.
    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflator;
        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
            mInflator = DeviceScanActivity.this.getLayoutInflater();
        }
        public void addDevice(BluetoothDevice device) {
            if(!mLeDevices.contains(device)) {
                mLeDevices.add(device);
            }
        }
        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }
        public void clear() {
            mLeDevices.clear();
        }
        @Override
        public int getCount() {
            return mLeDevices.size();
        }
        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }
        @Override
        public long getItemId(int i) {
            return i;
        }
        //
        public View getView(int i, View view, ViewGroup viewGroup) {

            return view;
        }
    }


    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }
}

