importScripts('https://www.gstatic.com/firebasejs/9.1.3/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/9.1.3/firebase-messaging-compat.js');

firebase.initializeApp({
    apiKey: "AIzaSyBjQrfvzruRetnw31JNVd6hfn4Y4W5nS0E",
    authDomain: "notificationwms-dcca7.firebaseapp.com",
    projectId: "notificationwms-dcca7",
    storageBucket: "notificationwms-dcca7.appspot.com",
    messagingSenderId: "686068105005",
    appId: "1:686068105005:web:3d2e566e5e5bc51f1b1da4", 
    measurementId: "G-GTVLQMN7L4"
    
});
const messaging = firebase.messaging();