package org.kulingkwi.learning.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Random;

public class Master implements Watcher {

    ZooKeeper zk;
    String hostPort;

    String serverId = Long.toHexString(new Random().nextLong());
    boolean isLeader = false;

    AsyncCallback.StringCallback mastercreateCallback = new AsyncCallback.StringCallback() {
        public void processResult(int rc, String path, Object ctx, String name) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    checkMaster();
                    return;
                case OK:
                    isLeader = true;
                    break;
                default:
                    isLeader = false;

            }
            System.out.println("I am " + (isLeader ? "" : " not ") + " the leader");
        }
    };

    Master(String hostPort) {
        this.hostPort = hostPort;
    }

    void startZk() throws Exception {
        zk = new ZooKeeper(hostPort, 15000, this);
    }

    void stopZk() throws Exception {
        zk.close();
    }

    /** return true if there is a master */
    boolean checkMaster() {
        while(true) {
            try {
                Stat stat = new Stat();
                byte[] data = zk.getData("/master", false, stat);
                isLeader = new String(data).equals(serverId);
                return true;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            } catch (KeeperException e) {
                e.printStackTrace();
            }
        }
    }

    /** run for master election */
    void runForMaster() throws InterruptedException {
       while(true) {
            try{
                zk.create("/master", serverId.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                isLeader = true;
            } catch (KeeperException.NodeExistsException e) {
                isLeader = false;
                break;
            } catch (KeeperException.ConnectionLossException e) {
                System.out.println(e);
            } catch (Exception e) {
                System.out.println(e);
            }
            if (checkMaster()) break;
        }
       //zk.create("/master", serverId.getBytes(),  ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, this.mastercreateCallback,null);
    }

    public void process(WatchedEvent watchedEvent) {
        System.out.println(watchedEvent);
    }

    public static void main(String[] args) throws Exception {
        String zkCon = args != null && args.length > 0 && args[0] != null ? args[0]:"127.0.0.1:2181";
        Master master = new Master(zkCon);
        master.startZk();
        master.runForMaster();

        if (master.isLeader) {
            System.out.println("I am the leader");
            //wait for a bit
            Thread.sleep(60000);
        } else {
            System.out.println("Someone else is the leader");
        }

        master.stopZk();
    }
}
