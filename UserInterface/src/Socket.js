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
    this.#greenNotification = greenNotification;
    this.#yellowNotification = yellowNotification;
    this.#redNotification = redNotification;
    this.#macAddress = this.#getMAC();
  }

  getInvoice(username, password, invoiceSeries, templateCode, start, end, storageFolder){
    let sendJsonObject = {
      job: 'GetInvoice',
      clientId: this.#macAddress,
      username: username,
      password: password,
      invoiceSeries: invoiceSeries,
      templateCode: templateCode,
      start: start,
      end: end
    };
    const socket = new this.#netModule.Socket();
    socket.connect(this.#port, this.#address, () => {
      console.log(`Successfully open TCP connection to ${this.#address}:${this.#port}`);
      socket.write(`${JSON.stringify(sendJsonObject)}\r\n`);
    });
    socket.on('error', (err) => {
      console.log(err);
      if (String(err).includes("ECONNREFUSED")){
        this.#redNotification("Không kết nối được đến máy chủ");
      }
    })
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
      job: 'SendInvoice',
      username: username,
      password: password,
      clientId: this.#macAddress,
      file: fileSystem.readFileSync(excelFilePath, {encoding:'base64', flag:'r'})
    }
    console.log(sendObject);
    const socket = new this.#netModule.Socket();
    socket.connect(this.#port, this.#address, () => {
      console.log(`Successfully open TCP connection to ${this.#address}:${this.#port}`);
      socket.write(`${JSON.stringify(sendObject)}\r\n`);
    });
    socket.on('error', (err) => {
      console.log(err);
      if (String(err).includes("ECONNREFUSED")){
        this.#redNotification("Không kết nối được đến máy chủ");
      }
    });
    socket.on('data', (data) => {
      data = JSON.parse(data);
      console.log(data);
      if(data.error){
        this.#redNotification(data);
      } else {
        this.#yellowNotification(data);
      }
    });
    socket.on("close", () => {
      console.log(`Connection to ${this.#address}:${this.#port} is closed`);
    });
  }

  #getMAC(){
    const interfaces = require('os').networkInterfaces();
    for (const i in interfaces){
      for (const ele of interfaces[i]){
        if (ele.mac !== '00:00:00:00:00:00' && ele.mac !== "ff:ff:ff:ff:ff:ff"){
          return ele.mac;
        }
      }
    }
    return null;
  }
}
