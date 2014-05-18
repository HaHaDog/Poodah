#encoding:utf-8
'''
Created on 2013��11��1��

@author: yangluo
'''
#import pygame
import wx
import os
#import win32api
#import win32con
#import thread
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
        self.money_type_choose = 0
#         self.clientNum = 0

        #conductEvent
        #self.messageQueue = []

        #create thread
        #self.keythread_handle = threading.Thread(target=self.conductThread)


        #self.keythread_handle.start()

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

        sampleList = ['paper money', 'coins']
#         ST_IP = wx.Image("image/st_ip.jpg",wx.BITMAP_TYPE_JPEG).ConvertToBitmap()
#         ST_Port = wx.Image("image/st_port.jpg",wx.BITMAP_TYPE_JPEG).ConvertToBitmap()
#         ST_Msglist = wx.Image("image/st_messagelist.jpg",wx.BITMAP_TYPE_JPEG).ConvertToBitmap()
        
        self.moneyType =  wx.RadioBox(
                 bkg, -1, "money type", wx.DefaultPosition, wx.DefaultSize,
                 sampleList, 1, wx.RA_SPECIFY_COLS
                 )
        self.Bind(wx.EVT_RADIOBOX, self.EvtRadioBox, self.moneyType)


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

#         self.infoButton = wx.BitmapButton(bkg,-1,self.info_common,size=startbutton_size)
#         self.infoButton.Bind(wx.EVT_BUTTON,self.infoBtn)
#         self.infoButton.SetBitmapSelected(self.info_press)
#         self.infoButton.SetBitmapFocus(self.info_focus)

        #ST~~
#         ST_IP_text = wx.StaticBitmap(bkg,-1,  ST_IP)
#         ST_Port_name = wx.StaticBitmap(bkg,-1, ST_Port)
#         ST_Message = wx.StaticBitmap(bkg,-1, ST_Msglist)

        ST_IP_text = wx.StaticText(bkg,-1,"type:")
        ST_Port_name = wx.StaticText(bkg,-1,"num:")
        #ST_Message =wx.StaticText(bkg,-1,"Message List")
        
        
        self.IP_text = wx.TextCtrl(bkg)
        self.Port_name  = wx.TextCtrl(bkg)
        
        
        self.IP_text.Bind(wx.EVT_TEXT, self.OncheckType)
        self.Port_name.Bind(wx.EVT_TEXT, self.OncheckNum)
        self.IP_text.SetEditable(True)
        self.Port_name.SetEditable(True)
        #ST_IP_text.SetFocus()

        self.IP_text.SetOwnForegroundColour(text_colour)
        self.Port_name.SetOwnForegroundColour(text_colour)
#         self.IP_text.SetInitialSize((,30))

        self.IP_text.SetTransparent(254)
        self.Port_name.SetBackgroundColour(frame_colour)
        
        self.input_button = wx.Button(bkg,-1,"input")

        #ST_clientNum = wx.StaticText(bkg,-1,"clientnum:")
#         self.client_text = wx.TextCtrl(bkg)


        self.messageListbox = wx.ListBox(bkg,style = wx.TE_MULTILINE | wx.HSCROLL)
        self.messageListbox.SetForegroundColour(text_colour)
        self.messageListbox.SetFont(self.listFont)

        listItem = ['Food List:']
        self.messageListbox.InsertItems(listItem,0)
