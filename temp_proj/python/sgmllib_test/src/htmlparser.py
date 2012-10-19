# !/usr/bin/python
# coding:utf-8

import string
from HTMLParser import HTMLParser

class MyParser(HTMLParser):

    result = 0
    levelField = -1
    levelMeaning = -1
    pos = -1
    
#str = 'abandon = <C><E>abandons|abandoned|abandoning</E><F><H><M>a·ban·don || ə\'bændən</M></H><I><N><U>n.</U>  放纵, 放任; 狂热</N></I><I><N><U>v.</U>  丢弃; 中止, 放弃; 遗弃, 抛弃; 使放纵</N></I></F></C>'
        
    def handle_starttag(self, tag, attrs):
        if tag == 'e':
            self.pos = 1
        elif tag == 'f':
            self.result.field.append(DictField())
            self.levelField += 1
            self.pos = 2
        elif tag == 'h' and self.pos == 2:
            self.pos = 3
        elif tag == 'm' and self.pos == 3:
            self.pos = 4
        elif tag == 'i' and self.pos == 2:
            self.result.field[self.levelField].meaning.append(DictMeaning())
            self.levelMeaning += 1
            self.pos = 5
        elif tag == 'n' and self.pos == 5:
            self.pos = 6
        elif tag == 'u' and self.pos == 6:
            self.pos = 7
    
    def handle_endtag(self, tag):
        if self.pos == 4:
            self.pos = 3
        elif self.pos == 3:
            self.pos = 2
        elif self.pos == 5:
            self.pos = 2
        elif self.pos == 2:
            self.levelField -= 1
            self.pos = -1
        elif self.pos == 6:
            self.pos = 5
        elif self.pos == 7:
            self.pos = 6
        elif self.pos == 5:
            self.levelMeaning -= 1
            self.pos = 2
        else:
            self.pos = -1
        
    def handle_data(self, data):
        if self.pos == 1:
            self.result.extend = data
        elif self.pos == 4:
            self.result.field[self.levelField].symbol = data
        elif self.pos == 5:
            self.result.field[self.levelField].meaning[self.levelMeaning].category = data
        elif self.pos == 6:
            self.result.field[self.levelField].meaning[self.levelMeaning].meaning = data
            
    
    def parse(self, html, data):
        self.result = data
        self.feed(html)
            
        
class DictMeaning:
    category = ''
    meaning = ''
    
    def __str__(self):
        return 'category = %s meaning = %s' % (self.meaning, self.meaning)

class DictField:
    symbol = ''
    meaning = []
    
    def __str__(self):
        return 'symbol = %s meaning = %s' % (self.symbol, string.join(map(str, self.meaning)))

class DictData:
    word = ''
    extend = ''
    field = []
    
    def __str__(self):
        return 'word = %s extend = %s field = %s' % (self.word, self.extend, string.join(map(str, self.field)))

def parseHtml(html, output):
    parser = MyParser()
    parser.parse(html, output)
    parser.close()
    
    print output
    
    
def analyseLine(str, output):
    pos = str.find(' =')
    output.word = str[:pos]
    html = str[pos + 3 :]
#    print 'html=', html
    parseHtml(html, output)

def main():
    str = 'abandon = <C><E>abandons|abandoned|abandoning</E><F><H><M>a·ban·don || ə\'bændən</M></H><I><N><U>n.</U>  放纵, 放任; 狂热</N></I><I><N><U>v.</U>  丢弃; 中止, 放弃; 遗弃, 抛弃; 使放纵</N></I></F></C>'
    data = DictData()
    analyseLine(str, data)

main()


#        
#class data:
#    id = 0
#    value = 'value'
#    
#def list_add(l):
#    d = data()
#    d.id = 10
#    d.value = '1'
#    
#    l.append(d)
#        
#def t(i):
#    i = 10        
#
#list = [data(), data()]    
#
#str = 'abandon = <C><E>abandons|abandoned|abandoning</E><F><H><M>a·ban·don || ə\'bændən</M></H><I><N><U>n.</U>  放纵, 放任; 狂热</N></I><I><N><U>v.</U>  丢弃; 中止, 放弃; 遗弃, 抛弃; 使放纵</N></I></F></C>'
#parser = MyParser()
#parser.feed(str)
#parser.close();
#
#print list[0].id, list[0].value
#
#list_add(list)
#
#print list[-1].id, list[-1].value
#
#m = 1
#print m
#t(m)
#print m