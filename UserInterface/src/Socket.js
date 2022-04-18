module.exports = class {
  #address;
  #port;
  #netModule;
  #macAddress;
  #fileSystem;
  #greenNotification
  #yellowNotification
  #redNotification

  constructor(address, port, greenNotification, yellowNotification, redNotification){
    this.#netModule = require('net');
    this.#fileSystem = require('fs');
    this.#port = port;
    this.#address = address;
    this.#macAddress = require('os').networkInterfaces().en0[0].mac;
    this.#greenNotification = greenNotification;
    this.#yellowNotification = yellowNotification;
    this.#redNotification = redNotification;
  }

  getInvoice(username, password, invoiceSeries, templateCode, start, end, storageFolder){
    let sendJsonObject = {
      job: 'GetInvoice',
      clientId: this.#macAddress,
      username: "0101183303-007",
      password: "123456aA@",
      invoiceSeries: "AA/20E",
      templateCode: "02GTTT0/001",
      start: 11037,
      end: 11039
    };
    const socket = new this.#netModule.Socket();
    socket.connect(10000, '127.0.0.1', () => {
      console.log(`Successfully open TCP connection to ${this.#address}:${this.#port}`);
      socket.write(`${JSON.stringify(sendJsonObject)}\r\n`);
    });
    let trunkage = "";
    socket.on('data', (data) => {
      data = String(data).trim();
      trunkage += String(data);
      if (data[data.length - 1] == '}'){
        // due to stupid JS (man I HATE THIS LANGUAGE SO MUCH!) segmented the
        // input package from TCP, it must be capture multiple package and joined
        // into one package in order to parse the data correctly.
        let jsonPackage = JSON.parse(trunkage);
        if(jsonPackage.error){
          this.#redNotification(jsonPackage.error);
        } else if (jsonPackage.file) {
          fileSystem.writeFile(`${storageFolder}/${jsonPackage.name}.pdf`, jsonPackage.file, 'base64', (err) =>{
            console.log(err);
          });
          this.#yellowNotification(`Hoàn thành tải về ${jsonPackage.name}.pdf`);
          console.log(`a file is written as ${jsonPackage.name}.pdf`);
        } else {
          this.#greenNotification(jsonPackage.response);
        }
        trunkage = "";
      }
    });
    socket.on('close', () => {
      console.log(`Connection to ${this.#address}:${this.#port} is closed`);
    });
  }

  async sendInvoice(username, password, excelFilePath){
    let sendObject = {
      job: 'ViettelInvoiceSend',
      username: username,
      password: password
    }
    await fileSystem.readFile(excelFilePath, 'base64', (err, data) => {
      if(err){
        console.log(err);
        return;
      }
      sendObject.file = data;
    });
    const socket = new this.#netModule.Socket();
    socket.connect(this.#port, this.#address, () => {
      socket.write(`${JSON.stringify(sendObject)}\r\n`);
    });
    socket.on('data', function(data) {
      data = Json.parse(data);
      yellowNotification(data.response);
    });
    socket.on("close", () => {
      console.log(`Connection to ${this.#address}:${this.#port} is closed`);
    });
  }
}
