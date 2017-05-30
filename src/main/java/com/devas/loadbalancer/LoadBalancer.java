package com.devas.loadbalancer;

import java.util.List;
import java.util.Map;

/**
 * Service which distribute users’ requests to different servers based on predefined weight.
 * Once user has been assigned to particular backend, every consecutive request of that user should go to same backend.
 *
 * Example weights:
 *
 * Backend A: 1
 * Backend B: 2
 * Backend C: 7
 */
public interface LoadBalancer {

    List<Backend> getBackends();

    Map<Backend, Double> getBackendsProbabilities();

    void printAllBackends();

    Backend route(UserId id);

    Backend addBackend(Backend backend);

    Backend removeBackend(Backend backend);

    interface Backend {

        int getWeight();

        List<UserId> getUserIds();

        void addUserId(UserId userId);

    }

    interface UserId {

        int getId();

    }
}
