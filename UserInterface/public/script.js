const socketClass = require(`../src/Socket.js`);
const fileSystem = require('fs');

const settingPane = document.querySelector("#setting-pane");
const settingSelector = document.querySelector('#setting-selector');
const searchInvoicePane = document.querySelector("#search-invoice-pane");
const searchInvoiceSelector = document.querySelector("#search-invoice-selector");
const getInvoicePane = document.querySelector('#get-invoice-pane');
const getInvoiceSelector = document.querySelector('#get-invoice-selector');
const sendInvoicePane = document.querySelector('#send-invoice-pane');
const sendInvoiceSelector = document.querySelector('#send-invoice-selector');

const usernameInput = document.querySelector('#username-input');
const passwordInput = document.querySelector('#password-input');
const addressInput = document.querySelector('#address-input');
const portInput = document.querySelector('#port-input');
const invoiceStorageFolderInput = document.querySelector('#invoice-storage-folder-input');
// const chooseStorageFolderButton = document.querySelector('#choose-folder-button');
const saveInfoButton = document.querySelector('#store-info-button');

const greenNotification = document.querySelector("#green-notification");
const yellowNotification = document.querySelector('#yellow-notification');
const redNotification = document.querySelector('#red-notification');

const excelFilePathInput = document.querySelector('#excel-file-input');
const chooseExcelFilePathButton = document.querySelector('#choose-excel-file-button');
const sendInvoiceButton = document.querySelector('#send-invoice-button');

const invoiceSeriesInput = document.querySelector('#invoice-series-input');
const templateCodeInput = document.querySelector('#template-code-input');
const startNumberInput = document.querySelector('#start-number-input');
const endNumberInput = document.querySelector('#end-number-input')
const startDownloadInvoiceButton = document.querySelector('#start-download-invoice-button');

const configFile = 'config.txt';

let displayingPane;
let displayingNotification;
let socket;

function toggleGreenNotification(displayMsg){
  if (displayingNotification) {
    displayingNotification.classList.toggle('non-active-pane');
  }
  greenNotification.innerHTML = displayMsg;
  greenNotification.classList.toggle('non-active-pane');
  displayingNotification = greenNotification;
}
function toggleYellowNotification(displayMsg){
  if (displayingNotification) {
    displayingNotification.classList.toggle('non-active-pane');
  }
  yellowNotification.innerHTML = displayMsg;
  yellowNotification.classList.toggle('non-active-pane');
  displayingNotification = yellowNotification;
}
function toggleRedNotification(displayMsg){
  if (displayingNotification) {
    displayingNotification.classList.toggle('non-active-pane');
  }
  redNotification.innerHTML = displayMsg;
  redNotification.classList.toggle('non-active-pane');
  displayingNotification = redNotification;
}
function storeInfomation(){
  const username = usernameInput.value;
  const password = passwordInput.value;
  const address = addressInput.value;
  const port = portInput.value;
  const storageFolder = invoiceStorageFolderInput.value;
  const invoiceSeries = invoiceSeriesInput.value;
  const templateCode = templateCodeInput.value;
  const content = `username:${username}\n`
  + `password:${password}\n`
  + `address:${address}\n`
  + `port:${port}\n`
  + `storageFolder:${storageFolder}\n`
  + `invoiceSeries:${invoiceSeries}\n`
  + `templateCode:${templateCode}`
  fileSystem.writeFile('config.txt', content, (error) => {
    if(error){
      toggleRedNotification("Kh??ng th??? ghi l???i th??ng tin");
      console.log(error);
    }
  });
}
function startSocket(){
  const address = addressInput.value;
  if (address == ""){
    toggleRedNotification("?????a ch??? m??y ch??? ch??a ???????c ??i???n");
    return;
  }
  const port = portInput.value;
  if (port == ""){
    toggleRedNotification("C???ng ????ng nh???p ch??a ???????c ??i???n");
    return;
  }
  try{
    socket = new socketClass(address, Number(port), toggleGreenNotification, toggleYellowNotification, toggleRedNotification);
  } catch (e){
    console.log(e);
    toggleRedNotification('Kh??ng th??? nh???n di???n ???????c MAC address, vui l??ng li??n h??? v???i ng?????i vi???t ph???n m???m');
  }
}
async function boostup(){
  await fileSystem.readFile(configFile, 'utf8', (err, data) => {
    if (err) {
      console.error(err)
      toggleRedNotification("Th??ng tin c???n thi???t ch??a ???????c khai b??o, vui l??ng v??o C??i ?????t");
      return
    }
    for (line of data.split('\n')){
      arr = line.split(':');
      switch (arr[0]) {
        case 'username':
        usernameInput.value = arr[1];
        break;
        case 'password':
        passwordInput.value = arr[1];
        break;
        case 'address':
        addressInput.value = arr[1];
        break;
        case 'port':
        portInput.value = arr[1];
        break;
        case 'storageFolder':
        invoiceStorageFolderInput.value = arr[1];
        break;
        case 'invoiceSeries':
        invoiceSeriesInput.value = arr[1];
        break;
        case 'templateCode':
        templateCodeInput.value = arr[1];
        break;
      }
    }
    startSocket();
  });
}

