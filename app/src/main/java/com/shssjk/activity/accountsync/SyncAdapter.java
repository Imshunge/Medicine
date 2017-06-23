package com.shssjk.activity.accountsync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

/**
 * Created by heyangya on 17-1-10.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }
    @Override
    public void onPerformSync(Account account, Bundle extras,
                              String authority, ContentProviderClient provider,
                              SyncResult syncResult) {
//        Log.i("account sync", "-----------onPerformSync--------");
    }
}
