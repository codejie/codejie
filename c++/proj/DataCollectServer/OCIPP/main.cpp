
#include <string>
#include <iostream>

#include "ocipp.h"

int test()
{
    try
    {
        ocipp::Environment env;
    
        ocipp::Connection* conn = env.makeConnection("scott", "tiger", "QA");
        ocipp::Statement *stmt = conn->makeStatement("select NAME from tab");
        std::string name;
        stmt->defineString(1, name);
        stmt->execute();
        while(stmt->getNext() == 0)
        {
            std::cout << "\n" << name;
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

}

int main()
{
    test();
    return 0;
}