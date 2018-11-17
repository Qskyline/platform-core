@echo off
:: Set client build dir. It can be any dir.
set build_dir=C:\Users\Administrator\Desktop\clientBuild_temp_dir

set workdir=%~dp0
set repo_url=https://github.com/Qskyline/client.git
set prj_name=platform-client
set version=master

if exist %build_dir% (
	cd /d %build_dir%
	if exist %prj_name% (
		cd %prj_name%
		git checkout %version%
		git pull
	)else (
		git clone %repo_url%
		cd %prj_name%
		git checkout -b %version% origin/%version%
	)
)else (
	md %build_dir%
	cd /d %build_dir%
	git clone %repo_url%
	cd %prj_name%
	git checkout -b %version% origin/%version%
)

call npm install
call npm run build

set static_dir=%workdir%\src\main\resources\static
if exist %static_dir% (
	rd %static_dir% /s /q
)
md %static_dir%
xcopy dist\* %static_dir%\ /e

cd /d %~dp0