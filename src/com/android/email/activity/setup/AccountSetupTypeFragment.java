/*
* Copyright (C) 2014 MediaTek Inc.
* Modification based on code covered by the mentioned copyright
* and/or permission notice(s).
*/
/*
 * Copyright (C) 2014 The Android Open Source Project
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

package com.android.email.activity.setup;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.email.R;
import com.android.email.activity.UiUtilities;
import com.android.email.service.EmailServiceUtils;
import com.android.emailcommon.utility.FeatureOption;
import android.view.KeyEvent;
import android.util.Log;
import com.android.email.activity.setup.AccountSetupBasicsFragment;
import com.android.email.activity.setup.AccountSetupTypeFragment;
import android.widget.LinearLayout;
import android.view.Gravity;
import android.graphics.Color;

public class AccountSetupTypeFragment extends AccountSetupFragment
        implements View.OnClickListener {
    private int mLastId;
    public static int OnkeyDownFlag = 0;

    public interface Callback extends AccountSetupFragment.Callback {
        /**
         * called when the user has selected a protocol type for the account
         * @param protocol {@link EmailServiceUtils.EmailServiceInfo#protocol}
         */
        void onChooseProtocol(String protocol);
    }

    public static AccountSetupTypeFragment newInstance() {
        return new AccountSetupTypeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        OnkeyDownFlag = 1;
        final View view = inflateTemplatedNewView(inflater, container,
                R.layout.account_setup_type_fragment, R.string.account_setup_account_type_headline);

        final Context appContext = inflater.getContext().getApplicationContext();

        final ViewGroup parent = UiUtilities.getView(view, R.id.accountTypes);
        Button soft_btn = UiUtilities.getView(view, R.id.softkey_left);


        soft_btn.setTextSize(16);
        soft_btn.setText(R.string.soft_key_btn_previous);
        Button soft_select_btn = UiUtilities.getView(view, R.id.softkey_center);
        soft_select_btn.setText("");
        View lastView = parent.getChildAt(0);
        int i = 1;
        for (final EmailServiceUtils.EmailServiceInfo info
                : EmailServiceUtils.getServiceInfoList(appContext)) {
            if (EmailServiceUtils.isServiceAvailable(appContext, info.protocol)) {
                // Don't show types with "hide" set
                if (info.hide) {
                    continue;
                }

                /* M: Dont add Exchange in the list when it is not enabled ****/
                if(!FeatureOption.MTK_EXCHNAGE_SUPPORT &&
                    ("com.android.exchange".equals(info.accountType) || "eas".equals(info.protocol)))
                        {
                            continue;
                        }

                inflater.inflate(R.layout.account_type, parent);
                final Button button = (Button)parent.getChildAt(i);
                if (parent instanceof RelativeLayout) {
                    final RelativeLayout.LayoutParams params =
                            (RelativeLayout.LayoutParams)button.getLayoutParams();
                    params.addRule(RelativeLayout.BELOW, lastView.getId());
                }
                button.setId(i);
                button.setTag(info.protocol);
                button.setText(info.name);
                button.setGravity(Gravity.CENTER_VERTICAL);
                //button.setBackgroundColor(Color.parseColor("#FFFFFF"));
                button.setOnClickListener(this);
                lastView = button;
                i++;
                button.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                  @Override
                  public void onFocusChange(View v, boolean hasFocus) {
                     if (hasFocus == true) {
                        Log.i("lhz0217","11111111");
                        button.setBackgroundColor(Color.parseColor("#FF9D3C"));
                        button.setTextColor(Color.parseColor("#e5e5e5"));
                     } else {
                        button.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        button.setTextColor(Color.parseColor("#000000"));
                  }
                }
              });
                Log.i("lhz0217","i:"+i);
                if(i == 2){
                    button.setFocusable(true);
                    button.requestFocus();
                    button.setBackgroundColor(Color.parseColor("#FF9D3C"));
                    button.setTextColor(Color.parseColor("#e5e5e5"));
                }else{
                    button.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    button.setTextColor(Color.parseColor("#000000"));
                }
            }
        }
        mLastId = i - 1;

        //setNextButtonVisibility(View.INVISIBLE);

        return view;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        final Callback callback = (Callback) getActivity();
        String protocol = "pop3";
        switch(keyCode){
            case KeyEvent.KEYCODE_SOFT_RIGHT:
                 callback.onChooseProtocol(protocol);
                 return false;
            case KeyEvent.KEYCODE_SOFT_LEFT:
                 callback.onBackPressed();
                 //callback.onChooseProtocol(protocol);
                 return false;
        }
        return false;
    }



    @Override
    public void onClick(View v) {
        final int viewId = v.getId();
        if (viewId <= mLastId) {
            final String protocol = (String) v.getTag();
            final Callback callback = (Callback) getActivity();
            Log.i("lhz1202:","protocol:"+protocol);
            callback.onChooseProtocol(protocol);
        } else {
            super.onClick(v);
        }
    }
}
