package com.rudraksha.supershare.core.data.manager

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.WpsInfo
import android.net.wifi.p2p.*
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

private const val TAG = "WiFiDirectManager"

class WiFiDirectManager(private val context: Context) {

    private val manager: WifiP2pManager =
        context.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager

    private val channel: WifiP2pManager.Channel =
        manager.initialize(context, Looper.getMainLooper(), null)

    // -- Peers as a SharedFlow --
    private val _peers = MutableSharedFlow<List<WifiP2pDevice>>(replay = 1)
    val peers: SharedFlow<List<WifiP2pDevice>> = _peers.asSharedFlow()

    // -- PeerListListener --
    private val peerListListener = WifiP2pManager.PeerListListener { peerList ->
        val devices = peerList.deviceList.toList()
        _peers.tryEmit(devices)
        Log.d(TAG, "Peers found: ${devices.size}")
    }

    private var broadcastReceiver: BroadcastReceiver? = null

    // -- Public API --

    /**
     * Starts peer discovery.
     *
     * Caller must hold ACCESS_FINE_LOCATION & NEARBY_WIFI_DEVICES.
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @RequiresPermission(allOf = [
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.NEARBY_WIFI_DEVICES
    ])
    fun discoverPeers() {
        if (!context.hasWifiDirectPermissions()) {
            Log.e(TAG, "discoverPeers: missing required permissions")
            return
        }
        manager.discoverPeers(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() { Log.d(TAG, "Peer discovery started") }
            override fun onFailure(reason: Int) { Log.e(TAG, "Peer discovery failed: reason=$reason") }
        })
    }

    /**
     * Requests the current peer list.
     *
     * Caller must hold NEARBY_WIFI_DEVICES.
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @RequiresPermission(Manifest.permission.NEARBY_WIFI_DEVICES)
    fun requestPeers() {
        if (!context.hasWifiDirectPermissions()) return
//        manager.requestPeers(channel, peerListListener)
    }

    /**
     * Connects to a given peer.
     *
     * Caller must hold ACCESS_FINE_LOCATION & NEARBY_WIFI_DEVICES.
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @RequiresPermission(allOf = [
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.NEARBY_WIFI_DEVICES
    ])
    fun connectToPeer(device: WifiP2pDevice) {
        if (!context.hasWifiDirectPermissions()) {
            Log.e(TAG, "connectToPeer: missing required permissions")
            return
        }
        val config = WifiP2pConfig().apply {
            deviceAddress = device.deviceAddress
            wps.setup = WpsInfo.PBC
        }
        manager.connect(channel, config, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                Log.d(TAG, "Connection initiated to ${device.deviceName}")
            }
            override fun onFailure(reason: Int) {
                Log.e(TAG, "Connection to ${device.deviceName} failed: reason=$reason")
            }
        })
    }

    /**
     * Registers the internal BroadcastReceiver to listen for Wi-Fi Direct events.
     * Call this in your Activity/Service onStart or onResume.
     */
    fun registerReceiver(receiver: BroadcastReceiver) {
        broadcastReceiver = receiver
        IntentFilter().apply {
            addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
        }.also { filter ->
            ContextCompat.registerReceiver(
                context,
                receiver,
                filter,
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
        }
        Log.d(TAG, "Wi-Fi Direct receiver registered")
    }

    /**
     * Unregisters the internal BroadcastReceiver.
     * Call this in your Activity/Service onStop or onPause.
     */
    fun unregisterReceiver() {
        broadcastReceiver?.let {
            try {
                context.unregisterReceiver(it)
                Log.d(TAG, "Wi-Fi Direct receiver unregistered")
            } catch (ise: IllegalArgumentException) {
                Log.w(TAG, "Receiver not registered or already unregistered")
            }
        }
        broadcastReceiver = null
    }
}

/**
 * Extension to check both LOCATION & NEARBY_WIFI_DEVICES permissions.
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun Context.hasWifiDirectPermissions(): Boolean {
    val fineLocation = ContextCompat.checkSelfPermission(
        this, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val nearbyWifi = ContextCompat.checkSelfPermission(
        this, Manifest.permission.NEARBY_WIFI_DEVICES
    ) == PackageManager.PERMISSION_GRANTED

    return fineLocation && nearbyWifi
}
