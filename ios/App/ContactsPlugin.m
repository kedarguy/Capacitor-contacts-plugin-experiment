//
//  ContactsPlugin.m
//  App
//
//  Created by Guy Kedar on 2/26/20.
//

#import <Foundation/Foundation.h>
#import <Capacitor/Capacitor.h>

CAP_PLUGIN(ContactsPlugin, "ContactsPlugin",
           CAP_PLUGIN_METHOD(authorize, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(getContacts, CAPPluginReturnPromise);
           )
