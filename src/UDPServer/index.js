const dgram = require('dgram');
const server = dgram.createSocket('udp4');

const MESSAGE = 'RESPONSE';
server.on('error', (err) => {
  console.log(`server error:\n${err.stack}`);
  server.close();
});

server.on('message', (msg, rinfo) => {
  console.log(`${new Date().toISOString()}  server got: ${msg} from ${rinfo.address}:${rinfo.port}`);
  server.send(MESSAGE, 41234, rinfo.address, err => {
	    if (err) {
        console.log('There was an error while sending the packet', err);
      } else {
        console.log(`${new Date().toISOString()}  response message sent to ${rinfo.address}`);
      }
  })
});

server.on('listening', () => {
  var address = server.address();
  console.log(`server listening ${address.address}:${address.port}`);
});

server.bind(41234);