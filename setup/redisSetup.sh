rootDir="redis"
version="7.2.4"
mkdir -p "${rootDir}"
mkdir -p "${rootDir}"/rdb

# Kill redis server if already running
ps cax | grep redis-server > /dev/null
if [ $? -eq 0 ]; then
  echo "\nRedis server is already running.";
  echo '\nStopping redis server';
  pid=$(ps cax | grep redis-server | awk '{print $1}')
  echo ${pid};
  kill -9 ${pid};
else
  echo '\nRedis server is not running.';
fi

#get redis archive from s3 if not already present
if [ -f "${rootDir}"/redis-"${version}".tar.gz ]
    then
      echo "\nRedis archive already exists. Skipping download..."
    else
      curl https://hrx-projects.s3.amazonaws.com/packages/redis/"${version}"/redis-"${version}".tar.gz -o "${rootDir}"/redis-"${version}".tar.gz
fi

#install redis server
if [ -d "${rootDir}"/redis-"${version}" ]
    then
      echo "\nRedis installation already exists. Skipping installation..."
    else
      tar -xvzf "${rootDir}"/redis-"${version}".tar.gz -C "${rootDir}"/
      cd "${rootDir}"/redis-"${version}"/
      make;

      # Setup config and start redis server
      touch redis-server.log;
      chmod 777 redis-server.log;
      chmod -R 777 src;
      chmod -R 777 ../rdb/;
      rm -rf "${rootDir}"/redis.out;
      cd ../../
      echo "\nCurrently at $PWD"
fi

#start redis
echo "Running command: nohup ${rootDir}/redis-"${version}"/src/redis-server ${rootDir}/redis-"${version}"/redis.conf --maxclients 5 --dir ${rootDir}/rdb/ --dbfilename dump.rdb > "${rootDir}"/redis.out 2>&1 &"
nohup "${rootDir}"/redis-"${version}"/src/redis-server "${rootDir}"/redis-"${version}"/redis.conf --maxclients 5 --dir "${rootDir}"/rdb/ --dbfilename dump.rdb > "${rootDir}"/redis.out 2>&1 &
sleep 15;

# Setup Validation
rm -rf "${rootDir}"/redisInfo.dat;
"${rootDir}"/redis-"${version}"/src/redis-cli INFO > "${rootDir}"/redisInfo.dat;
info=$(cat "${rootDir}"/redis.out| grep "Ready to accept connections");
if [ "${info}" == "" ]; then
    echo [ERROR] [warning]: Redis setup failed.. Please check it!;
else
    sleep 2
    printf '\nRedis server configured successfully.\n';
fi
exit 0;
