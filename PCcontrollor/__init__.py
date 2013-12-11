#encoding:utf-8
'''
Created on 2013��11��1��

@author: yangluo
'''
import pygame
import wx
import os
import socket
import win32api
import win32con
import socket
import thread
import threading

window_size = (420,350)
child_win_size = (300,200)
button_size = (70,30)
startbutton_size = (122,64)
blank1_size = (120,40)
blank2_size = (120,30)

# frame_colour = wx.Colour(198,222,241)
frame_colour = wx.Colour(255,255,255)
text_colour = wx.Colour(40,139,213)





class MyFrame(wx.Frame):
    def __init__(self, parent=None, id=-1,
                 pos=wx.DefaultPosition, title='Controlor_Server'):
         wx.Frame.__init__(self, parent, id,title,pos,size=window_size)
#         panel = wx.Panel(self)
#         frame_bkg = image.ConvertToBitmap()
#         self.bitmap_bkg = wx.StaticBitmap(parent=panel,bitmap=frame_bkg)


class childFrame(wx.Frame):     
    def  __init__(self,image,parent=None,id=-1,pos=wx.DefaultPosition,title="newDialog"):   
        temp = image.ConvertToBitmap()
        size = temp.GetWidth(), temp.GetHeight()
        wx.Frame.__init__(self, parent, id, title, pos, size)
        panel = wx.Panel(self)
        self.bmp = wx.StaticBitmap(parent=panel, bitmap=temp)
        self.SetClientSize(size)
        
        #print'frame!' 

class MyApp(wx.App):
    def __init__(self,port=5757,redirect=False):
        #print"a new app"
        wx.App.__init__(self,redirect)
        self.isStartServer = False
        self.isReStartServer = False
#         self.clientNum = 0
        
        #conductEvent
        #self.messageQueue = []
        
        #socket
        self.client_list = []
        self.isConnect = False
        self.socket = socket.socket()
        self.msgList = []
        self.port = port
        self.ip=socket.gethostbyname(socket.gethostname())
        
        #create thread 
        self.keythread_handle = threading.Thread(target=self.conductThread)
        self.recvMsgthread_handle = threading.Thread(target=self.recvMsgThread)

        
        self.keythread_handle.start()
        self.recvMsgthread_handle.start()
        
    def OnInit(self):
        self.listFont = wx.Font(15,wx.MODERN,wx.NORMAL,wx.NORMAL)

        self.win = MyFrame()
        bkg = wx.Panel(self.win)
        bkg.SetBackgroundColour(frame_colour)
        
        self.start_common = wx.Image("image/start_common.jpg", wx.BITMAP_TYPE_JPEG).ConvertToBitmap()
        self.start_focus  = wx.Image("image/start_focus.jpg",  wx.BITMAP_TYPE_JPEG).ConvertToBitmap()
       # self.start_disable= wx.Image("image/start_disable.jpg",wx.BITMAP_TYPE_JPEG).ConvertToBitmap()
        self.start_press  = wx.Image("image/start_press.jpg",  wx.BITMAP_TYPE_JPEG).ConvertToBitmap()
         
        self.pause_common = wx.Image("image/pause_common.jpg", wx.BITMAP_TYPE_JPEG).ConvertToBitmap()
        self.pause_focus  = wx.Image("image/pause_focus.jpg",  wx.BITMAP_TYPE_JPEG).ConvertToBitmap()
        #self.pause_disable= wx.Image("image/pause_disable.jpg",wx.BITMAP_TYPE_JPEG).ConvertToBitmap()
        self.pause_press  = wx.Image("image/pause_press.jpg",  wx.BITMAP_TYPE_JPEG).ConvertToBitmap()
         
        self.info_common  = wx.Image("image/info_common.jpg", wx.BITMAP_TYPE_JPEG).ConvertToBitmap()
        self.info_focus   = wx.Image("image/info_focus.jpg",  wx.BITMAP_TYPE_JPEG).ConvertToBitmap()
        self.info_press   = wx.Image("image/info_press.jpg",  wx.BITMAP_TYPE_JPEG).ConvertToBitmap()

