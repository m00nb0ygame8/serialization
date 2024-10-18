package me.moonboygamer.serialization;

import java.util.function.Supplier;

public class Holder<T> {
    private final Supplier<T> supplier;
    private T value;
    private final boolean isDirect;

    // Private constructor for Holder
    private Holder(Supplier<T> supplier, boolean isDirect) {
        this.supplier = supplier;
        this.isDirect = isDirect;
        if (isDirect) {
            this.value = supplier.get(); // Immediately load the object for direct holder
        }
    }

    // Static method to create a Direct Holder
    public static <T> Holder<T> direct(T value) {
        return new Holder<>(() -> value, true); // Immediate creation
    }

    // Static method to create a Reference Holder (Lazy loading)
    public static <T> Holder<T> reference(Supplier<T> supplier) {
        return new Holder<>(supplier, false); // Deferred creation
    }

    // Method to get the value (will load lazily if it's a reference)
    public T get() {
        if (!isDirect && value == null) {
            value = supplier.get();  // Load lazily for reference holders
        }
        return value;
    }

    // ToString to visualize holder details
    @Override
    public String toString() {
        return isDirect ? "Direct Holder [value=" + value + "]"
                : "Reference Holder [loaded=" + (value != null) + "]";
    }
}
