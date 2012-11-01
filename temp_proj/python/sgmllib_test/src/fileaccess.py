#!/usr/bin/python
# coding:utf-8


def openfile(filename):
    return open(filename, "wb")

def closefile(f):
    f.close()

def push2file(f, record):
    f.write(record)
    return f.tell()


f = openfile('../data/a.d')

push2file(f, 'fdkfjdkfjdklfjkdf\'s')

closefile(f)