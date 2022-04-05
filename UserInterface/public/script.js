const settingPane = document.querySelector("#setting-pane");
const settingSelector = document.querySelector('#setting-selector');
const searchInvoicePane = document.querySelector("#search-invoice-pane");
const searchInvoiceSelector = document.querySelector("#search-invoice-selector");
const getInvoicePane = document.querySelector('#get-invoice-pane');
const getInvoiceSelector = document.querySelector('#get-invoice-selector');
const sendInvoicePane = document.querySelector('#send-invoice-pane');
const sendInvoiceSelector = document.querySelector('#send-invoice-selector');

let displayingPane;

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
