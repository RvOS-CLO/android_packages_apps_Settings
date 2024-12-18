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
import android.os.SystemProperties;
import android.text.TextUtils;

import androidx.preference.Preference;

import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;

public class CodeLinaroRevisionPreferenceController extends BasePreferenceController {

    private static final String CODELINARO_REVISION_PROP = "ro.codelinaro.revision";
    private static final String CODELINARO_REVISION_LINK_PROP = "ro.codelinaro.revision.link";

    private final Context mContext;

    public CodeLinaroRevisionPreferenceController(Context context, String key) {
        super(context, key);
        mContext = context;
    }

    @Override
    public int getAvailabilityStatus() {
        return AVAILABLE_UNSEARCHABLE;
    }

    @Override
    public CharSequence getSummary() {
		return SystemProperties.get(CODELINARO_REVISION_PROP);
    }

    @Override
    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!TextUtils.equals(preference.getKey(), getPreferenceKey())) {
            return false;
        }

        String codelinaroRevisionLink = SystemProperties.get(CODELINARO_REVISION_LINK_PROP);

        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(android.net.Uri.parse(codelinaroRevisionLink));

        mContext.startActivity(intent);
        return true;
    }
}
