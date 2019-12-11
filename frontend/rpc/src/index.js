import {BufferEncoders, RSocketClient} from "rsocket-core";
import RSocketWebSocketClient from "rsocket-websocket-client";
import {HelloRequest} from './proto/hello_pb';
import {HelloServiceClient} from './proto/hello_rsocket_pb';

const initialize = async () => {
    const request = document.getElementById('request');
    const response = document.getElementById('response');

    const client = new RSocketClient({
        transport: new RSocketWebSocketClient({url: 'ws://localhost:7000/rsocket'}, BufferEncoders),
        setup: {
            keepAlive: 10000,
            lifetime: 20000,
        }
    });
    const rsocket = await client.connect();
    const helloService = new HelloServiceClient(rsocket);

    // Say Hello
    document.getElementById('hello')
        .addEventListener('click', async () => {
            try {
                const hello = new HelloRequest().setGreeting(request.value);
                const result = await helloService.sayHello(hello);
                response.innerText = result.getReply();
            } catch (error) {
                console.error(error);
                alert(JSON.stringify(error));
            }
        });

    // Lots Of Replies
    let cancel = null;
    document.getElementById('stream')
        .addEventListener('click', event => {
            if (!cancel) {
                let subscription = null;
                let n = 8;
                const hello = new HelloRequest().setGreeting(request.value);
                helloService.lotsOfReplies(hello)
                    .subscribe({
                        onSubscribe: sub => {
                            event.target.innerText = 'Cancel Stream';
                            subscription = sub;
                            subscription.request(n);
                            cancel = () => sub.cancel();
                        },
                        onNext: value => {
                            setTimeout(() => {
                                response.innerText = value.getReply() + '\n' + response.innerText;
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
                event.target.innerText = 'Lots of Replies';
            }
        });
};

document.addEventListener('DOMContentLoaded', initialize);