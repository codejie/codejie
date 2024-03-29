# !/usr/bin/python
# coding:utf-8


import string

import htmlparser
import data2xml
import dbaccess
import fileaccess


def test():
#    str = 'a = <C><F><H><M>e?;?</M></H><I><N>字母A</N></I></F></C>'
#    str = 'A A = <C><E>A A</E><F><H><L>AA (Alcoholics Anonymous)</L></H><I><N>嗜酒者互诫协会</N></I></F><E>A A</E><F><H><L>AA (anti-aircraft)</L></H><I><N>aa, 抵抗飞机, 为保护不受敌人的飞机和直升机伤害</N></I></F></C>'
    str = 'test = <c><f><i><u>9</u></i><i><u>2</u></i></f><f><i><u>3</u></i></f>'
    data = htmlparser.DictData()
    htmlparser.analyseLine(str, data)
    print data
    print data2xml.data2xml(data) + '\n'

def main():
#    str = 'abandon = <C><E>abandons|abandoned|abandoning</E><F><H><M>a·ban·don || ə\'bændən</M></H><I><N><U>n.</U>  放纵, 放任; 狂热</N></I><I><N><U>v.</U>  丢弃; 中止, 放弃; 遗弃, 抛弃; 使放纵</N></I></F></C>'
#    str = 'a = <C><F><H><M>e?;?</M></H><I><N>字母A</N></I></F></C>'
#    str = 'AAA = <C><F><H><L>AAA (American Automobile Association)</L></H><I><N>美国汽车协会, 给在会会员提供紧急通路服务 (牵引等) 和其它汽车服务 (地图, 导游等) 的组织</N></I></F><E>A A A</E><F><H><L>AAA (antiaircraft artillery)</L></H><I><N>对空作战方法 (使用大炮或导弹)</N></I></F><E>A A A</E><F><H><L>AAA (Amateur Athletic Association)</L></H><I><N>业余体育协会, 监督非职业体育活动的组织</N></I></F></C>'
#    str = 'AB = <C><F><H><L>AB (air base)</L></H><I><N>ab, 空军基地, 空军的军事基地, 空军训练和操作的中心之一</N></I></F></C>'
#    str = 'test = <c><E>1</E><E>2</E>'
    
    file = open("../data/output.txt", "r")
    conn = dbaccess.db_create("../data/lac.db3")
    dbaccess.table_create(conn)
#    dbaccess.db_test(conn)
#    return
    dbaccess.add_dict(conn, 'Vicon English-Chinese(S) Dictionary')
    cursor = conn.cursor()
    i = 0
    for line in file:
#        print line
        data = htmlparser.DictData()
        htmlparser.analyseLine(string.rstrip(line,  '\n'), data)
#        print 'data ===== ', data      
        
#        print data2xml.data2xml(data)
        dbaccess.add_record(conn, cursor, data.word, data2xml.data2xml(data))
        
#        if i > 10:
#            break
        i += 1
        if i % 100  == 0:
            print 'i =', i 
#        print ret + 
#        break   

    dbaccess.db_close(conn)
    file.close()

def mainToFile():
    dfile = open('../data/output.txt', 'r')
    vfile = fileaccess.openfile('../data/vicondata.db')
    conn = dbaccess.db_create('../data/lac.db3')
    dbaccess.table_create(conn)
    
    dbaccess.add_dict(conn, 'Vicon English-Chinese(S) Dictionary')
    
    i = 0
    pos = 0
    size = 0
    for line in dfile:
        data = htmlparser.DictData()
        htmlparser.analyseLine(string.rstrip(line, '\n'), data)
        size = fileaccess.push2file(vfile, data2xml.data2xml(data)) - pos
        dbaccess.add_word(conn, data.word, pos, size)
        pos += size
        
#        if i > 10:
#            break        
        i += 1
        if i % 100  == 0:
            print 'i =', i         
    
    dbaccess.db_close(conn)
    vfile.close()
    dfile.close()

#main()
#test()
mainToFile()    

