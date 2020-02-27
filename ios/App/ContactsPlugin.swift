//
//  ContactsPlugin.swift
//  App
//
//  Created by Guy Kedar on 2/26/20.
//

import Foundation
import Contacts
import Capacitor

let encoder = JSONEncoder()

var contactsStore: CNContactStore = CNContactStore();

@objc(ContactsPlugin)
public class ContactsPlugin: CAPPlugin {
    
    @objc func authorize(_ call: CAPPluginCall) {
        
        // Check if contacts is available
        contactsStore.requestAccess(for: .contacts) {(access, error) in
            if !access {
                call.reject("Could not get contacts read permission")
            }
            call.resolve()
            
        }
        
        // Request permission
        
    }
    
    @objc func getContacts(_ call: CAPPluginCall){
        
        print("calling get contacts $$$$$")
        //        let keysToFetch = [CNContactGivenNameKey, CNContactFamilyNameKey, CNContactPhoneNumbersKey, CNContactThumbnailImageDataKey]
        let req = CNContactFetchRequest(keysToFetch: [
            CNContactFamilyNameKey as CNKeyDescriptor,
            CNContactGivenNameKey as CNKeyDescriptor,
            CNContactPhoneNumbersKey as CNKeyDescriptor,
            CNContactThumbnailImageDataKey as CNKeyDescriptor,
            CNContactImageDataAvailableKey as CNKeyDescriptor
        ])
        
        req.sortOrder = CNContactSortOrder.userDefault
        
        var contacts: [[String: Any]]  = [];
        try! contactsStore.enumerateContacts(with: req) {
            contact, stop in
            if (!contact.phoneNumbers.isEmpty) {
                var numbers: [String] = []
                for phoneNumber in contact.phoneNumbers {
                    print("number:  \(phoneNumber.value.stringValue)")
                    numbers.append(phoneNumber.value.stringValue)
                }
                
                
                var customContact: [String: Any] = ["displayName": contact.givenName + " " + contact.familyName, "phoneNumbers": numbers
                ]
                if (contact.imageDataAvailable) {
                    customContact["image"] = contact.thumbnailImageData?.base64EncodedString()
                }
                
                contacts.append(customContact)
            }
            
        }
        print("## after contacts", contacts.count)
        call.resolve([
            "contacts": contacts
        ])
        
        // Pass in date from web code
        
        // run query
        
        // Pass result back to web code
        
    }
}
