package me.moonboygamer.serialization;

public interface Codec<T> {
    String encode(T object);
    T decode(String encodedData);
}
