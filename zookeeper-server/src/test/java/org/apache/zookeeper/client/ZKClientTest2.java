/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.zookeeper.client;

import org.apache.zookeeper.*;
import org.apache.zookeeper.common.ZKConfig;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig.ConfigException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.apache.zookeeper.client.ZKClientConfig.*;
import static org.junit.Assert.*;
public class ZKClientTest2 {
    //zookeeper客户端
    private ZooKeeper zooKeeper;

    /**
     * 创建客户端实例对象
     */
    @Before
    public void before() throws IOException {
        try {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            //连接成功后，会回调watcher监听，此连接操作是异步的，执行完new语句后，直接调用后续代码
            //  可指定多台服务地址 127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183
            zooKeeper = new ZooKeeper("127.0.0.1:2181", 400000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if(Event.KeeperState.SyncConnected==event.getState()){
                        //如果收到了服务端的响应事件,连接成功
                        System.out.println("【Watcher监听事件】="+event.getState()+"");
                        System.out.println("【监听路径为】="+event.getPath()+"");
                        System.out.println("【监听的类型为】="+event.getType()+"");//  三种监听类型： 创建，删除，更新
                        countDownLatch.countDown();
                    }
                }
            });
            countDownLatch.await();
            System.out.println("【初始化ZooKeeper连接状态....】= {"+zooKeeper.getState()+"}");

        }catch (Exception e){
            System.out.println("初始化ZooKeeper连接异常....】={}");
            e.getStackTrace();
        }
    }

    //列出根节点下的子节点(非递归)
    @Test
    public void ls() throws InterruptedException, KeeperException {
        List<String> children = zooKeeper.getChildren("/",null);

        for (String child : children) {
            System.out.println(child);
        }

        //Thread.sleep(Long.MAX_VALUE);
    }

    //在一个节点下创建子节点
    @Test
    public void create() throws KeeperException, InterruptedException {
        //parameter1：创建的节点路径  parameter2：节点的数据 parameter3：节点权限  parameter4：节点类型
        String str = zooKeeper.create("/idea", "idea2019".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println(str);
        //Thread.sleep(Long.MAX_VALUE);
    }
}
