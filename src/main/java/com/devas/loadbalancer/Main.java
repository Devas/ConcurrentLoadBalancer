package com.devas.loadbalancer;

/**
 * This demo all the time produces users and consumes them by Backend's routing policy.
 */
public class Main {

    private static final LoadBalancer loadBalancer = new CoreLoadBalancer();
    private static final UsersManager usersManager = new UsersManager(10, 20);

    public static void main(String[] args) {

        loadBalancer.addBackend(new CoreBackend(1));
        loadBalancer.addBackend(new CoreBackend(3));
        loadBalancer.addBackend(new CoreBackend(11));
        loadBalancer.printAllBackends();

        Thread thread1 = new Thread(() -> usersManager.produceUsers());
        Thread thread2 = new Thread(() -> usersManager.consumeUsers(loadBalancer));

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