#         start_common = wx.Image("image/start_common.png", wx.BITMAP_TYPE_PNG).ConvertToBitmap()
#         start_focus  = wx.Image("image/start_focus.png",  wx.BITMAP_TYPE_PNG).ConvertToBitmap()
#         start_disable= wx.Image("image/start_disable.png",wx.BITMAP_TYPE_PNG).ConvertToBitmap()
#         start_press  = wx.Image("image/start_press.png",  wx.BITMAP_TYPE_PNG).ConvertToBitmap()
#         
#         pause_common = wx.Image("image/pause_common.png", wx.BITMAP_TYPE_PNG).ConvertToBitmap()
#         pause_focus  = wx.Image("image/pause_focus.png",  wx.BITMAP_TYPE_PNG).ConvertToBitmap()
#         pause_disable= wx.Image("image/pause_disable.png",wx.BITMAP_TYPE_PNG).ConvertToBitmap()
#         pause_press  = wx.Image("image/pause_press.png",  wx.BITMAP_TYPE_PNG).ConvertToBitmap()
#         
#         info_common  = wx.Image("image/info_common.png", wx.BITMAP_TYPE_PNG).ConvertToBitmap()
#         info_focus   = wx.Image("image/info_focus.png",  wx.BITMAP_TYPE_PNG).ConvertToBitmap()
#         info_press   = wx.Image("image/info_press.png",  wx.BITMAP_TYPE_PNG).ConvertToBitmap()
        
        
        ST_IP = wx.Image("image/st_ip.jpg",wx.BITMAP_TYPE_JPEG).ConvertToBitmap()
        ST_Port = wx.Image("image/st_port.jpg",wx.BITMAP_TYPE_JPEG).ConvertToBitmap()
        ST_Msglist = wx.Image("image/st_messagelist.jpg",wx.BITMAP_TYPE_JPEG).ConvertToBitmap()
        
        

        self.startButton = wx.BitmapButton(bkg,-1,self.start_common,size=startbutton_size)
        self.startButton.Bind(wx.EVT_BUTTON, self.startBtn)
        
        self.startButton.SetBitmapSelected(self.start_press)
        #self.startButton.SetBitmapFocus(self.start_focus)
        #self.startButton.SetBitmapDisabled(self.start_disable)

#         self.pauseButton = wx.BitmapButton(bkg,-1,self.pause_common,size=button_size)
#         self.pauseButton.Bind(wx.EVT_BUTTON,self.pauseBtn)
#         
#         self.pauseButton.SetBitmapSelected(self.pause_press)
#         self.pauseButton.SetBitmapFocus(self.pause_focus)
#         self.pauseButton.SetBitmapDisabled(self.pause_disable)
#         self.pauseButton.Enable(False)
        
        self.infoButton = wx.BitmapButton(bkg,-1,self.info_common,size=startbutton_size)
        self.infoButton.Bind(wx.EVT_BUTTON,self.infoBtn)
        self.infoButton.SetBitmapSelected(self.info_press)
        self.infoButton.SetBitmapFocus(self.info_focus)
        
        #ST~~
        ST_IP_text = wx.StaticBitmap(bkg,-1,  ST_IP)
        ST_Port_name = wx.StaticBitmap(bkg,-1, ST_Port)
        ST_Message = wx.StaticBitmap(bkg,-1, ST_Msglist)
        self.IP_text = wx.TextCtrl(bkg)
        self.Port_name  = wx.TextCtrl(bkg)
        self.IP_text.SetEditable(False)
        self.Port_name.SetEditable(False)
        ST_IP_text.SetFocus()
        
        self.IP_text.SetOwnForegroundColour(text_colour)
        self.Port_name.SetOwnForegroundColour(text_colour)
#         self.IP_text.SetInitialSize((,30))
        
        self.IP_text.SetTransparent(254)
        self.Port_name.SetBackgroundColour(frame_colour)
    
    
        #ST_clientNum = wx.StaticText(bkg,-1,"clientnum:")
#         self.client_text = wx.TextCtrl(bkg)
    
        self.ip=socket.gethostbyname(socket.gethostname())
        self.IP_text.SetValue(self.ip)
#         self.IP_text.Disable()
        self.Port_name.SetValue('5757')
        
        self.messageListbox = wx.ListBox(bkg,style = wx.TE_MULTILINE | wx.HSCROLL)
        self.messageListbox.SetForegroundColour(text_colour)
        self.messageListbox.SetFont(self.listFont)

        listItem = ['keyValue:']
        self.messageListbox.InsertItems(listItem,0)
#         for i in range(10):
#             self.messageListbox.Append("values%d"%i)
        hbox1 = wx.BoxSizer()
        hbox1.Add(ST_IP_text, proportion=0, flag=wx.ALIGN_CENTER, border=5)
        hbox1.Add(self.IP_text, proportion=1, flag = wx.ALIGN_CENTER)
#         hbox1.Add(self.infoButton, proportion=0, flag=wx.ALIGN_CENTER, border=5)

        hbox2 = wx.BoxSizer()
        hbox2.Add(ST_Port_name, proportion=0, flag=wx.ALIGN_CENTER,border=5)
        hbox2.Add(self.Port_name,proportion=1, flag=wx.ALIGN_CENTER)
        #hbox2.Add(ST_clientNum,proportion=0,flag=wx.ALIGN_CENTER)
