import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';
import { BehaviorSubject } from 'rxjs';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable()
export class WebSocketAPIService {
    webSocketEndPoint: string = environment.webScoketURL + "notification";
    stompClient: any;
    topic: string = "/topic/messages";
    private messageSource = new BehaviorSubject("");
    currentMessage = this.messageSource.asObservable();
    constructor() {
    }
    _connect() {
        let ws = new SockJS(this.webSocketEndPoint);
        this.stompClient = Stomp.over(ws);
        const _this = this;
        _this.stompClient.connect({}, frame => {
            // Subscribe to notification topic
            _this.stompClient.subscribe(this.topic, notifications => {
                // Update notifications attribute with the recent messsage sent from the server
                _this.onMessageReceived(notifications);
            })
        }, this.errorCallBack);
    };

    _disconnect() {
        if (this.stompClient !== null) {
            this.stompClient.disconnect();
        }
        console.log("Disconnected");
    }

    // on error, schedule a reconnection attempt
    errorCallBack(error) {
        console.log("errorCallBack -> " + error)
        setTimeout(() => {
            this._connect();
        }, 5000);
    }

    /**
     * Send message to sever via web socket
      * @param {*} message 
     */
    _send(message) {
        console.log("calling logout api via web socket");
        this.stompClient.send("/app/hello", {}, JSON.stringify(message));
    }

    onMessageReceived(message) {
        this.messageSource.next(JSON.parse(message.body))
    }
}