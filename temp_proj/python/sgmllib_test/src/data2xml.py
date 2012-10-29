
import string

#<X>
#<W>Word</W>
#<D>dictid</D>
#<E>E1</E>
#<E>E2</E>
#<F>
#<S>Symbol</S>
#<L>Link</L>
#<I>
#<C>category</C>
#<M>Meaning</M>
#</I>
#<I>
#<C>category</C>
#<M>Meaning</M>
#</I>
#</F>
#<F>
#<S>Symbol</S>
#<L>Link</L>
#<I>
#<C>category</C>
#<M>Meaning</M>
#</I>
#</F>
#</X>

def addtag(list, stag, etag):
    if len(list) > 0:
        ret = ''
        for data in list:
            ret = stag + string.strip(data, ' ') + etag
        return ret
    else:
        return ''

def addExtend(extend):
    return addtag(extend, '<e>', '</e>')

def addInfo(info, index):
    if len(info) > 0:
        ret = ''
        for i in info[index * 5:(index + 1) * 5]:
            if i.category == '' and i.meaning == '':
                break
            ret += '<i>'
            if i.category != '':
                ret += '<c>' +  string.strip(i.category, ' ') + '</c>'
            if i.meaning != '':
                ret += '<m>' +  string.strip(i.meaning, ' ') + '</m>'
            ret += '</i>'
        return ret
    else:
        return ''

def addSubField(f, index):
    ret = ''
    if f.symbol != '':
        ret += '<s>' + string.strip(f.symbol, ' ') + '</s>'
    if f.link != '':
        ret += '<l>' +  string.strip(f.link, ' ') + '/l>'
    ret += addInfo(f.info, index)
    return ret 

def addField(field):
    index = 0
    if len(field) > 0:
        ret = '<f>'
        for f in field:
            ret += addSubField(f, index)
            index += 1
        ret += '</f>'
        return ret
    else:
        return ''
    
    

def data2xml(data):
    ret = '<x><w>' +  string.strip(data.word, ' ') + '</w>' \
        + '<d>1</d>' \
        + addExtend(data.extend) \
        + addField(data.field) \
        + "</x>"
    return ret


