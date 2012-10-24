
import string

import htmlparser

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

def addInfo(info):
    if len(info) > 0:
        ret = ''
        for i in info:
            ret += '<i>'
            if i.category != '':
                ret += '<c>' + i.category + '</c>'
            if i.meaning != '':
                ret += '<m>' + i.meaning + '</m>'
                ret += '</i>'
        return ret
    else:
        return ''

def addSubField(f):
    ret = ''
    if f.symbol != '':
        ret += '<s>' + string.strip(f.symbol, '') + '</s>'
    if f.link != '':
        ret += '<l>' + f.link + '/l>'
    ret += addInfo(f.info)
    return ret 

def addField(field):
    if len(field) > 0:
        ret = '<f>'
        for f in field:
            ret += addSubField(f)
        ret += '</f>'
        return ret
    else:
        return ''
    
    

def data2xml(data):
    ret = '<w>' + data.word + '</w>' \
        + '<d>1</d>' \
        + addExtend(data.extend) \
        + addField(data.field)
    return ret


