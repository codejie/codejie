#!/usr/bin/python

import string
import array

l = []

class A:
    v = []
    
    def __str__(self):
        return string.join(map(str, self.v))
    
l.append(A())
l[0].v.append(1)
l[0].v.append(2)
l.append(A())
l[1].v.append(3)


print l[0]
print l[1]