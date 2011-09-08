
#include <string>
#include <iostream>
#include <vector>

#include "oci.h"

namespace ocipp
{

class Exception
{
public:
    Exception(int code, const std::string& msg);
    Exception(int code, OCIError *err, const std::string& msg);

    void show(std::ostream& os) const;
protected:
    void getError(int code);
    void getError(int code, OCIError *err);
protected:
    int _code;
    std::string _msg;
};

class Connection;

class Environment
{
public:
    Environment(unsigned int mode = OCI_DEFAULT);
    virtual ~Environment();

    Connection* makeConnection(const std::string& user, const std::string& passwd, const std::string& server = "");
    void destroyConnection(Connection* conn);

    OCIEnv *getEnv() { return _env; }
    OCIError *getError() { return _err; }
private:
    void makeEnvironment(unsigned int mode);
protected:
    OCIEnv *_env;
    OCIError *_err;
};

class Statement;

class Connection
{
    friend class Environment;
protected:
    Connection(Environment* env);
    virtual ~Connection() {}
public:
    Statement *makeStatement(const std::string& sql);
    void destroyStatement(Statement* stmt);

    Environment *getEnvironment() { return _env; }

    OCISvcCtx *getSvc() { return _svc; }
private:
    Environment *_env;
protected:
    OCIServer *_srv;
    OCISvcCtx *_svc;
    OCISession *_auth;
};

class Statement
{
    friend class Connection;
protected:
	static const size_t BUF_SIZE = 256;
	struct TDefData
	{
		TDefData(std::string& val)
			: str(&val), buf(NULL)
		{
			buf = new char[BUF_SIZE];
		}

		std::string* str;
		char* buf;
	};
	typedef std::vector<TDefData> TDefVector;
protected:
    Statement(Connection* conn);
	virtual ~Statement() {}

	void syncDefVector();
	void freeDefVector();
public:
    int bindString(unsigned int pos, const std::string& val);
	int defineString(unsigned int pos, std::string& val);

    int execute();
    int getNext();
private:
    Connection *_conn;
protected:
    OCIStmt *_stmt;

	TDefVector _vctDef;
};

}

std::ostream& operator << (std::ostream& os, const ocipp::Exception& e);