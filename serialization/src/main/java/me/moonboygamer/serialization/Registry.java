package me.moonboygamer.serialization;

import java.util.*;
import java.util.function.Supplier;

public class Registry<T> {
    private final Map<String, Holder<T>> registry = new HashMap<>();

    // Register an object directly with a unique ID
    public void registerDirect(String id, T object) {
        if (registry.containsKey(id)) {
            throw new IllegalArgumentException("ID '" + id + "' is already registered.");
        }
        registry.put(id, Holder.direct(object)); // Use direct method for immediate loading
    }

    // Register an object with a Supplier for deferred loading
    public void registerReference(String id, Supplier<T> supplier) {
        if (registry.containsKey(id)) {
            throw new IllegalArgumentException("ID '" + id + "' is already registered.");
        }
        registry.put(id, Holder.reference(supplier)); // Use reference method for lazy loading
    }

    // Get an object by its ID (returns Optional)
    public Optional<T> get(String id) {
        return Optional.ofNullable(registry.get(id)).map(Holder::get); // Get object via Holder
    }

    // Remove an object from the registry
    public Optional<T> remove(String id) {
        return Optional.ofNullable(registry.remove(id)).map(Holder::get); // Return the removed object
    }

    // Serialize all registered objects using a Codec
    public Set<String> serializeAll(Codec<T> codec) {
        Set<String> serialized = new HashSet<>();
        for (Map.Entry<String, Holder<T>> entry : registry.entrySet()) {
            String encoded = codec.encode(entry.getValue().get());
            serialized.add(encoded);
        }
        return Collections.unmodifiableSet(serialized);
    }

    // Deserialize an object and register it (using Codec for deserialization)
    public void deserializeAndRegister(String id, String data, Codec<T> codec) {
        if (registry.containsKey(id)) {
            throw new IllegalArgumentException("ID '" + id + "' is already registered.");
        }
        T object = codec.decode(data);
        registry.put(id, Holder.direct(object)); // Store directly since the object is already decoded
    }
    public Set<T> deserializeAll(Codec<T> codec, Set<String> data) {
        Set<T> deserialized = new HashSet<>();
        for (String encoded : data) {
            T object = codec.decode(encoded);
            deserialized.add(object);
        }
        return Collections.unmodifiableSet(deserialized);
    }

    // List all registered IDs
    public Set<String> getAllIDs() {
        return Collections.unmodifiableSet(registry.keySet());
    }

    // Clear all objects from the registry
    public void clear() {
        registry.clear();
    }
}
