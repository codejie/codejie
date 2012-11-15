# !/usr/bin/python
# coding:utf-8

import string
from HTMLParser import HTMLParser

class MyParser(HTMLParser):

    result = 0
    levelField = -1
    levelInfo = -1
    flag = -1    
    
#str = 'abandon = <C><E>abandons|abandoned|abandoning</E><F><H><M>a·ban·don || ə\'bændən</M></H><I><N><U>n.</U>  放纵, 放任; 狂热</N></I><I><N><U>v.</U>  丢弃; 中止, 放弃; 遗弃, 抛弃; 使放纵</N></I></F></C>'
        
    def handle_starttag(self, tag, attrs):
        if tag == 'c':
            self.flag = 0 #content
        elif tag == 'e':
            self.flag = 1 #extend
        elif tag == 'f':
            self.result.field.append(DictField())
            self.levelField += 1            
            self.levelInfo = -1
#            print 'levelField =', self.levelField
            self.flag = 2 #field
        elif tag == 'l':
            self.flag = 3 #link
        elif tag == 'm':
            self.flag = 4 #symbol
        elif tag == 'i':
            self.result.field[self.levelField].info.append(DictInfo())
            self.levelInfo += 1
#            print 'info == levelField = %s levelInfo = %s' % (self.levelField, self.levelInfo)
            self.flag = 5 #info
        elif tag == 'n':        
            self.flag = 6 #meaning
        elif tag == 'u':
            self.flag = 7 #category        
 
    def handle_endtag(self, tag):
        if tag == 'u':
            self.flag = 6 #meaning        
        
    def handle_data(self, data):
        
        index = self.levelField
        
        if self.flag == 1:
            self.result.extend.append(data)
        elif self.flag == 3:
            self.result.field[self.levelField].link = data
        elif self.flag == 4:
            self.result.field[self.levelField].symbol = data
        elif self.flag == 6:
#           print 'meaning == index = %d' % index
            self.result.field[self.levelField].info[self.levelInfo].meaning = data
#            print 'meaning == levelField=%d levelInfo=%d' % (self.levelField, self.levelInfo)
#            print 'meaning == info: %s' % self.result.field[self.levelField].info[self.levelInfo]
        elif self.flag == 7:
#            print 'category == levelInfo = %d' % self.levelInfo 
            self.result.field[self.levelField].info[self.levelInfo].category = data
#            print 'category == levelField=%d levelInfo=%d' % (self.levelField, self.levelInfo)
#            print 'category == info: %s' % self.result.field[self.levelField].info[self.levelInfo]           
    
    def parse(self, html, data):
#        self.levelField = -1
#        self.levelInfo = -1
#        self.flag = -1  
                
        self.result = data
        self.feed(html)
            
        
class DictInfo:
    category = ''
    meaning = ''
    
    def __str__(self):
        return '[category = %s meaning = %s]' % (self.category, self.meaning)

class DictField:
    symbol = ''
    link = ''
#    info = [DictInfo() for i in range(0,25)]
    
    def __init__(self):
        self.info = []
    
    def __str__(self):
        return '[symbol = %s | link = %s info = %s]' % (self.symbol, self.link, string.join(map(str, self.info)))
 
class DictData:
    word = ''
    extend = [] #stringlist
    field = []   
    
    def __str__(self):
        return 'word = %s extend = %s field = %s' % (self.word, string.join(map(str, self.extend)), string.join(map(str, self.field)))

def parseHtml(html, output):
    parser = MyParser()
    parser.parse(html, output)
    parser.close()
   
    
def analyseLine(str, output):
    pos = str.find(' =')
    output.word = str[:pos]
    html = str[pos + 3 :]
#    print 'html=', html
    parseHtml(html, output)

