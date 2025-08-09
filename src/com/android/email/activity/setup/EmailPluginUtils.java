package com.android.email.activity.setup;

import android.content.Context;
import android.content.res.Resources;

import com.android.emailcommon.VendorPolicyLoader;
import com.android.emailcommon.VendorPolicyLoader.Provider;
import com.android.mail.utils.LogUtils;

/**
 * M: Utilities methods that support plug-in extension in email account setup.
 *
 */
public class EmailPluginUtils {
    private static final String TAG = "EmailPluginUtils";

    public static final String EXTRA_MAIL_PROVIDER_NAME = "mail_provider_name";
    public static final String EXTRA_MAIL_PROVIDER_ICON = "mail_provider_icon";
    public static final String EXTRA_MAIL_PROVIDER_DOMAIN = "mail_provider_domain";
    public static final String EXTRA_MAIL_PROVIDER_COUNT = "mail_provider_count";
    public static final String EXTRA_MAIL_PROVIDER_RES = "mail_provider_resource";
    public static final String EXTRA_MAIL_PROVIDER_HINT = "mail_provider_hint";

    /**
     * Return whether support provider list feature.
     * @param context
     * @return
     */
    public static boolean isSupportProviderList(Context context) {
        return VendorPolicyLoader.getInstance(context).isSupported();
    }

    public static String[] getProviderNames(Context context) {
        if (VendorPolicyLoader.getInstance(context).isSupported()) {
            LogUtils.d(TAG, "getProviderNames from VendorPolicy");
            return VendorPolicyLoader.getInstance(context).getProviderNames();
        }
        return null;
    }

    public static String[] getProviderDomains(Context context) {
        if (VendorPolicyLoader.getInstance(context).isSupported()) {
            LogUtils.d(TAG, "getProviderDomains from VendorPolicy");
            return VendorPolicyLoader.getInstance(context).getProviderDomains();
        }
        return null;
    }

    public static String[] getProviderHints(Context context) {
        if (VendorPolicyLoader.getInstance(context).isSupported()) {
            LogUtils.d(TAG, "getProviderHints from VendorPolicy");
            return VendorPolicyLoader.getInstance(context).getProviderEmailHints();
        }
        return null;
    }

    public static int[] getProviderIcons(Context context) {
        if (VendorPolicyLoader.getInstance(context).isSupported()) {
            LogUtils.d(TAG, "getProviderIcons from VendorPolicy");
            return VendorPolicyLoader.getInstance(context).getProviderIcons();
        }
        return null;
    }

    public static Resources getProviderResources(Context context) {
        if (VendorPolicyLoader.getInstance(context).isSupported()) {
            LogUtils.d(TAG, "getProviderResources from VendorPolicy");
            return VendorPolicyLoader.getInstance(context).getResources();
        }
        return null;
    }

    public static int getProviderCount(Context context) {
        if (VendorPolicyLoader.getInstance(context).isSupported()) {
            LogUtils.d(TAG, "getProviderCount from VendorPolicy");
            return VendorPolicyLoader.getInstance(context).getDisplayMailProviderNum();
        }
        return 0;
    }

    public static Provider findProviderForDomain(Context context, String domain) {
        return findProviderForDomain(context, domain, null);
    }

    public static Provider findProviderForDomain(Context context, String domain, String protocol) {
        if (VendorPolicyLoader.getInstance(context).isSupported()) {
            LogUtils.d(TAG, "findProviderForDomain from VendorPolicy");
            return VendorPolicyLoader.getInstance(context).findProviderForDomainProtocol(domain, protocol);
        }
        return null;
    }
}
