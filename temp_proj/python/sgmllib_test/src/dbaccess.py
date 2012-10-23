
import sqlite3 as sql

def table_create(conn):
    str = 'CREATE TABLE Word'
    
    
def db_create(file):
    conn = sql.connect(file)
    table_create(conn)
    conn.close()
    
    return conn