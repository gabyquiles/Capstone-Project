package com.gabyquiles.eventy.addeditevent;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import com.gabyquiles.eventy.model.BaseGuest;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
public class GetContactAsyncTask extends AsyncTask<Uri, Void, BaseGuest> {
    private final String LOG_TAG = GetContactAsyncTask.class.getSimpleName();

    private Context mContext;
    private AddEditEventContract.Presenter mPresenter;

    public GetContactAsyncTask(Context context, AddEditEventContract.Presenter presenter) {
        mContext = context;
        mPresenter = presenter;
    }

    @Override
    protected BaseGuest doInBackground(Uri... uris) {
        int count = uris.length;

        String fullNameIdx = ContactsContract.Contacts.DISPLAY_NAME;
        String emailIdx = ContactsContract.CommonDataKinds.Email.ADDRESS;
        String[] fields = { emailIdx, fullNameIdx};

        BaseGuest guest = null;
        for (Uri uri: uris) {
            Cursor cursor = mContext.getContentResolver().query(uri, fields, null, null, null);
            if(cursor != null) {
                if (cursor.moveToFirst()) {
                    int fullnameColumn = cursor.getColumnIndex(fullNameIdx);
                    String fullName = cursor.getString(fullnameColumn);
                    int emailColumn = cursor.getColumnIndex(emailIdx);
                    String email = cursor.getString(emailColumn);

                    guest = new BaseGuest(fullName, email);
                }
                cursor.close();
            }
        }
        return guest;
    }

    @Override
    protected void onPostExecute(BaseGuest guest) {
//        super.onPostExecute(guest);
        if (guest != null) {
            mPresenter.addGuest(guest);
        } else {
            mPresenter.errorSelectingGuest();
        }
    }
}
