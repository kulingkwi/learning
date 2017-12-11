package org.kulingkwi.learning.zk;

import org.apache.zookeeper.*;

import java.util.Random;

public class Worker implements Watcher {
    ZooKeeper zk;
    String hostPort;
    String serverId = Integer.toHexString(new Random().nextInt());
    String status;

    Worker(String hostPort) {
        this.hostPort = hostPort;
    }

    void startZk() throws Exception {
        zk = new ZooKeeper(hostPort, 15000, this);
        zk.create("/workers", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, null, null);
    }

    void stopZk() throws Exception {
        zk.close();
    }

    public void process(WatchedEvent watchedEvent) {
        System.out.println(watchedEvent.toString() + "," + hostPort);
    }

    void register() {
        zk.create("/workers/worker-" + serverId, "Idle".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, createWorkerCallback, null);
    }

    AsyncCallback.StringCallback createWorkerCallback = new AsyncCallback.StringCallback() {
        public void processResult(int rc, String path, Object ctx, String name) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    register();
                    break;
                case OK:
                    System.out.println("registered successfully:" + serverId);
                    break;
                case NODEEXISTS:
                    System.out.printf("Already registered: " + serverId);
                    break;
                default:
                    System.out.println("Something went wrong: " + KeeperException.create(KeeperException.Code.get(rc), path));
            }
        };

    };

    public static void main(String[] args) throws Exception {
        String zkCon = args != null && args.length > 0 && args[0] != null ? args[0]:"127.0.0.1:2181";
        Worker worker = new Worker(zkCon);
        worker.startZk();
        worker.register();
        Thread.sleep(30000);
        worker.stopZk();
    }
}
