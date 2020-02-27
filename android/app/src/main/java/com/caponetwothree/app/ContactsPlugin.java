package com.caponetwothree.app;

import android.Manifest;
import android.provider.ContactsContract;
import android.database.Cursor;
import android.util.Log;

import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import com.getcapacitor.JSObject;
import com.getcapacitor.JSArray;

import org.json.JSONException;

@NativePlugin(permissionRequestCode = 1, permissions = {Manifest.permission.READ_CONTACTS})
public class ContactsPlugin extends Plugin {

    public static final String CONTACT_ID = "contactId";
    //    public static final String EMAILS = "emails";
    public static final String PHONE_NUMBERS = "phoneNumbers";
    public static final String LOOKUP_KEY = "lookupKey";
    public static final String DISPLAY_NAME = "displayName";
    public static final String IMAGE = "imageUri";
//    public static final String ORGANIZATION_NAME = "organizationName";
//    public static final String ORGANIZATION_ROLE = "organizationRole";
//    public static final String BIRTHDAY = "birthday";

    @PluginMethod()
    public void authorize(PluginCall call) {
        if (!hasRequiredPermissions()) {
            requestPermissions(call);
        } else {
            JSObject result = new JSObject();
            call.success(result);
        }
    }

    @Override
    protected void handleRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.handleRequestPermissionsResult(requestCode, permissions, grantResults);

        PluginCall savedCall = getSavedCall();
        if (!hasRequiredPermissions()) {
            savedCall.error("Permissions not granted.");
        } else {
            savedCall.success();
        }
    }

    @PluginMethod()
    public void getContacts(PluginCall call) {
        JSObject result = new JSObject();
        JSArray contacts = new JSArray();
        String sortOrder = ContactsContract.Contacts.SORT_KEY_PRIMARY + " ASC";
        Cursor dataCursor = getContext().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                sortOrder);

        while (dataCursor.moveToNext()) {
            String numberOfPhoneNumbers = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            Boolean hasPhoneNumbers = !numberOfPhoneNumbers.equals("0");
            if (hasPhoneNumbers) {
            JSObject jsContact = new JSObject();
            String contactId = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Contacts._ID));
            jsContact.put(CONTACT_ID, contactId);
            jsContact.put(LOOKUP_KEY, dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY)));
            jsContact.put(DISPLAY_NAME, dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)));
            String contactImage = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));
            jsContact.put(IMAGE, contactImage);


//            addOrganization(jsContact);
            addPhoneNumbers(jsContact);
//            addEmails(jsContact);
//            addBirthday(jsContact);



                contacts.put(jsContact);
            }
        }
        dataCursor.close();
//        start a loop
//        JSObject contact = new JSObject();

//        contact.put("displayName", "test");
//        JSArray numbers = new JSArray();
//        numbers.put("0545894570");
//        numbers.put("+17086655768");
//        contact.put("phoneNumbers", numbers);
//
//        contacts.put(contact);
//end loop
        result.put("contacts", contacts);
        call.resolve(result);
    }

    private void addPhoneNumbers(JSObject jsContact) {
        try {
            JSArray phoneNumbers = new JSArray();
            String contactId = (String) jsContact.get(CONTACT_ID);
            Cursor cur1 = getContext().getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    new String[]{contactId}, null);
            while (cur1.moveToNext()) {
                String phoneNumber = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phoneNumbers.put(phoneNumber);
            }
            cur1.close();

            jsContact.put(PHONE_NUMBERS, phoneNumbers);
        } catch (JSONException e) {
            Log.e("Contacts", "JSONException addPhoneNumbers");
        }
    }

//    private void addPhoto(JSObject jsContact) {
//        try {
//            String base64Image;
//            String contactId = (String) jsContact.get(CONTACT_ID);
////            Cursor cur1 = getContext().getContentResolver().query(
////                    ContactsContract.CommonDataKinds.Photo.PHOTO_THUMBNAIL_URI, null,
////                    ContactsContract.CommonDataKinds.Photo.CONTACT_ID + " = ?",
////                    new String[]{contactId}, null);
//
////            while (cur1.moveToNext()) {
////                String phoneNumber = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
////                phoneNumbers.put(phoneNumber);
////            }
////            cur1.close();
//
////            jsContact.put(PHONE_NUMBERS, phoneNumbers);
//            String uri = null;
//            if (contactId != null) {
//                String[] projection = new String[] { ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
//                        ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI };
//                Cursor cur = getContext().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, projection, ContactsContract.CommonDataKinds.Phone._ID + " = ?",
//                        new String[] { contactId }, null);
//                String imageUri = null;
//                String thumbnailUri = null;
//                while (cur.moveToNext()) {
//                    imageUri = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
//                    thumbnailUri = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
//                }
//                uri = thumbnailUri != null ? thumbnailUri : imageUri;
//                Log.e("Contacts", "uri ######" + uri);
//            }
//
//
//        } catch (JSONException e) {
//            Log.e("Contacts", "JSONException addPhoneNumbers");
//        }
//    }
}
