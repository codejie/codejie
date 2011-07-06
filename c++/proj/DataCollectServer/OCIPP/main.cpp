
#include <string>
#include <iostream>

#include "ocipp.h"

int test()
{
    try
    {
        ocipp::Environment env;
    
        ocipp::Connection* conn = env.makeConnection("scott", "tiger", "QA");
		ocipp::Statement *stmt = conn->makeStatement("select TNAME, TABTYPE from tab where TNAME=:1");
        std::string name, type;
        stmt->defineString(1, name);
		stmt->defineString(2, type);
		std::string bind = "EMP";
		stmt->bindString(1, bind);
        stmt->execute();
        while(stmt->getNext() == 0)
        {
            std::cout << "\n" << name << " - " << type;
        }
        std::cout << std::endl;
        
        conn->destroyStatement(stmt);
        env.destroyConnection(conn);
    }
    catch (const ocipp::Exception& e)
    {
        std::cout << "\nException - " << e << std::endl;
        return -1;
    }

    return 0;
}

int test1()
{
    try
    {
        ocipp::Environment env;
    
        ocipp::Connection* conn = env.makeConnection("scott", "tiger", "QA");
		for(int i = 0; i < 10; ++ i)
		{
			ocipp::Statement *stmt = conn->makeStatement("INSERT INTO TEST (NAME, TITLE) VALUES (:1, :2)");
			std::string name = "myname";
			std::string title = "mytitle";
			stmt->bindString(1, name);
			stmt->bindString(2, title);
			stmt->execute();
	        
			conn->destroyStatement(stmt);
		}


		{
			ocipp::Statement *stmt = conn->makeStatement("DELETE FROM TEST");
			std::string name = "'%'";
			std::string title = "mytitle";
			//stmt->bindString(1, name);
			//stmt->bindString(2, title);
			stmt->execute();
			conn->destroyStatement(stmt);
		}
/*
		{
			ocipp::Statement *stmt = conn->makeStatement("UPDATE TEST SET NAME='9999' WHERE NAME like '%'");
			std::string name = "myname";
			std::string title = "mytitle";
			//stmt->bindString(1, name);
			//stmt->bindString(2, title);
			stmt->execute();
			conn->destroyStatement(stmt);
		}
*/
        env.destroyConnection(conn);
    }
    catch (const ocipp::Exception& e)
    {
        std::cout << "\nException - " << e << std::endl;
        return -1;
    }

    return 0;
}

int main()
{
	//test();
    test1();
    return 0;
}