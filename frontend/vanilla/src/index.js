import {RSocketClient} from "rsocket-core";
import RSocketWebSocketClient from "rsocket-websocket-client";

const initialize = async () => {
    const request = document.getElementById('request');
    const response = document.getElementById('response');

    const client = new RSocketClient({
        transport: new RSocketWebSocketClient({url: 'ws://localhost:7000/rsocket'}),
        setup: {
            dataMimeType: 'application/json',
            metadataMimeType: 'message/x.rsocket.routing.v0',
            keepAlive: 10000,
            lifetime: 20000,
        }
    });
    const rsocket = await client.connect();

    // Request Response
    document.getElementById('hello')
        .addEventListener('click', async () => {
            try {
                const res = await rsocket.requestResponse({
                    data: request.value,
                    metadata: routingMetadata('hello')
                });
                response.innerText = res.data;
            } catch (error) {
                console.error(error);
                alert(JSON.stringify(error));
            }
        });

    // Request Stream
    let cancel = null;
    document.getElementById('stream')
        .addEventListener('click', event => {
            if (!cancel) {
                let subscription = null;
                let n = 8;
                rsocket.requestStream({
                    data: request.value,
                    metadata: routingMetadata('hello.many')
                }).subscribe({
                    onSubscribe: sub => {
                        event.target.innerText = 'Cancel Stream';
                        subscription = sub;
                        subscription.request(n);
                        cancel = () => sub.cancel();
                    },
                    onNext: value => {
                        setTimeout(() => {
                            response.innerText = value.data + '\n' + response.innerText;
                            if (--n === 0) {
                                n = 8;
                                subscription.request(n);
                            }
                        }, 500);
                    },
                    onError: error => {
                        console.error(error);
                        alert(JSON.stringify(error));
                    },
                });
            } else {
                cancel();
                cancel = null;
                event.target.innerText = 'Request Stream';
            }
        });
};

const routingMetadata = (route) => {
    return String.fromCharCode(route.length) + route;
};

document.addEventListener('DOMContentLoaded', initialize);