package org.kulingkwi.learning.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher;

public class Master implements Watcher {

    ZooKeeper zk;
    String hostPort;

    Master(String hostPort) {
        this.hostPort = hostPort;
    }

    void startZk() throws Exception {
        zk = new ZooKeeper(hostPort, 15000, this);
    }

    public void process(WatchedEvent watchedEvent) {
        System.out.println(watchedEvent);
    }

    public static void main(String[] args) throws Exception {
        String zkCon = args != null && args.length > 0 && args[0] != null ? args[0]:"127.0.0.1:2181";
        Master master = new Master(zkCon);
        master.startZk();

        //wait for a bit
        Thread.sleep(60000);
    }
}
