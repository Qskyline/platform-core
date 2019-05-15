@echo off
:: Set client build dir. It can be any dir.
set build_dir=C:\Users\Administrator\Desktop\clientBuild_temp_dir

set workdir=%~dp0
set repo_url=https://github.com/Qskyline/client.git
set prj_name=client
set branch=template

if exist %build_dir% (
	cd /d %build_dir%
	if exist %prj_name% (
		cd %prj_name%
	)else (
		git clone %repo_url%
		cd %prj_name%
	)
)else (
	md %build_dir%
	cd /d %build_dir%
	git clone %repo_url%
	cd %prj_name%
)

set current_time=%date:~,4%%date:~5,2%%date:~8,2%%time:~0,2%%time:~3,2%%time:~6,2%
set "current_time=%current_time: =%"
set build_branch=%branch%-build-%current_time%
git checkout -b %build_branch% origin/%branch%

call npm install
call npm run build

set static_dir=%workdir%\src\main\resources\static
if exist %static_dir% (
	rd %static_dir% /s /q
)
md %static_dir%
xcopy dist\* %static_dir%\ /e

git checkout master
git branch -d %build_branch%

cd /d %workdir%