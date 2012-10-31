#!/usr/bin/python
# coding:utf-8

import sqlite3 as sqlite


def table_create(conn):
    cursor = conn.cursor()
    
    sql = [
           'CREATE TABLE Word (wordid INTEGER PRIMARY KEY, word TEXT, flag INTEGER)',
           'CREATE TABLE Src (srcid INTEGER PRIMARY KEY, wordid INTEGER, fmt INTEGER, orig INTEGER, content TEXT)',
           'CREATE TABLE Dict (dictid INTEGER PRIMARY KEY, title TEXT)'
           ]
    for s in sql:
        cursor.execute(s)

        
def add_dict(conn, title):
    cursor = conn.cursor()
    
    cursor.execute('INSERT INTO Dict (title) VALUES (\'%s\')' % title)
    
    conn.commit()        
        
def add_record(conn, word, record):
    cursor = conn.cursor()
    cursor.execute('INSERT INTO Word (word, flag) VALUES (\'%s\',1)' % (word))
    cursor.execute('INSERT INTO Src (wordid, fmt, orig, content) VALUES (%d, 3, 1, \'%s\')' % (cursor.lastrowid, record))
    conn.commit()
    
def db_create(dbfile):
    return sqlite.connect(dbfile)

def db_close(conn):
    conn.close()

#######################################3

#conn = db_create('../data/lac.db')


#add_dict(conn, 'test')

#db_close(conn)
    

