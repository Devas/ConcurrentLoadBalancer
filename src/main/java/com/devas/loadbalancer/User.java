package com.devas.loadbalancer;

import java.util.concurrent.atomic.AtomicInteger;

public class User implements LoadBalancer.UserId {

    private final AtomicInteger id = new AtomicInteger();

    public User(int id) {
        this.id.set(id);
    }

    public int getId() {
        return id.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id.get() == user.id.get();
    }

    @Override
    public int hashCode() {
        return id.get();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id.get() +
                '}';
    }

}
