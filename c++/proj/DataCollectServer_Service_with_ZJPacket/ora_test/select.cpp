#include <iostream>
#include <string>
#include "oci.h"

OCIEnv *env = NULL;
OCIError *err = NULL;

const std::string server = "QA"; 


int makeEnv()
{
	sword ret = OCIEnvCreate(&env, OCI_DEFAULT, NULL, NULL, NULL, NULL, 0, NULL);
	if(ret != 0)
		return -1;
	//error
	OCIHandleAlloc((const void*)env, (void**)&err, OCI_HTYPE_ERROR, 0, NULL);
	
	//server
	OCIServer *srv = NULL;
	OCIHandleAlloc((const void*)env, (void**)&srv, OCI_HTYPE_SERVER, 0, NULL);
	OCISvcCtx *svc = NULL;
	OCIHandleAlloc((const void*)env, (void**)&svc, OCI_HTYPE_SVCCTX, 0, NULL);

	OCIServerAttach(srv, err, (const OraText*)"QA", strlen("QA"), 0);
	
	OCIAttrSet((void*)svc, OCI_HTYPE_SVCCTX, (void*)srv, 0, OCI_ATTR_SERVER, err);
	
	//auth session
	OCISession *auth = NULL;
	OCIHandleAlloc((const void*)env, (void**)&auth, OCI_HTYPE_SESSION, 0, NULL);
	OCIAttrSet((void*)auth, OCI_HTYPE_SESSION, (void*)"scott", strlen("scott"), OCI_ATTR_USERNAME, err);
	OCIAttrSet((void*)auth, OCI_HTYPE_SESSION, (void*)"tiger", strlen("tigher"), OCI_ATTR_PASSWORD, err);
	
	ret = OCISessionBegin(svc, err, auth, OCI_CRED_RDBMS, OCI_DEFAULT);

	//

	return (ret == 0 ? 0 : -1);
}




int select()
{
	int ret = makeEnv();
	if(ret != 0)
		std::cout << "error" << std::endl;
	return 0;
}

int main()
{
	select();
	return 0;
}