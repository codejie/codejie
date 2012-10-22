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
            self.flag = 2 #field
        elif tag == 'l':
            self.flag = 3 #link
        elif tag == 'm':
            self.flag = 4 #symbol
        elif tag == 'i':
            self.result.field[self.levelField].info.append(DictInfo())
            self.levelInfo += 1
            self.flag = 5 #info
        elif tag == 'n':        
            self.flag = 6 #meaning
        elif tag == 'u':
            self.flag = 7 #category        
#        
#        if tag == 'e':
#            self.pos = 1
#        elif tag == 'f':
#            self.result.field.append(DictField())
#            self.levelField += 1
#            self.pos = 2
#        elif tag == 'h' and self.pos == 2:
#            self.pos = 3
#        elif tag == 'm' and self.pos == 3:
#            self.pos = 4
#        elif tag == 'i' and self.pos == 2:
#            self.result.field[self.levelField].meaning.append(DictMeaning())
#            self.levelMeaning += 1
#            self.pos = 5
#        elif tag == 'n' and self.pos == 5:
#            self.pos = 6
#        elif tag == 'u' and self.pos == 6:
#            self.pos = 7
    
    def handle_endtag(self, tag):
        if tag == 'u':
            self.flag = 6 #meaning        

#        if self.pos == 4:
#            self.pos = 3
#        elif self.pos == 3:
#            self.pos = 2
#        elif self.pos == 5:
#            self.pos = 2
#        elif self.pos == 2:
##            self.levelField -= 1
#            self.pos = -1
#        elif self.pos == 6:
#            self.pos = 5
#        elif self.pos == 7:
#            self.pos = 6
#        elif self.pos == 5:
##            self.levelMeaning -= 1
#            self.pos = 2
#        else:
#            self.pos = -1
        
    def handle_data(self, data):
        if self.flag == 1:
            self.result.extend.append(data)
        elif self.flag == 3:
            self.result.field[self.levelField].link = data
        elif self.flag == 4:
            self.result.field[self.levelField].symbol = data
        elif self.flag == 6:
            self.result.field[self.levelField].info[self.levelInfo].meaning = data
        elif self.flag == 7:
            self.result.field[self.levelField].info[self.levelInfo].category = data
            
#            
#        elif self.pos == 4:
#            self.result.field[self.levelField].symbol = data
#        elif self.pos == 7:
#            self.result.field[self.levelField].meaning[self.levelMeaning].category = data
#        elif self.pos == 6:
#            self.result.field[self.levelField].meaning[self.levelMeaning].meaning = data
            
    
    def parse(self, html, data):
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
    info = []
    
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
    
    print output
    
    
def analyseLine(str, output):
    pos = str.find(' =')
    output.word = str[:pos]
    html = str[pos + 3 :]
#    print 'html=', html
    parseHtml(html, output)

def main():
#    str = 'abandon = <C><E>abandons|abandoned|abandoning</E><F><H><M>a·ban·don || ə\'bændən</M></H><I><N><U>n.</U>  放纵, 放任; 狂热</N></I><I><N><U>v.</U>  丢弃; 中止, 放弃; 遗弃, 抛弃; 使放纵</N></I></F></C>'
    str = 'a = <C><F><H><M>e?;?</M></H><I><N>字母A</N></I></F></C>'
#    str = 'AAA = <C><F><H><L>AAA (American Automobile Association)</L></H><I><N>美国汽车协会, 给在会会员提供紧急通路服务 (牵引等) 和其它汽车服务 (地图, 导游等) 的组织</N></I></F><E>A A A</E><F><H><L>AAA (antiaircraft artillery)</L></H><I><N>对空作战方法 (使用大炮或导弹)</N></I></F><E>A A A</E><F><H><L>AAA (Amateur Athletic Association)</L></H><I><N>业余体育协会, 监督非职业体育活动的组织</N></I></F></C>'
#    str = 'AB = <C><F><H><L>AB (air base)</L></H><I><N>ab, 空军基地, 空军的军事基地, 空军训练和操作的中心之一</N></I></F></C>'
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