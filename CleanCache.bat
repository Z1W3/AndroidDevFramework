@echo off

@echo=
:clean
set /p o=清除项目(%~dp0)中的缓存文件，确定执行吗？ (Y/N):
if /i "%o%"=="y" goto yes
if /i "%o%"=="n" goto no
goto clean
:yes

@echo=
@echo 正在清除根目录.gradle、.idea、build
for /f "delims=" %%i in ('dir /ad/b/a "%dir%"') do (
if %~dp0%%i==%~dp0.gradle (
@echo 删除%~dp0%%i
rd /S/Q %%i
)
if %~dp0%%i==%~dp0.idea (
@echo 删除%~dp0%%i
rd /S/Q %%i
)
if %~dp0%%i==%~dp0build (
@echo 删除%~dp0%%i
rd /S/Q %%i
)
)

@echo=
@echo 正在清除所有Module中的build目录
set dir=build
for /f "delims=" %%i in ('dir /ad/b/a/s "%dir%"') do (
@echo 删除%%i
rd /S/Q %%i
)

@echo=
@echo 正在清除debug目录
set dir=debug
for /f "delims=" %%i in ('dir /ad/b/a/s "%dir%"') do (
@echo 删除%%i
rd /S/Q %%i
)

@echo=
@echo 正在清除release目录
set dir=release
for /f "delims=" %%i in ('dir /ad/b/a/s "%dir%"') do (
@echo 删除%%i
rd /S/Q %%i
)

@echo=
@echo 正在清除所有目录中的*iml文件
for /f "delims=" %%i in ('dir /a/s/b "*.iml"') do (
del %%i
)
pause 

:no
exit