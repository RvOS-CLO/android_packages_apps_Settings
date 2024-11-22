/*
 * Copyright 2024 RvOS
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

package com.android.settings.deviceinfo.firmwareversion;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;

import androidx.preference.Preference;

import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;

public class RvOSMaintainerPreferenceController extends BasePreferenceController {

    private static final String RVOS_MAINTAINER = "ro.rvos.maintainer";
    private static final String RVOS_MAINTAINER_LINK = "ro.rvos.maintainer.link";
    private static final String TAG = "RvOSMaintainerPrefCtrl";

    private final Context mContext;
    private final PackageManager mPackageManager;

    public RvOSMaintainerPreferenceController(Context context, String key) {
        super(context, key);
        mContext = context;
        mPackageManager = context.getPackageManager();
    }

    @Override
    public int getAvailabilityStatus() {
        return AVAILABLE_UNSEARCHABLE;
    }

    @Override
    public CharSequence getSummary() {
        String rvosMaintainer = SystemProperties.get(RVOS_MAINTAINER,
            mContext.getResources().getString(R.string.device_info_default));
        return rvosMaintainer;
    }

    @Override
    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!TextUtils.equals(preference.getKey(), getPreferenceKey())) {
            return false;
        }

        String maintainerLink = SystemProperties.get(RVOS_MAINTAINER_LINK, null);
        if (TextUtils.isEmpty(maintainerLink)) {
            Log.w(TAG, "Maintainer link is empty or undefined");
            return true;
        }

        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(android.net.Uri.parse(maintainerLink));

        if (mPackageManager.queryIntentActivities(intent, 0).isEmpty()) {
            Log.w(TAG, "queryIntentActivities() returns empty");
            return true;
        }

        mContext.startActivity(intent);
        return true;
    }
}
