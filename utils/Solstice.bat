@Title	Compiere Client %COMPIERE_HOME%   %1%
@Rem $Id: RUN_Compiere2.bat,v 1.2 2007/07/19 15:29:45 marmor01 Exp $
@Echo off

@Rem Set/Overwrite COMPIERE_HOME/JAVA_HOME 
@Rem explicitly here for different versions, etc. e.g.
@Rem
@SET COMPIERE_HOME=C:\Solstice\Compiere3
@SET JAVA_HOME=C:\Program Files\Java\jdk-27
@SET PATH=%JAVA_HOME%\bin;%PATH%

:CHECK_JAVA:
@if not "%JAVA_HOME%" == "" goto JAVA_HOME_OK
@Set JAVA=java
@Echo JAVA_HOME is not set.  
@Echo   You may not be able to start Compiere
@Echo   Set JAVA_HOME to the directory of your local JDK.
@Echo   You could set it via WinEnv.js e.g.:
@Echo     cscript WinEnv.js C:\Compiere2 C:\j2sdk1.4.2_06

@goto CHECK_COMPIERE
:JAVA_HOME_OK
@Set JAVA=%JAVA_HOME%\bin\javaw

:CHECK_COMPIERE
@if not "%COMPIERE_HOME%" == "" goto COMPIERE_HOME_OK
Set CLASSPATH=%COMPIERE_HOME%\lib\Compiere.jar;%COMPIERE_HOME%\lib\CompiereCLib.jar;%COMPIERE_HOME%\lib\*;%CLASSPATH%
set COMPIERE_HOME=C:\Solstice\Compiere3
@Echo COMPIERE_HOME is not set.  
@Echo   You may not be able to start Compiere
@Echo   Set COMPIERE_HOME to the directory of Compiere2.
@Echo   You could set it via WinEnv.js e.g.:
@Echo     cscript WinEnv.js C:\Compiere2 C:\j2sdk1.4.2_08
@goto MULTI_INSTALL
:COMPIERE_HOME_OK
@Set CLASSPATH=%COMPIERE_HOME%\lib\Compiere.jar;%COMPIERE_HOME%\lib\CompiereCLib.jar;%COMPIERE_HOME%\lib\*;%CLASSPATH%
@IF EXIST "%COMPIERE_HOME%\tools\lib" SET CLASSPATH=%CLASSPATH%;%COMPIERE_HOME%\tools\lib\*
@IF EXIST "D:\SolsticeKK-main\tools\lib" SET CLASSPATH=%CLASSPATH%;D:\SolsticeKK-main\tools\lib\*;D:\SolsticeKK-main\tools\lib\crystal\*

:MULTI_INSTALL
@REM  To switch between multiple installs, copy the created Compiere.properties file
@REM  Select the configuration by setting the PROP variable
@SET PROP=
@Rem  SET PROP=-DPropertyFile=C:\test.properties
@REM  Alternatively use parameter
@if "%1" == "" goto ENCRYPTION
@SET PROP=-DPropertyFile=%1

:ENCRYPTION
@Rem  To use your own Encryption class (implementing org.compiere.util.SecureInterface),
@Rem  you need to set it here (and in the server start script) - example:
@Rem  SET SECURE=-DCOMPIERE_SECURE=org.compiere.util.Secure
@SET SECURE=

:START
@SET COMPAT_JAR=%COMPIERE_HOME%\lib\japplet-compat.jar
@IF NOT EXIST "%COMPAT_JAR%" SET COMPAT_JAR=%COMPIERE_HOME%\tools\lib\japplet-compat.jar
@IF NOT EXIST "%COMPAT_JAR%" SET COMPAT_JAR=D:\SolsticeKK-main\tools\lib\japplet-compat.jar

@SET VM_ARGS=--enable-native-access=ALL-UNNAMED --patch-module java.desktop=%COMPAT_JAR% --add-exports=java.desktop/java.applet=ALL-UNNAMED --add-exports=java.desktop/sun.security.action=ALL-UNNAMED --add-exports=java.sql.rowset/com.sun.rowset=ALL-UNNAMED --add-exports=java.sql.rowset/com.sun.rowset.internal=ALL-UNNAMED --add-exports=java.sql.rowset/com.sun.rowset.providers=ALL-UNNAMED --add-exports=java.naming/com.sun.jndi.ldap=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.desktop/java.awt=ALL-UNNAMED --add-opens=java.desktop/javax.swing=ALL-UNNAMED

REM %JAVA% -Xms32m -Xmx1024m -DCOMPIERE_HOME=%COMPIERE_HOME% %PROP% %SECURE% -classpath %CLASSPATH% org.compiere.Compiere 
start javaw %VM_ARGS% -Xms1024m -Xmx1024m -DCOMPIERE_HOME=%COMPIERE_HOME% %PROP% %SECURE% -classpath "%CLASSPATH%" org.compiere.Compiere 

@sleep 15
