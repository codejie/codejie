# !/usr/bin/python
# coding:utf-8


import string

import htmlparser
import data2xml


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
    i = 0
    for line in file:
        print line
        data = htmlparser.DictData()
        htmlparser.analyseLine(string.rstrip(line,  '\n'), data)
        print 'data ===== ', data
    
        print data2xml.data2xml(data) + '\n'
        
        if i > 10:
            break
        i += 1
#        print ret + 
#        break   
         
    file.close()

#main()
test()    

