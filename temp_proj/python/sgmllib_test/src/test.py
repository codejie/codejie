#!/usr/bin/python

import string

l = []

class A:
    v = []
    
    def __init__(self):
        self.b = []
    
    def __str__(self):
        return string.join(map(str, self.v))
    
l.append(A())
l[0].v.append(1)
l[0].v.append(2)
l.append(A())
l[1].v.append(3)

l.append(A())
l[2].b.append(1)
l[2].b.append(2)
l.append(A())
l[3].b.append(6)


print l[0].v
print l[1].v

print l[2].b
print l[3].b