settingSelector.addEventListener("click", function(){
  if(displayingPane){
    displayingPane.classList.toggle("non-active-pane");
  }
  settingPane.classList.toggle("non-active-pane");
  displayingPane = settingPane;
});
searchInvoiceSelector.addEventListener('click', function(){
  if(displayingPane){
    displayingPane.classList.toggle("non-active-pane");
  }
  searchInvoicePane.classList.toggle("non-active-pane");
  displayingPane = searchInvoicePane;
});
getInvoiceSelector.addEventListener('click', function(){
  if(displayingPane){
    displayingPane.classList.toggle("non-active-pane");
  }
  getInvoicePane.classList.toggle("non-active-pane");
  displayingPane = getInvoicePane;
});
sendInvoiceSelector.addEventListener('click', function(){
  if(displayingPane){
    displayingPane.classList.toggle("non-active-pane");
  }
  sendInvoicePane.classList.toggle("non-active-pane");
  displayingPane = sendInvoicePane;
});

greenNotification.addEventListener('click', function() {
  greenNotification.classList.toggle('non-active-pane');
  greenNotification.innerHTML = "";
  displayingNotification = null;
});
yellowNotification.addEventListener('click', function() {
  yellowNotification.classList.toggle('non-active-pane');
  yellowNotification.innerHTML = "";
  displayingNotification = null;
});
redNotification.addEventListener('click', function() {
  redNotification.classList.toggle('non-active-pane');
  redNotification.innerHTML = "";
  displayingNotification = null;
});

invoiceStorageFolderInput.addEventListener('change', function (){
  if (invoiceStorageFolderInput.value.length != 0){
    if (invoiceStorageFolderInput.value[invoiceStorageFolderInput.length - 1] !== '\\' || invoiceStorageFolderInput.value[invoiceStorageFolderInput.length - 1] !== "/"
    ){
      if (process.platform === "win32"){
        invoiceStorageFolderInput.value = invoiceStorageFolderInput.value + "\\";
      } else {
        invoiceStorageFolderInput.value = invoiceStorageFolderInput.value + "/";
      }
    }
  }
});

chooseExcelFilePathButton.addEventListener('click', function() {
  const fileChooser = document.createElement('input');
  fileChooser.type = 'file';
  fileChooser.onchange = () => {
    excelFilePathInput.value = fileChooser.files[0].path;
  }
  fileChooser.click();
});
sendInvoiceButton.addEventListener('click', function(){
  username = usernameInput.value.trim();
  if (username == ""){
    toggleRedNotification("T??n ng?????i d??ng ch??a ???????c ??i???n, khai b??o ??? ph???n C??i ?????t");
    return;
  }
  password = passwordInput.value.trim();
  if (password == ""){
    toggleRedNotification("M???t kh???u ch??a ???????c ??i???n, khai b??o ??? ph???n C??i ?????t");
    return;
  }
  excelFilePath = excelFilePathInput.value.trim();
  if (excelFilePath == ""){
    toggleRedNotification("???????ng d???n t???p tin excel b??? b??? tr???ng");
    return;
  }
  if (!socket) startSocket();
  socket.sendInvoice(username, password, excelFilePath);
});
startDownloadInvoiceButton.addEventListener('click', function() {
  let username = usernameInput.value;
  if (username == "") {
    toggleRedNotification("T??n ng?????i d??ng ????ng nh???p v??o Viettel ch??a ???????c ??i???n, vui l??ng v??o ph???n C??i ?????t");
    return;
  }
  let password = passwordInput.value;
  if (password == "") {
    toggleRedNotification("M???t kh???u d??ng ????ng nh???p v??o Viettel ch??a ???????c ??i???n, vui l??ng v??o ph???n C??i ?????t");
    return;
  }
  let invoiceSeries = invoiceSeriesInput.value;
  if (invoiceSeries == "") {
    toggleRedNotification("K?? hi???u ho?? ????n ch??a ???????c ??i???n");
    return;
  }
  let templateCode = templateCodeInput.value;
  if (templateCode == "") {
    toggleRedNotification("M???u ho?? ????n ch??a ???????c ??i???n");
    return;
  }
  let startNumber = Number(startNumberInput.value);
  if (startNumber == "") {
    toggleRedNotification("S??? b???t ?????u ch??a ???????c ??i???n");
    return;
  }
  let endNumber = Number(endNumberInput.value);
  if (endNumber == "") {
    toggleRedNotification("S??? k???t th??c ch??a ???????c ??i???n");
    return;
  }
  let storageFolder = invoiceStorageFolderInput.value;
  if (storageFolder == ""){
    toggleRedNotification("Th?? m???c ch???a ho?? ????n t???i v??? ch??a ???????c khai b??o");
    return;
  }
  storeInfomation();
  if (!socket) startSocket();
  socket.getInvoice(username, password, invoiceSeries, templateCode, startNumber, endNumber, storageFolder);
});
saveInfoButton.addEventListener('click', function(){
  storeInfomation();
  toggleGreenNotification('Ho??n th??nh ghi l???i th??ng tin');
});

boostup();
