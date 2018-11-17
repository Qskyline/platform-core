#/bin/bash

build_dir=/Users/skyline/WebstormProjects

workdir=$(cd `dirname $0`; pwd)
repo_url=ssh://git@github.com:Qskyline/client.git
temp=${repo_url##*/}
prj_name=${temp%.*}
version=master

if [ -d "$build_dir/$prj_name" ]; then
    cd $build_dir/$prj_name
    git checkout $version
    git pull
elif [ -d "$build_dir" ]; then
    cd $build_dir
    git clone $repo_url
    cd $prj_name
    git checkout -b $version origin/$version
else
    mkdir -p $build_dir
    cd $build_dir
    git clone $repo_url
    cd $prj_name
    git checkout -b $version origin/$version
fi

npm install
npm run build

rm -fr $workdir/src/main/resources/static/*
cp -fr dist/* $workdir/src/main/resources/static/