/*
 * Copyright (C) 2025 RvOS Android Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.settings.preferences

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.preference.PreferenceScreen
import com.android.settings.R
import com.android.settingslib.core.AbstractPreferenceController
import com.android.settingslib.widget.LayoutPreference
import com.android.settings.utils.DeviceInfoUtil

class RvosPreferenceController(context: Context) : AbstractPreferenceController(context) {

    override fun displayPreference(screen: PreferenceScreen) {
        super.displayPreference(screen)

        val bannerPreference = screen.findPreference<LayoutPreference>(KEY_ROM_BANNER)
        bannerPreference?.let {
            val buildType = getBuildType()
            val majorVersion = getMajorVersion()
            val minorVersion = getMinorVersion()
            val bannerText = "RvOS | $majorVersion $minorVersion | $buildType"

            it.findViewById<TextView>(R.id.banner_text)?.text = bannerText
        }

        val systemInfoPreference = screen.findPreference<LayoutPreference>(KEY_SYSTEM_INFO)
        systemInfoPreference?.let {
            it.findViewById<TextView>(R.id.maintainer_info)?.text = getMaintainerName()
            it.findViewById<TextView>(R.id.clo_revision)?.text = getCloRevision()
            it.findViewById<TextView>(R.id.processor_info)?.text = DeviceInfoUtil.getProcessor()
            it.findViewById<TextView>(R.id.camera_info)?.text =
                "${DeviceInfoUtil.getFrontCameraMegapixels(mContext)} / ${DeviceInfoUtil.getRearCameraMegapixels(mContext)}"
            it.findViewById<TextView>(R.id.display_info)?.text = DeviceInfoUtil.getScreenResolution(mContext)
            it.findViewById<TextView>(R.id.battery_info)?.text = DeviceInfoUtil.getBatteryCapacity(mContext)

            val ramTextView = it.findViewById<TextView>(R.id.ram_info)
            val zramBadge = it.findViewById<TextView>(R.id.zram_badge)

            val ramInfo = DeviceInfoUtil.getTotalRam()

            if (ramInfo.contains("+")) {
                val parts = ramInfo.split("+").map { it.trim() }
                val mainRam = parts[0]
                val zram = parts[1].replace("ZRAM", "").trim()

                ramTextView.text = mainRam

                zramBadge.text = "+ $zram"
                zramBadge.visibility = View.VISIBLE
            } else {
                ramTextView.text = ramInfo
                zramBadge.visibility = View.GONE
            }

        }
    }

    private fun getMajorVersion(): String {
        return android.os.SystemProperties.get("ro.rvos.version.major", "Tiramisu")
    }

    private fun getMinorVersion(): String {
        return android.os.SystemProperties.get("ro.rvos.version.minor", "Unknown")
    }

    private fun getBuildType(): String {
        return android.os.SystemProperties.get("ro.rvos.build.type", "Unofficial")
    }
    
    private fun getCloRevision(): String {
        return android.os.SystemProperties.get("ro.codelinaro.revision", "Unknown")
    }

    private fun getMaintainerName(): String {
        return android.os.SystemProperties.get("ro.rvos.maintainer", "Unknown")
            .replace("_", " ")
    }

    override fun isAvailable(): Boolean = true

    override fun getPreferenceKey(): String = KEY_ROM_BANNER

    companion object {
        private const val KEY_ROM_BANNER = "rvos_banner"
        private const val KEY_SYSTEM_INFO = "rvos_system_info"
    }
}
