@Title API Documentation for entire Product
@Rem $Id: RUN_doc.bat,v 1.1 2007/07/18 15:24:08 marmor01 Exp $

del /F /S /Q API
rmdir /S /Q API

@call documentation.bat  "..\base\src;..\client\src;..\dbPort\src;..\extend\src;..\interfaces\src;..\looks\src;..\print\src;..\serverApps\src\main\ejb;..\serverApps\src\main\servlet;..\serverRoot\src\main\client;..\serverRoot\src\main\ejb;..\serverRoot\src\main\server;..\serverRoot\src\main\servlet;..\tools\src" API

@pause


