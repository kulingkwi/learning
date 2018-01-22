FROM  maven:3.3.1

RUN groupadd admin && useradd -g admin -s /bin/bash -c Admin admin  && mkdir -p /home/admin && chown admin /home/admin
USER admin
RUN cd /home/admin && git clone https://github.com/alibaba/RocketMQ.git
WORKDIR /home/admin/RocketMQ
RUN git checkout c9bc70a141d393c0b7334b0ff866fb8298bfe5c5 -b dev
RUN mvn -Dmaven.test.skip=true clean package install assembly:assembly -U
RUN ln -s target/alibaba-rocketmq-3.5.8-alibaba-rocketmq/alibaba-rocketmq devenv

WORKDIR /home/admin/RocketMQ/devenv/bin
EXPOSE 9876
CMD  ["sh", "-c", ". ./play.sh; while sleep 10; do echo RocketMQ, GO ROCK; done"]
