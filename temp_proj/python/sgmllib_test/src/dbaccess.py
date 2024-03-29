#!/usr/bin/python
# coding:utf-8

import sqlite3 as sqlite
import re


def table_create(conn):
    cursor = conn.cursor()
    
    sql = [
           'CREATE TABLE IF NOT EXISTS Word (wordid INTEGER SECOND KEY, word TEXT PRIMARY KEY, flag INTEGER)',
           'CREATE TABLE IF NOT EXISTS Src (srcid INTEGER PRIMARY KEY, wordid INTEGER, fmt INTEGER, orig INTEGER, content TEXT)',
           'CREATE TABLE IF NOT EXISTS Dict (dictid INTEGER PRIMARY KEY, title TEXT)',
           'CREATE TABLE IF NOT EXISTS Word2 (wordid INTEGER PRIMARY KEY, word TEXT, flag INTEGER, orig INTEGER, fmt INTEGER)',
           'CREATE TABLE IF NOT EXISTS ViconIndex (wordid INTEGER, position INTEGER, size INTEGER)'
           ]
    for s in sql:
        cursor.execute(s)

        
def add_dict(conn, title):
    cursor = conn.cursor()    
    cursor.execute('INSERT INTO Dict (title) VALUES (\'%s\')' % title)    
    conn.commit()        
        
def add_record(conn, cursor, word, record):
#    sql.encode('string_scape')
    cursor.execute('INSERT INTO Word (word, flag) VALUES ("%s",1)' % (word))#'INSERT INTO Word (word, flag) VALUES (\'%s\',1)' % (word))
    record = record.replace('\"', '')
    cursor.execute('INSERT INTO Src (wordid, fmt, orig, content) VALUES (%d, 3, 1, "%s")' % (cursor.lastrowid, record))
#    conn.commit()
    
def add_word(conn, word, pos, len):
    cursor = conn.cursor()
    cursor.execute('INSERT INTO Word2 (word, flag, orig, fmt) VALUES ("%s",1, 1, 4)' % (word))#'INSERT INTO Word (word, flag) VALUES (\'%s\',1)' % (word))
    cursor.execute('INSERT INTO ViconIndex (wordid, position, size) VALUES (%d, %d, %d)' % (cursor.lastrowid, pos, len))   
    
    
def db_create(dbfile):
    return sqlite.connect(dbfile)

def db_close(conn):
    conn.commit();
    conn.close()
    
def db_test(conn):
    cursor = conn.cursor()
    record = '"1234"'
    record.replace('\"', '')
    cursor.execute('INSERT INTO Word (word, flag) VALUES ("%s", 1)' % (record))
    conn.commit()
         

#######################################3

#conn = db_create('../data/lac.db')


#add_dict(conn, 'test')

#db_close(conn)
    

