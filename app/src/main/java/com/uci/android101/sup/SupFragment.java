package com.uci.android101.sup;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by somarkoe on 11/2/16.
 */

public class SupFragment extends Fragment {

    private static final int REQUEST_CONTACT = 0;
    private static final String SAVED_STATE_FRIEND_NAME = "SAVED_STATE_FRIEND_NAME";
    private static final String SAVED_STATE_FRIEND_PHONE_NUMBER = "SAVED_STATE_FRIEND_PHONE_NUMBER";

    private Button mSelectFriend;
    private Button mSendButton;
    private TextView mLastSupSent;
    private Friend mFriend;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sup, container, false);

        mSelectFriend = (Button) v.findViewById(R.id.select_friend);
        mSendButton = (Button) v.findViewById(R.id.send_sup);
        mLastSupSent = (TextView) v.findViewById(R.id.last_sup_sent);
        if (mFriend == null) {
            mFriend = new Friend();
        }
        String savedContactName = null;
        if (savedInstanceState != null) {
            savedContactName = savedInstanceState.getString(SAVED_STATE_FRIEND_NAME);
            String number = savedInstanceState.getString(SAVED_STATE_FRIEND_PHONE_NUMBER);

            mFriend.setFriendName(savedContactName);
            mFriend.setPhoneNumber(number);
        }

        if (savedContactName != null) {
            mSelectFriend.setText(savedContactName);
        }

        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

        mSelectFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFriend.getPhoneNumber() != null) {
                    sendSup();
                } else {
                    sendCustomizedSupWithoutPhoneNumber();
                }
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            populateName(contactUri);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mFriend != null) {
            outState.putString(SAVED_STATE_FRIEND_NAME, mFriend.getFriendName());
            outState.putString(SAVED_STATE_FRIEND_PHONE_NUMBER, mFriend.getPhoneNumber());
        }
    }

    /**
     * Method responsible for sending sup messages.
     */
    private void sendSup() {
        SmsManager smsManager;
        try {
            smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(mFriend.getPhoneNumber(), null, getString(R.string.message),
                    null, null);
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.sent),
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(),
                    getString(R.string.sup_error),
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            mSelectFriend.setText(R.string.select_friend);
            mFriend = new Friend();
        }
    }

    /**
     * Method responsible for sending sup message without phone number.
     */
    private void sendCustomizedSupWithoutPhoneNumber() {
        Toast.makeText(getActivity().getApplicationContext(), R.string.noPhoneNumber,
                Toast.LENGTH_SHORT).show();

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, getString(R.string.message));
        startActivity(i);
    }

    /**
     * Retrieves the contact name and phone number from address book.
     * @param contactUri
     */
    private void populateName(Uri contactUri) {
        String[] queryFields = new String[] {
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER,
                ContactsContract.Contacts._ID
        };

        Cursor cursor = getActivity().getContentResolver()
                .query(contactUri, queryFields, null, null, null);

        try {
            if (cursor.getCount() == 0) {
                return;
            }
            cursor.moveToFirst();
            int nameColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            String name = cursor.getString(nameColumnIndex);

            mFriend.setFriendName(name);
            mSelectFriend.setText(name);

            if (name != null) {
                int hasPhoneColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
                int idColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                String hasPhone = cursor.getString(hasPhoneColumnIndex);
                String id = cursor.getString(idColumnIndex);

                populatePhoneNumber(hasPhone.equalsIgnoreCase("1"), id);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * Retrieves the phone number from address book.
     * @param hasPhone
     * @param id
     */
    private void populatePhoneNumber(boolean hasPhone, String id) {
        String phoneNumber = null;
        if (hasPhone) {
            Cursor phones = getActivity().getContentResolver()
                    .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                            null, null);
            try {
                phones.moveToFirst();
                phoneNumber = phones.getString(phones.getColumnIndex("data1"));
            } finally {
                if (phones != null) {
                    phones.close();
                }
            }
        }
        mFriend.setPhoneNumber(phoneNumber);
    }
}