#         for i in range(10):
#             self.messageListbox.Append("values%d"%i)
        hbox1 = wx.BoxSizer()
        hbox1.Add(self.moneyType,proportion=1, flag = wx.ALIGN_CENTER)
        hbox1_tmp = wx.BoxSizer()
        hbox1_tmp.Add(ST_IP_text, proportion=0, flag=wx.ALIGN_CENTER, border=5)
        hbox1_tmp.Add(self.IP_text, proportion=1, flag = wx.ALIGN_CENTER)
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
    
        vbox_tmp = wx.BoxSizer(wx.VERTICAL)
        vbox_tmp.Add(hbox1_tmp,proportion=0,flag=wx.ALIGN_CENTER)
        vbox_tmp.Add(hbox2,proportion=0,flag=wx.ALIGN_CENTER)
        
        uphbox = wx.BoxSizer()
        uphbox.Add(hbox1,proportion=0, flag=wx.ALIGN_CENTER)
        uphbox.Add(vbox_tmp,proportion=1,flag=wx.ALIGN_CENTER)
        uphbox.Add(self.input_button,proportion=0,flag= wx.ALIGN_CENTER)
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
        
        #vbox1.Add(self.infoButton,proportion=0,flag=wx.ALIGN_CENTER)
        
        
        #list
        hbox4 = wx.BoxSizer()
       # hbox4.Add(ST_Message,proportion=1,flag=wx.ALIGN_CENTER)

        vbox2 = wx.BoxSizer(wx.VERTICAL)
        vbox2.Add(hbox4,proportion=0,flag=wx.EXPAND|wx.ALL,border=5)
        vbox2.Add(self.messageListbox, proportion=1, flag=wx.EXPAND|wx.LEFT|wx.BOTTOM|wx.RIGHT, border=5)

        hbox5 = wx.BoxSizer()
        hbox5.Add(vbox2,proportion=1,flag=wx.EXPAND|wx.ALL,border=5)
        hbox5.Add(vbox1,proportion=0,flag=wx.EXPAND|wx.ALL,border=5)


#       aa  hbox3.Add(self.infoButton,proportion=0,flag=wx.ALIGN_CENTER,border=5)

        vbox3 = wx.BoxSizer(wx.VERTICAL)
        vbox3.Add(uphbox,proportion=0, flag=wx.EXPAND|wx.ALL,border=5)
        #vbox3.Add(hbox2,proportion=0,flag = wx.EXPAND|wx.ALL,border=5)
        vbox3.Add(hbox5,proportion=1,flag=wx.EXPAND|wx.ALL,border=5)



        bkg.SetSizer(vbox3)
        self.win.Show()
        #self.SetTopWindow(self.win)

        return True
    
    def OncheckType(self,event):
        try:
            content = self.IP_text.GetValue()
            isint = False
            isint = type(eval(content)) == int
            i_content = int(content) 
        except:
            pass
        if content =="":
            return
        if self.money_type_choose == 1 :
            self.IP_text.SetValue("1")
            self.IP_text.setv
            return
        
        if not isint:
            dlg = wx.MessageDialog(None,"please input a num","warning",style=wx.OK,pos=wx.DefaultPosition)
            dlg.ShowModal()
            self.hour_text.SetSelection(0,5)
            #self.hour_text.SetValue("")
        else:
            #if (i_content == 1) or (i_content==5) or i_content==10 or i_content==20 or i_content==50 or i_content=100 :
            if int(content)==1 or int(content)==5 or int(content)==10 or int(content)==20 or int(content)==50 or int(content)==100:
                return
            else:
                dlg = wx.MessageDialog(None,"type must 1,5,10,20,50,100","warning",style=wx.OK,pos=wx.DefaultPosition)
                dlg.ShowModal()
                self.hour_text.SetSelection(0,5)

    def OncheckNum(self,event):
        try:
            content = self.Port_name.GetValue()
            isint = False
            isint = type(eval(content)) == int
        except:
            pass
        if content =="":
            return
        if not isint:
            dlg = wx.MessageDialog(None,"please input a num","warning",style=wx.OK,pos=wx.DefaultPosition)
            dlg.ShowModal()
            self.hour_text.SetSelection(0,5)
            #self.hour_text.SetValue("")
            
        else:
            if int(content)<0 or int(content)>24:
                dlg = wx.MessageDialog(None,"Time seems too long~~","warning",style=wx.OK,pos=wx.DefaultPosition)
                dlg.ShowModal()
                self.hour_text.SetSelection(0,5)
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


        #win32api.MessageBox(win32con.NULL, 'start', 'hello', win32con.MB_OK)

    def infoBtn(self,event):
        logo_image = wx.Image("image/info_logo2.jpg",wx.BITMAP_TYPE_JPEG)
        info_frame = childFrame(image=logo_image,title="About Poodah")
        info_frame.Show()
        #win32api.MessageBox(win32con.NULL,'Information', 'hello',win32con.MB_OK)
    def EvtRadioBox(self, event):
        self.money_type_choose = event.GetInt()



def main():
    app=MyApp();
    app.MainLoop()


if __name__ == '__main__':
    main()
