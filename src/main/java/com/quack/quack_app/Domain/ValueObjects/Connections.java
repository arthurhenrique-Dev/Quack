package com.quack.quack_app.Domain.ValueObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record Connections(List<UUID> connections) {

    public static Connections Start(List<UUID> connections) {
        return new Connections(new ArrayList<>(connections));
    }

    public void addConnection(UUID uuid) {
        this.connections.add(uuid);
    }
    public int nOfConnections() {
        return this.connections.size();
    }
    public void removeConnection(UUID uuid) {
        this.connections.remove(uuid);
    }
}
