/*******************************************************************************
 *      Copyright (C) 2014 Google Inc.
 *      Licensed to The Android Open Source Project.
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 *******************************************************************************/

package com.android.mail.ui.settings;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.text.TextUtils;

import com.android.mail.R;
import com.android.mail.providers.Account;
import com.android.mail.providers.MailAppProvider;
import com.android.mail.providers.UIProvider;
import com.android.mail.providers.UIProvider.AccountCapabilities;
import com.google.common.annotations.VisibleForTesting;

import java.lang.ref.WeakReference;
import java.util.List;
import android.view.LayoutInflater;
import android.view.View;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MailPreferenceActivity extends PreferenceActivity {

    public static final String PREFERENCE_FRAGMENT_ID = "preference_fragment_id";

    private static final int ACCOUNT_LOADER_ID = 0;

    private WeakReference<GeneralPrefsFragment> mGeneralPrefsFragmentRef;

    private Cursor mAccountsCursor;
    protected List<Header> mCopyHeaders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
       LayoutInflater inflater = getLayoutInflater();
       View view = inflater.inflate(R.layout.set_softkey_info, null);
       setListFooter(view);
       */
        final ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            // Hide the app icon.
            actionBar.setIcon(android.R.color.transparent);
            actionBar.setDisplayUseLogoEnabled(false);
        }
        if (mCopyHeaders != null && mCopyHeaders.size() > 0) {
            setListAdapter(new HeaderAdapter(this, mCopyHeaders));
        }
        getLoaderManager().initLoader(ACCOUNT_LOADER_ID, null, new AccountLoaderCallbacks());
    }

    private class AccountLoaderCallbacks implements LoaderCallbacks<Cursor> {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(MailPreferenceActivity.this,
                    MailAppProvider.getAccountsUri(), UIProvider.ACCOUNTS_PROJECTION,
                    null, null, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mAccountsCursor = data;
            invalidateHeaders();
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mAccountsCursor = null;
            invalidateHeaders();
        }
    }

    @VisibleForTesting
    GeneralPrefsFragment getGeneralPrefsFragment() {
        return mGeneralPrefsFragmentRef != null ? mGeneralPrefsFragmentRef.get() : null;
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof GeneralPrefsFragment) {
            mGeneralPrefsFragmentRef =
                    new WeakReference<GeneralPrefsFragment>((GeneralPrefsFragment) fragment);
        }
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        // TODO: STOPSHIP fix Email to use the PublicPreferenceActivity trampoline
        return true;
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
        if (mAccountsCursor != null && mAccountsCursor.moveToFirst()) {
            do {
                final Account account = Account.builder().buildFrom(mAccountsCursor);
                // TODO: This will no longer be needed when the Combined view is moved to Unified
                if (!account.supportsCapability(AccountCapabilities.VIRTUAL_ACCOUNT)) {
                    final Header header = new Header();
                    if (TextUtils.isEmpty(account.getDisplayName()) ||
                            TextUtils.equals(account.getDisplayName(), account.getEmailAddress())) {
                        // No (useful) display name, just use the email address
                        header.title = account.getEmailAddress();
                    } else {
                        header.title = account.getDisplayName();
                        header.summary = account.getEmailAddress();
                    }
                    header.fragment = account.settingsFragmentClass;
                    final Bundle accountBundle = new Bundle(1);
                    accountBundle.putString(MailAccountPrefsFragment.ARG_ACCOUNT_EMAIL,
                            account.getEmailAddress());
                    header.fragmentArguments = accountBundle;

                    target.add(header);
                }
            } while (mAccountsCursor.moveToNext());
        }
        onBuildExtraHeaders(target);
        mCopyHeaders = target;
    }

    /**
     * Override this in a subclass to add extra headers besides "General Settings" and accounts
     * @param target List of headers to mutate
     */
    public void onBuildExtraHeaders(List<Header> target) {
    }
    private static class HeaderAdapter extends ArrayAdapter<Header> {

        private static class HeaderViewHolder {
            ImageView icon;
            TextView title;
            TextView summary;
        }

        private LayoutInflater mInflater;

        public HeaderAdapter(Context context, List<Header> objects) {
            super(context, 0, objects);
            mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HeaderViewHolder holder;
            View view;

            if (convertView == null) {
                view = mInflater.inflate(R.layout.custom_preference_header_item,
                        parent, false);
                holder = new HeaderViewHolder();
                holder.icon = (ImageView) view.findViewById(R.id.icon);
                holder.title = (TextView) view.findViewById(android.R.id.title);
                holder.summary = (TextView) view.findViewById(android.R.id.summary);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (HeaderViewHolder) view.getTag();
            }

            // All view fields must be updated every time, because the view may be recycled
            Header header = getItem(position);
            holder.icon.setImageResource(header.iconRes);
            holder.title.setText(header.getTitle(getContext().getResources()));
            CharSequence summary = header.getSummary(getContext().getResources());
            if (!TextUtils.isEmpty(summary)) {
                holder.summary.setVisibility(View.VISIBLE);
                holder.summary.setText(summary);
            } else {
                holder.summary.setVisibility(View.GONE);
            }

            return view;
        }
    }
}
