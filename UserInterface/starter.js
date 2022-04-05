const electronModule = require("electron");
const electron = electronModule.app;

let mainWindow;

function createWindow(){
  mainWindow = new electronModule.BrowserWindow({width: 800, height: 600});
  mainWindow.loadURL(`file://${__dirname}/public/MainPage.html`);
  mainWindow.webContents.openDevTools()
  mainWindow.on("closed", function(){
    mainWindow = null;
  });
}

electron.on('ready', createWindow);

electron.on("window-all-closed", function(){
  electron.quit()
})
