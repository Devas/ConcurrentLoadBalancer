package com.devas.loadbalancer;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class LoadBalancerTest {

    @Test
    public void shouldWork() {
        assertTrue(true);
    }

    @Test
    public void areBackendsAdded() {
        LoadBalancer loadBalancer = new CoreLoadBalancer();
        loadBalancer.addBackend(new CoreBackend(1));
        loadBalancer.addBackend(new CoreBackend(3));
        loadBalancer.addBackend(new CoreBackend(11));
        assertTrue(loadBalancer.getBackends().size() == 3);
    }

    @Test(expected = RuntimeException.class)
    public void isExceptionThrownIfNoBackendsAdded() {
        LoadBalancer loadBalancer = new CoreLoadBalancer();
        loadBalancer.route(new User(17));
    }

    // TODO
//    @Test
//    public void areNotUsersDuplicated() {
//        LoadBalancer loadBalancer = new CoreLoadBalancer();
//        HashMap<Integer, User> users = new HashMap<>();
//        UsersManager usersManager = new UsersManager(10, 20);
//        Thread thread1 = new Thread(() -> usersManager.produceUsers());
//        thread1.start();
//        try {
//            thread1.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

}