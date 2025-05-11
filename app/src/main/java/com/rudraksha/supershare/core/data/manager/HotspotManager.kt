package com.rudraksha.supershare.core.data.manager

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.SoftApConfiguration
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat

class HotspotManager(private val context: Context) {

    private var reservation: WifiManager.LocalOnlyHotspotReservation? = null

    /**
     * Starts a local-only Wi-Fi hotspot.
     *
     * @param onStarted Called when hotspot is successfully started with SSID and password.
     * @param onFailed Called with reason code if the hotspot fails to start.
     */
    fun startHotspot(onStarted: (String, String) -> Unit, onFailed: (Int, String) -> Unit) {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        if (!checkPermissions(onFailed)) return

        try {
            wifiManager.startLocalOnlyHotspot(object : WifiManager.LocalOnlyHotspotCallback() {
                @Suppress("DEPRECATION")
                override fun onStarted(res: WifiManager.LocalOnlyHotspotReservation) {
                    super.onStarted(res)
                    this@HotspotManager.reservation = res

                    // Pick up SSID/password in a null-safe, non-deprecated way on R+;
                    // fall back only on older.
                    val (ssid, password) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        val cfg = res.softApConfiguration
                        // `ssid` and `passphrase` are both nullable Strings → provide defaults
                        (cfg.ssid ?: "Unknown SSID") to (cfg.passphrase ?: "No Password")
                    } else {
                        val oldCfg = res.wifiConfiguration
                        (oldCfg?.SSID ?: "Unknown SSID") to (oldCfg?.preSharedKey ?: "No Password")
                    }

                    Log.d("HotspotManager", "Hotspot started: SSID=$ssid, Password=$password")
                    onStarted(ssid, password)
                }

                override fun onStopped() {
                    super.onStopped()
                    Log.d("HotspotManager", "Hotspot stopped")
                }

                override fun onFailed(reason: Int) {
                    super.onFailed(reason)
                    val reasonMsg = when (reason) {
                        ERROR_NO_CHANNEL -> "No channel available"
                        ERROR_GENERIC -> "Generic error"
                        ERROR_INCOMPATIBLE_MODE -> "Incompatible mode"
                        ERROR_TETHERING_DISALLOWED -> "Tethering disallowed"
                        else -> "Unknown error"
                    }
                    Log.e("HotspotManager", "Hotspot failed: $reasonMsg")
                    onFailed(reason, reasonMsg)
                }
            }, Handler(Looper.getMainLooper()))
        } catch (e: SecurityException) {
            onFailed(-1, "SecurityException: ${e.localizedMessage}")
            Log.e("HotspotManager", "SecurityException: ${e.localizedMessage}")
        } catch (e: Exception) {
            onFailed(-1, "Exception: ${e.localizedMessage}")
            Log.e("HotspotManager", "Exception: ${e.localizedMessage}")
        }
    }

    fun stopHotspot() {
        try {
            reservation?.close()
            reservation = null
            Log.d("HotspotManager", "Hotspot stopped successfully.")
        } catch (e: Exception) {
            Log.e("HotspotManager", "Failed to stop hotspot: ${e.localizedMessage}")
        }
    }

    private fun checkPermissions(onFailed: (Int, String) -> Unit): Boolean {
        val locationGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val nearbyGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.NEARBY_WIFI_DEVICES) == PackageManager.PERMISSION_GRANTED
        } else true

        if (!locationGranted || !nearbyGranted) {
            onFailed(-1, "Missing required permissions: ACCESS_FINE_LOCATION or NEARBY_WIFI_DEVICES")
            return false
        }
        return true
    }
}
