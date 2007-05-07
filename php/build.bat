@echo off
@call vcvars32.bat /nologo

@cd .\wsf_c\axis2c\build

@if exist axis2c-bin-1.0.0-win32 rmdir /s /q axis2c-bin-1.0.0-win32

@cd win32

@nmake dist

@cd .\..\..\rampart\build\win32

@nmake dist

@cd .\..

@xcopy /E /Q /I rampartc-0.9\modules\rampart .\..\..\build\axis2c-bin-1.0.0-win32\modules\rampart

@copy  rampartc-0.9\modules\rampart\mod_rampart.lib .\..\..\build\axis2c-bin-1.0.0-win32\lib\
@copy  rampartc-0.9\modules\rampart\mod_rampart.dll .\..\..\build\axis2c-bin-1.0.0-win32\lib\


@cd ..\..\..\sandesha2c\build\win32

@nmake dist

@cd ..

@xcopy /E /Q /I sandesha2\sandesha2 .\..\..\axis2c\build\axis2c-bin-1.0.0-win32\modules\sandesha2

@copy sandesha2\samples\bin\*.exe .\..\..\axis2c\build\axis2c-bin-1.0.0-win32\bin\samples\

@xcopy /E /Q /I sandesha2\samples\bin\RMSampleService .\..\..\axis2c\build\axis2c-bin-1.0.0-win32\services\RMSampleService


@copy /Y sandesha2\axis2.xml .\..\..\axis2c\build\axis2c-bin-1.0.0-win32\axis2.xml

@copy /Y sandesha2\mysql_schema.txt .\..\..\axis2c\build\axis2c-bin-1.0.0-win32\mysql_schema.txt

@copy /Y sandesha2\sqlite_schema.bat  .\..\..\axis2c\build\axis2c-bin-1.0.0-win32\sqlite_schema.bat

@copy /Y sandesha2\init_mysql.bat .\..\..\axis2c\build\axis2c-bin-1.0.0-win32\init_mysql.bat

@cd ..\..\axis2c\build\axis2c-bin-1.0.0-win32\bin

















