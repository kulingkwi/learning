#!/bin/bash

# get docker container's id
container_hostname=`tail -1 /etc/hosts | awk '{print $2}'`

tracker_conf=/etc/fdfs/tracker.conf
storage_conf=/etc/fdfs/storage.conf
storage_conf_tmp=/etc/fdfs/storage.conf.tmp

# set storage configuration's tracker_server ip
sed "s/127.0.0.1/$container_hostname/g" $storage_conf > $storage_conf_tmp
mv $storage_conf /etc/fdfs/storage.conf.bak
mv $storage_conf_tmp $storage_conf

# start up tracker daemon
/usr/bin/fdfs_trackerd $tracker_conf start

# start up storage daemon
/usr/bin/fdfs_storaged $storage_conf start

# print monitor information
/usr/bin/fdfs_monitor $storage_conf