#         hbox2.Add(self.client_text,proportion=0,flag=wx.ALIGN_CENTER)
#         hbox2.Add(self.startButton,proportion=0,flag=wx.ALIGN_CENTER,border=5)
#         hbox2.Add(self.pauseButton,proportion=0,flag=wx.LEFT,border=5)
#         hbox3 = wx.BoxSizer()
#         #hbox3.Add(ST_Port_name, proportion=1, flag=wx.ALIGN_CENTER,border=5)
#         blank_str = "                                "
#         leftBlank = wx.StaticText(bkg,-1,blank_str)
#         rightBlank =wx.StaticText(bkg,-1,blank_str)
#         hbox3.Add(leftBlank,proportion=1,flag=wx.ALIGN_CENTER,border=5)
#         hbox3.Add(self.startButton,proportion=0,flag=wx.LEFT)
#         hbox3.Add(rightBlank,proportion=1,flag=wx.ALIGN_CENTER,border=5)

        #button
        vbox1 = wx.BoxSizer(wx.VERTICAL)
        blank_str = "              "
        blank_text1 = wx.StaticText(bkg,-1,blank_str,size=blank1_size)
        blank_text2 = wx.StaticText(bkg,-1,blank_str,size=blank2_size)
        #blank_text3 = wx.StaticText(bkg,-1,blank_str,size=blank3_size)
        vbox1.Add(blank_text1,proportion=0)
        vbox1.Add(self.startButton,proportion=0,flag=wx.ALIGN_CENTER)
        vbox1.Add(blank_text2,proportion=0)
        #vbox1.Add(blank_text3,proportion=0)
        vbox1.Add(self.infoButton,proportion=0,flag=wx.ALIGN_CENTER)
        #list
        hbox4 = wx.BoxSizer()
        hbox4.Add(ST_Message,proportion=1,flag=wx.ALIGN_CENTER)
        
        vbox2 = wx.BoxSizer(wx.VERTICAL)
        vbox2.Add(hbox4,proportion=0,flag=wx.EXPAND|wx.ALL,border=5)
        vbox2.Add(self.messageListbox, proportion=1, flag=wx.EXPAND|wx.LEFT|wx.BOTTOM|wx.RIGHT, border=5)
        
        hbox5 = wx.BoxSizer()
        hbox5.Add(vbox2,proportion=1,flag=wx.EXPAND|wx.ALL,border=5)
        hbox5.Add(vbox1,proportion=0,flag=wx.EXPAND|wx.ALL,border=5)
        
        
#       aa  hbox3.Add(self.infoButton,proportion=0,flag=wx.ALIGN_CENTER,border=5)
        
        vbox3 = wx.BoxSizer(wx.VERTICAL)
        vbox3.Add(hbox1,proportion=0, flag=wx.EXPAND|wx.ALL,border=5)
        vbox3.Add(hbox2,proportion=0,flag = wx.EXPAND|wx.ALL,border=5)
        vbox3.Add(hbox5,proportion=1,flag=wx.EXPAND|wx.ALL,border=5)
        
        
        
        bkg.SetSizer(vbox3)
        self.win.Show()
        #self.SetTopWindow(self.win)

        return True
    
    def startBtn(self,event):
        if  self.isStartServer:
            self.isStartServer = False
            #self.startButton.RefreshRect(self.startButton.GetRect(),True)
            self.startButton.SetBitmapLabel(self.start_common)
            self.startButton.SetBitmapSelected(self.start_press)
            #self.startButton.SetBitmapFocus(self.start_focus) 
            
        else:
            self.isStartServer = True
            self.startButton.Refresh(True,self.startButton.GetRect())
            self.startButton.SetBitmapSelected(self.pause_press)
            #self.startButton.SetBitmapFocus(self.pause_focus)
            self.startButton.SetBitmapLabel(self.pause_common)
            
            
        (serverIP, serverPort) = self.IP_text.GetValue(), self.Port_name.GetValue()
        self.setPort(serverPort)
        
        #win32api.MessageBox(win32con.NULL, 'start', 'hello', win32con.MB_OK) 
        
    def infoBtn(self,event):
        logo_image = wx.Image("image/info_logo2.jpg",wx.BITMAP_TYPE_JPEG)
        info_frame = childFrame(image=logo_image,title="About Poodah")
        info_frame.Show()
        #win32api.MessageBox(win32con.NULL,'Information', 'hello',win32con.MB_OK)

