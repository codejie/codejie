# !/usr/bin/python
# encoding: utf-8

from sgmllib import SGMLParser

class parser(SGMLParser):
    flag = -1
    def unknown_starttag(self, tag, attributes):
        if tag == 'm':
            flag = 0
        elif tag == 'u':
            print tag
        
    def unknown_endtag(self, tag):
        print tag
        
    def handle_data(self, data):
        print data
        
#               
        
def main():
#    str = u'abut = <C><E>abuts|abutted|abutting</E><F><H><M>a·but || ə\'bʌt</M></H><I><N><U>v.</U>  邻接; 紧靠; 毗连</N></I></F></C>'
    str = 'abandon = <C><E>abandons|abandoned|abandoning</E><F><H><M>a·ban·don || ə\'bændən</M></H><I><N><U>n.</U>  放纵, 放任; 狂热</N></I><I><N><U>v.</U>  丢弃; 中止, 放弃; 遗弃, 抛弃; 使放纵</N></I></F></C>'
    parser().feed(str)
    
main()              