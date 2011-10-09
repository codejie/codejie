#ifndef __SERVICE_H__
#define __SERVICE_H__

#include "ace/Event_Handler.h"
#include "ace/NT_Service.h"
#include "ace/Singleton.h"
#include "ace/Mutex.h"

class ServiceKeeper : public ACE_NT_Service
{
public:
    ServiceKeeper();
    virtual ~ServiceKeeper();

    virtual void handle_control(DWORD control_code);
    virtual int handle_exception(ACE_HANDLE h);
    virtual int svc();
    virtual int handle_timeout(const ACE_Time_Value& tv, const void *arg = 0);
private:
    int stop_;
};

//
class ServiceStarter
{
public:
    ServiceStarter();
    ~ServiceStarter();

    int run(int argc, char* argv[]);
private:
    void parse_args(int argc, char* argv[]);
    void print_usage_and_die();

private:
    int opt_install;
    int opt_remove;
    int opt_start;
    int opt_kill;
    int opt_type;
    int opt_debug;

    int opt_startup;
};

typedef ACE_Singleton<ServiceKeeper, ACE_Mutex> KEEPER;


#endif