#conductEvent
    def dequeue(self):
        if not len(self.msgList):
            return None 
        currentElement = self.msgList[0]
        del self.msgList[0]
        return currentElement
    def enqueue(self,value):
        self.msgList.append(value)
#         self.messageListbox.Append(value)
        tmplist = self.keymapType(value)
        if tmplist == None:
            return False
        self.messageListbox.InsertItems(tmplist,1)
        self.messageListbox.SetSelection(1)
        return True
    def keymapType(self,keytype):
        keymapcontent = {  '21' : 'VolumeUp',
                           '22' : 'VolumeDown',
                           '23' : 'VolumeMute',
                           '27' : 'ESCAPE',
                           '32' : 'SPACE',
                           '37' : 'left Arrow',
                           '38' : 'UP Arrow',
                           '39' : 'Right Arrow',
                           '40' : 'Down Arrow',
                           '45' : 'F5',
                           '65' : 'game   Left',
                           '68' : 'game   Right',
                           '73' : 'game   A',
                           '74' : 'game   B',
                           '75' : 'game   X',
                           '77' : 'game   LB',
                           '78' : 'game   RB',
                           '83' : 'game   Down',
                           '85' : 'game   Y',
                           '87' : 'game   Up'
                            }
        content = keymapcontent.get(keytype)
        if content == None:
            return None
        return [keytype+'  '+content]
        
    def keyEventMap(self,keytype):
        
        keymap = { '21' : 175,          #VolumeUp
                   '22' : 174,          #VolumeDown
                   '23' : 173,          #VolumeMute
                   '27' : 27,           #ESCAPE
                   '32' : 32,           #SPACE
                   '37' : 37,           #left Arrow
                   '38' : 40,           #UP Arrow
                   '39' : 39,           #Right Arrow
                   '40' : 38,           #Down Arrow
                   '45' : 116,          #F5
                   '65' : 65,           #game   Left
                   '68' : 68,           #game   Right
                   '73' : 73,           #game   A
                   '74' : 74,           #game   B
                   '75' : 75,           #game   X
                   '77' : 77,           #game   LB
                   '78' : 78,           #game   RB
                   '83' : 83,           #game   Down
                   '85' : 85,           #game   Y
                   '87' : 87            #game   Up
                    }
        key = keymap.get(keytype)
        if key == None:
            return None
        win32api.keybd_event(key,0,0,0)
        win32api.Sleep(45)
        win32api.keybd_event(key,0,win32con.KEYEVENTF_KEYUP,0)
#     win32api.keybd_event(38,0,0,0)
#     win32api.keybd_event(38,0,win32con.KEYEVENTF_KEYUP,0)
#     win32api.keybd_event(13,0,0,0)
#     win32api.keybd_event(13,0,win32con.KEYEVENTF_KEYUP,0)

#socket
    def setPort(self,port):
        self.port = port
    def getPort(self):
        return self.port
    def getMsg(self):
        if not len(self.msgList):
            return None
        firstMsg = self.msgList
        del self.msgList[0]
        return firstMsg
    def startSocket(self):
        self.listen()
        #self.lsn()
        
    def close(self):
        self.socket.close()
        
    def getSelfSocketname(self):
        return (self.ip,self.port)
    
    def listen(self):
        self.socket.bind((self.ip,self.port))
        self.socket.listen(50)
        con,addr = self.socket.accept()
        con.settimeout(60000)
        while True:
            try:
                msg = con.recv(2)
                if  msg.startswith("st") and not self.isConnect:
                    self.isConnect = True
                    continue
                if not self.isStartServer:
                    continue     
                if msg.isdigit() :
                    #if 10<int(msg)<100:
                    self.keyEventMap(msg)
                    tmplist = self.keymapType(msg)
                    self.messageListbox.InsertItems(tmplist,1)
                    self.messageListbox.SetSelection(1)
                    #self.enqueue(msg)     
            except:pass
    def lsn(self):
#         for i in range(10):
#             self.enqueue("175")
#             win32api.Sleep(100)
        #self.enqueue("173")
        for i in range(100):
            win32api.Sleep(1000)
            if self.isStartServer:
                self.enqueue("38")
                self.enqueue("65")
                
#thread 
    def conductThread(self):
        while True:
            if not len(self.msgList):
                win32api.Sleep(100)
                #print "empty"
                continue
            keytype = self.dequeue()
            if keytype != None:
                self.keyEventMap(keytype)  
    def recvMsgThread(self):
        self.startSocket()

    

    
def main():
    app=MyApp();
    app.MainLoop()
    
 
if __name__ == '__main__':
    main()
