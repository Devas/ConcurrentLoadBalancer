package com.devas.loadbalancer;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class UsersManager {

    private int maxNumberOfUsersInQueue;
    private int maxNumberOfUniqueUsers;
    private final BlockingQueue<User> usersQueue;

    public UsersManager(int maxNumberOfUsersInQueue, int maxNumberOfUniqueUsers) {
        this.maxNumberOfUsersInQueue = maxNumberOfUsersInQueue;
        this.maxNumberOfUniqueUsers = maxNumberOfUniqueUsers;
        this.usersQueue = new ArrayBlockingQueue<>(maxNumberOfUsersInQueue);
    }

    public int getMaxNumberOfUsersInQueue() {
        return maxNumberOfUsersInQueue;
    }

    public void setMaxNumberOfUsersInQueue(int maxNumberOfUsersInQueue) {
        this.maxNumberOfUsersInQueue = maxNumberOfUsersInQueue;
    }

    public int getMaxNumberOfUniqueUsers() {
        return maxNumberOfUniqueUsers;
    }

    public void setMaxNumberOfUniqueUsers(int maxNumberOfUniqueUsers) {
        this.maxNumberOfUniqueUsers = maxNumberOfUniqueUsers;
    }

    public BlockingQueue<User> getUsersQueue() {
        return usersQueue;
    }

    /**
     * Produces users and adds them to the queue.
     */
    public void produceUsers() {
        Random random = new Random();
        while (true) {
            try {
                usersQueue.put(new User(random.nextInt(maxNumberOfUniqueUsers)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Makes users to be consumed by LoadBalancer.
     *
     * @param loadBalancer Load balancer that consumes users from the queue
     */
    public void consumeUsers(LoadBalancer loadBalancer) {
        Random random = new Random();
        while (true) {
            try {
                Thread.sleep(10); // 100 times per second
                if (random.nextInt(10) == 0) { // With this if() it executes on average 10 times per second
                    User user = usersQueue.take();
                    System.out.println("\nTrying to route the user with id: " + user.getId() + "; Number of users in usersQueue is: " + usersQueue.size());
                    loadBalancer.route(user);
                    System.out.println(loadBalancer);
                    for (LoadBalancer.Backend backend : loadBalancer.getBackends()) {
                        System.out.println(backend);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
