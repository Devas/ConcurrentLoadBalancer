package com.devas.loadbalancer;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class CoreLoadBalancer implements LoadBalancer {

    private final List<Backend> backends;
    private final AtomicInteger totalWeight;
    private final ConcurrentSkipListMap<Backend, Double> backendsProbabilities;

    public CoreLoadBalancer() {
        backends = new CopyOnWriteArrayList<>();
        Comparator<Backend> comparator = (o1, o2) -> {
            if (o1.getWeight() > o2.getWeight())
                return 1;
            else if (o1.getWeight() < o2.getWeight())
                return -1;
            else
                return 0;
        };
        backendsProbabilities = new ConcurrentSkipListMap<>(comparator);
        totalWeight = new AtomicInteger();
    }

    @Override
    public List<Backend> getBackends() {
        return backends;
    }

    public AtomicInteger getTotalWeight() {
        return totalWeight;
    }

    public Map<Backend, Double> getBackendsProbabilities() {
        return backendsProbabilities;
    }

    public void printAllBackends() {
        backendsProbabilities.forEach((backend, normWeight) -> System.out.println(backend + " Normalized probability " + normWeight + "\n"));
    }

    @Override
    public Backend route(UserId userId) {
        if (backends.isEmpty()) {
            throw new NullPointerException("Backends do not exist");
        }

        Backend routedBackend = null;

        if (!backends.isEmpty()) {

            // Check if user has been already routed, this can be optimized also using HashMap
            for (Backend backend : backends) {
                if (backend.getUserIds().contains(userId)) {
                    System.out.println("User already routed");
                    return null;
                }
            }

            double random = Math.random();
            System.out.println("Random: " + random);
            double cumulativeProbability = 0.0;
            for (Map.Entry<Backend, Double> entry : backendsProbabilities.entrySet()) {
                cumulativeProbability += entry.getValue();
                System.out.println("Cumulative probability: " + cumulativeProbability);
                if (random <= cumulativeProbability) {
                    routedBackend = entry.getKey();
                    System.out.println(routedBackend);
                    break;
                }
            }
            System.out.println(routedBackend);

            // Add only if Backend exist
            if (routedBackend != null) {
                routedBackend.addUserId(userId);
            }
        }

        return routedBackend;
    }

    @Override
    public Backend addBackend(Backend backend) {
        backends.add(backend);
        totalWeight.addAndGet(backend.getWeight());

        // Add new Backend with proper probability
        double normalizedWeight = normalizeWeight(backend.getWeight());
        backendsProbabilities.put(backend, normalizedWeight);

        // After adding new Backend, all all map with probabilities must be updated
        updateProbabilities();

        return backend;
    }

    @Override
    public Backend removeBackend(Backend backend) {
        backends.remove(backend);
        backendsProbabilities.remove(backend);
        totalWeight.addAndGet(-backend.getWeight());
        return backend;
    }

    @Override
    public String toString() {
        return "CoreLoadBalancer info: {" +
                "Total weight of Backends = " + totalWeight.get() +
                "}, {backendsProbabilities=" + backendsProbabilities.values() +
                '}';
    }

    private void updateProbabilities() {
        backendsProbabilities.replaceAll((backend, aDouble) -> normalizeWeight(backend.getWeight()));
    }

    private double normalizeWeight(int weight) {
        return (double) weight / (double) totalWeight.get();
    }

}
