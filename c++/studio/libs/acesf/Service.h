#ifndef __SERVICE_H__
#define __SERVICE_H__

#include <string>
#include <iostream>

#include "ace/NT_Service.h"

#include "acef/app.h"

#if !defined (ACE_LACKS_PRAGMA_ONCE)
#  pragma once
#endif /* ACE_LACKS_PRAGMA_ONCE */

class ACE_Export ACESF_Service : public ACE_NT_Service
{
protected:
    struct service_opt
    {
        int install_;
        int remove_;
        int start_;
        int kill_;
        int type_;
        int debug_;

        int startup_;
    };
public:
    ACESF_Service();
    ACESF_Service(const std::string& name, const std::string& desc);
    virtual ~ACESF_Service() = 0;

    virtual void regist_name() = 0;
    virtual void regist_app() = 0;

	virtual void regist_svc() = 0;

	virtual int run(int argc, char* argv[]);
	
	virtual int svc();

	void show_usage(std::ostream& os) const;
protected:
    virtual void handle_control(DWORD control_code);
    virtual int handle_exception(ACE_HANDLE handle);
    virtual int handle_timeout(const ACE_Time_Value &current_time, const void* act);

    virtual int init(int argc, char* argv[]);
    virtual int fini();

    virtual int getopt(int argc, char* argv[]);
private:
	ACESF_Service(const ACESF_Service&);
	const ACESF_Service& operator=(const ACESF_Service&);
protected:
    std::string name_;
    std::string desc_;

	ACEF_App *app_;
private:    
    service_opt opt_;
private:
	bool stop_;
};

extern ACESF_Service* pACESF_Service;

#if defined (__ACE_INLINE__)
#  include "acesf/Service.i"
#endif /* __ACE_INLINE__ */

#endif

