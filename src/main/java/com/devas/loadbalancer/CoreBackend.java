package com.devas.loadbalancer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static com.devas.loadbalancer.LoadBalancer.Backend;
import static com.devas.loadbalancer.LoadBalancer.UserId;

/**
 * Backend stores Users that were assigned to it
 */
public class CoreBackend implements Backend, Comparable<Backend> {

    private final AtomicInteger weight = new AtomicInteger();
    private final List<UserId> userIds = new CopyOnWriteArrayList<>();

    public CoreBackend(int weight) {
        this.weight.set(weight);
    }

    public int getWeight() {
        return weight.get();
    }

    public List<UserId> getUserIds() {
        return userIds;
    }

    public void addUserId(UserId userId) {
        userIds.add(userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CoreBackend that = (CoreBackend) o;

        if (!weight.equals(that.weight)) return false;
        return userIds != null ? userIds.equals(that.userIds) : that.userIds == null;
    }

    @Override
    public int hashCode() {
        int result = weight.hashCode();
        result = 31 * result + (userIds != null ? userIds.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CoreBackend{" +
                "weight=" + weight +
                ", userIds=" + userIds +
                '}';
    }

    @Override
    public int compareTo(Backend o) {
        if (weight.get() > o.getWeight())
            return 1;
        else if (weight.get() < o.getWeight())
            return -1;
        else
            return 0;
    }

}
