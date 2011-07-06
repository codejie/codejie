
#include "ocipp.h"

namespace ocipp
{

Exception::Exception(int code, const std::string &msg)
: _code(code)
, _msg(msg)
{
    getError(code);
}

Exception::Exception(int code, OCIError* err, const std::string &msg)
: _code(code)
, _msg(msg)
{
    getError(code, err);
}

void Exception::getError(int code)
{
    switch(code)
    {
    case OCI_SUCCESS:
        _msg = "(OCI_SUCCESS) - " + _msg;
        break;
    case OCI_SUCCESS_WITH_INFO:
        _msg = "(OCI_SUCCESS_WITH_INFO) - " + _msg;
        break;
    case OCI_NEED_DATA:
        _msg = "(OCI_NEED_DATA) - " + _msg;
        break;
    case OCI_NO_DATA:
        _msg = "(OCI_NODATA) - " + _msg;
        break;
    case OCI_ERROR:
        _msg = "(OCI_ERROR) - " + _msg;
        break;
    case OCI_INVALID_HANDLE:
        _msg = "(OCI_INVALID_HANDLE) - " + _msg;
        break;
    case OCI_STILL_EXECUTING:
        _msg = "(OCI_STILL_EXECUTE) - " + _msg;
        break;
    case OCI_CONTINUE:
        _msg = "(OCI_CONTINUE) - " + _msg;
        break;
    default:
        _msg = "(UNKNOWN) - " + _msg;
    }
}

void Exception::getError(int code, OCIError *err)
{
    getError(code);
    if(code == OCI_ERROR)
    {
        char buf[512];
        OCIErrorGet((void*)err, 1, NULL, &code, (OraText*)buf, sizeof(buf), OCI_HTYPE_ERROR);
        _msg += "::";
        _msg +=  buf;
    }
}

void Exception::show(std::ostream &os) const
{
    os << "[" << _code << "]" << _msg;
}

////

Environment::Environment(unsigned int mode)
: _env(NULL)
, _err(NULL)
{
    makeEnvironment(mode);    
}

Environment::~Environment()
{
    if(_err != NULL)
    {
        OCIHandleFree((void*)_err, OCI_HTYPE_ERROR);
        _err = NULL;
    }
    if(_env != NULL)
    {
        OCIHandleFree((void*)_env, OCI_HTYPE_ENV);
        _env = NULL;
    }
}

void Environment::makeEnvironment(unsigned int mode)
{
    int ret = OCIEnvCreate(&_env, mode, NULL, NULL, NULL, NULL, 0, NULL);
    if(ret != OCI_SUCCESS || _env == NULL)
    {
        throw Exception(ret, "create Environment failed.");
    }
    
    ret = OCIHandleAlloc((const void*)_env, (void**)&_err, OCI_HTYPE_ERROR, 0, NULL);
    if(ret != OCI_SUCCESS || _env == NULL)
    {
        throw Exception(ret, _err, "create Error failed.");
    }
}

Connection* Environment::makeConnection(const std::string &user, const std::string &passwd, const std::string &server)
{
    Connection *conn = new Connection(this);

    int ret = OCIHandleAlloc((const void*)_env, (void**)&(conn->_srv), OCI_HTYPE_SERVER, 0, NULL);
    if((ret != OCI_SUCCESS && ret != OCI_SUCCESS_WITH_INFO) || conn->_srv == NULL)
    {
        throw Exception(ret, "create Server failed.");
    }

    ret = OCIHandleAlloc((const void*)_env, (void**)&(conn->_svc), OCI_HTYPE_SVCCTX, 0, NULL);
    if(ret != OCI_SUCCESS || conn->_svc == NULL)
    {
        throw Exception(ret, "create ScvCtx failed.");
    }

    ret = OCIServerAttach(conn->_srv, _err, (const OraText*)server.c_str(), server.size(), 0);
    if(ret != OCI_SUCCESS)
    {
        throw Exception(ret, _err, "attach Server failed.");
    }

    ret = OCIAttrSet((void*)conn->_svc, OCI_HTYPE_SVCCTX, (void*)conn->_srv, 0, OCI_ATTR_SERVER, _err);
    if(ret != OCI_SUCCESS)
    {
        throw Exception(ret, _err, "set SVCCTX attrib failed.");
    }

    ret = OCIHandleAlloc((const void*)_env, (void**)&conn->_auth, OCI_HTYPE_SESSION, 0, NULL);
    if(ret != OCI_SUCCESS || conn->_auth == NULL)
    {
        throw Exception(ret, "create Auth Session failed.");
    }

    ret = OCIAttrSet((void*)conn->_auth, OCI_HTYPE_SESSION, (void*)user.c_str(), user.size(), OCI_ATTR_USERNAME, _err);
    if(ret != OCI_SUCCESS)
    {
        throw Exception(ret, _err, "set Username attrib failed.");
    }

    ret = OCIAttrSet((void*)conn->_auth, OCI_HTYPE_SESSION, (void*)passwd.c_str(), passwd.size(), OCI_ATTR_PASSWORD, _err);
    if(ret != OCI_SUCCESS)
    {
        throw Exception(ret, _err, "set Password attrib failed.");
    }
    
    ret = OCISessionBegin(conn->_svc, _err, conn->_auth, OCI_CRED_RDBMS, OCI_DEFAULT);
    if(ret != OCI_SUCCESS)
    {
        throw Exception(ret, _err, "Start session failed.");
    }

    OCIAttrSet((void*)conn->_svc, OCI_HTYPE_SVCCTX, (void*)conn->_auth, 0, OCI_ATTR_SESSION, _err);

    return conn;
}

void Environment::destroyConnection(ocipp::Connection *conn)
{
    if(conn == NULL)
        return;

    OCISessionEnd(conn->_svc, _err, conn->_auth, OCI_DEFAULT);
    OCIServerDetach(conn->_srv, _err, 0);

    OCIHandleFree((void*)conn->_auth, OCI_HTYPE_SESSION);
    OCIHandleFree((void*)conn->_svc, OCI_HTYPE_SVCCTX);
    OCIHandleFree((void*)conn->_srv, OCI_HTYPE_SERVER);

    delete conn, conn = NULL;
}

////
Connection::Connection(ocipp::Environment *env)
: _env(env)
, _srv(NULL)
, _svc(NULL)
, _auth(NULL)
{
}

Statement* Connection::makeStatement(const std::string &sql)
{
    Statement *stmt = new Statement(this);

    int ret = OCIHandleAlloc((const void*)_env->getEnv(), (void**)&(stmt->_stmt), OCI_HTYPE_STMT, 0, NULL);
    if(ret != OCI_SUCCESS || stmt->_stmt == NULL)
    {
        throw Exception(ret, "create Stmt fail.");
    }

    ret = OCIStmtPrepare(stmt->_stmt, _env->getError(), (const OraText*)sql.c_str(), sql.size(), OCI_NTV_SYNTAX, OCI_DEFAULT);
    if(ret != OCI_SUCCESS)
    {
        throw Exception(ret, _env->getError(), "prepare Stmt failed.");
    }

    return stmt;
}

void Connection::destroyStatement(ocipp::Statement *stmt)
{ 
    OCIHandleFree(stmt->_stmt, OCI_HTYPE_STMT);
    delete stmt, stmt = NULL;
}

////
Statement::Statement(ocipp::Connection *conn)
: _conn(conn)
, _stmt(NULL)
{
}

int Statement::bindString(unsigned int pos, const std::string &val)
{
    OCIBind* bd = NULL;
    int ret = OCIBindByPos(_stmt, &bd, _conn->getEnvironment()->getError(), pos, (void*)val.c_str(), val.size(), SQLT_STR, NULL, NULL, NULL, 0, NULL, OCI_DEFAULT);
    if(ret != OCI_SUCCESS)
    {
        throw Exception(ret, _conn->getEnvironment()->getError(), "bind String failed.");
    }

    return 0;
}

int Statement::defineString(unsigned int pos, std::string &val)
{
    OCIDefine* def = NULL;
    char buf[256];
    memset(buf, 0, 256);
    int ret = OCIDefineByPos(_stmt, &def, _conn->getEnvironment()->getError(), pos, (void*)buf, sizeof(buf), SQLT_STR, NULL, NULL, NULL, OCI_DEFAULT);
    if(ret != OCI_SUCCESS)
    {
        throw Exception(ret, _conn->getEnvironment()->getError(), "define String failed.");
    }

    val.assign(buf, strlen(buf));

    return 0;
}

int Statement::execute()
{
    unsigned short type = 0;
    int ret = OCIAttrGet((const void*)_stmt, OCI_HTYPE_STMT, (void*)&type, 0, OCI_ATTR_STMT_TYPE, _conn->getEnvironment()->getError());
    if(ret != OCI_SUCCESS)
    {
        throw Exception(ret, _conn->getEnvironment()->getError(), "get Stmt type failed.");
    }

    ret = OCIStmtExecute(_conn->getSvc(), _stmt, _conn->getEnvironment()->getError(), (type != OCI_STMT_SELECT ? 1 : 0), 0, NULL, NULL, OCI_COMMIT_ON_SUCCESS/*OCI_DEFAULT*/);
    if(ret != OCI_SUCCESS)
    {
        throw Exception(ret, _conn->getEnvironment()->getError(), "execute Stmt failed.");
    }
    return 0;
}

int Statement::getNext()
{
    int ret = OCIStmtFetch2(_stmt, _conn->getEnvironment()->getError(), 1, OCI_FETCH_NEXT, 1, OCI_DEFAULT);
    if(ret == OCI_NO_DATA)
        return -1;
    if(ret != OCI_SUCCESS)
    {
        throw Exception(ret, _conn->getEnvironment()->getError(), "fetch Stmt failed.");
    }
    return 0;
}

}

/////
std::ostream& operator << (std::ostream& os, const ocipp::Exception& e)
{
    e.show(os);
    return os;
}