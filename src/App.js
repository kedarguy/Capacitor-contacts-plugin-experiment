import React from "react";
import logo from "./logo.svg";
import { Plugins, Capacitor } from "@capacitor/core";
import "./App.css";

const { ContactsPlugin } = Plugins;

function App() {
  const [contacts, setContacts] = React.useState([]);

  React.useEffect(() => {
    async function getDistance() {
      // let result = await DistancePlugin.authorize();
      // let data = await DistancePlugin.getDistance({ startDate: "2019/07/01" });
      // console.log('########### data', data);
    }
    getDistance();
  }, []);

  React.useEffect(() => {
    console.log("plugins:", Plugins);
    async function getContactsAuthorization() {
      let result = await ContactsPlugin.authorize();
    }
    getContactsAuthorization();
  }, []);
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Ediaat5555 <code>src/App.js</code> and save to reload.
        </p>
        <button
          onClick={async () => {
            const res = await ContactsPlugin.getContacts();
            console.log("res ####", res.contacts);
            setContacts(res.contacts);
          }}
        >
          get contacts
        </button>
        <div>
          {contacts.map(contact => (
            <div>
              {(contact.image || contact.imageUri) && (
                <img
                  src={
                    contact.image
                      ? `data:image/png;base64, ${contact.image}`
                      : Capacitor.convertFileSrc(contact.imageUri)
                  }
                  alt="Red dot"
                />
              )}
              {contact.displayName} {contact.phoneNumbers[0]}
            </div>
          ))}
        </div>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
    </div>
  );
}

export default App;
