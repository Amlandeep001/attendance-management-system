# attendance-management-system
Attendance Management System for an organization or school/college

**For generating auth code**:
1. Login with the following URL in the incognito mode: http://127.0.0.1:8000/oauth2/authorize?response_type=code&client_id=client1&scope=openid read&client_secret=myClientSecretValue&redirect_uri=http://127.0.0.1:8080/authorized
2. It will land up to white level error page after successful login
3. In this stage, just overwrite the address bar with same URL again:  http://127.0.0.1:8000/oauth2/authorize?response_type=code&client_id=client1&scope=openid read&client_secret=myClientSecretValue&redirect_uri=http://127.0.0.1:8080/authorized
4. It will give the auth code in the addrsss bar once its loading is completed. 
   
