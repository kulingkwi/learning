FROM mamohr/centos-java

RUN   mkdir -p ~/fastdfs && cd ~/fastdfs && wget https://github.com/happyfish100/libfastcommon/archive/V1.0.7.tar.gz \
  && yum -y install gcc automake autoconf libtool make perl && tar -zxvf V1.0.7.tar.gz &&  tar -zxvf V1.0.7.tar.gz &&  cd libfastcommon-1.0.7 && ./make.sh && ./make.sh install

RUN cd ~/fastdfs && wget https://github.com/happyfish100/fastdfs/archive/V5.05.tar.gz && tar -zxvf V5.05.tar.gz && cd fastdfs-5.05 && ./make.sh && ./make.sh install \
  && cd /etc/fdfs && mkdir -p /data/fastdfs/storage && sed 's/home\/yuqing/data/g' tracker.conf.sample > tracker.conf \
  && sed 's/\/home\/yuqing\/fastdfs/\/data\/fastdfs\/storage/g' storage.conf.sample | sed 's/192.168.209.121/127.0.0.1/g' > storage.conf

WORKDIR /data/fastdfs
ADD start-fdfs.sh /data/fastdfs/
RUN chmod a+x /data/fastdfs/start-fdfs.sh

ENTRYPOINT ["/data/fastdfs/start-fdfs.sh"]

