package hr.fer.oprpp1.custom.scripting.exec;

import hr.fer.oprpp1.custom.collections.EmptyStackException;

import java.util.*;

/**
 * Custom map implementation but with a twist!
 * Values assigned to a key(String) are stacks of ValueWrappers.
 * Every value therefor supports stack like commands.
 *
 * @author MatejCubek
 * @project hw03-0036516398
 * @created 29/03/2021
 */
public class ObjectMultistack {
    private final Map<String, MultistackEntry> map;

    public ObjectMultistack() {
        map = new HashMap<>();
    }

    /**
     * Pushes to map's designated stack.
     *
     * @param keyName      key for stack in question
     * @param valueWrapper valut to push to stack
     */
    public void push(String keyName, ValueWrapper valueWrapper) {
        var last = map.get(keyName);
        MultistackEntry entry = new MultistackEntry(Objects.requireNonNull(valueWrapper), last);

        map.put(keyName, entry);
    }

    /**
     * Pops top element from stack for specified key.
     * That element is removed from stack.
     * If stack is empty method throws {@link EmptyStackException}
     *
     * @param keyName key for stack from which items are queried.
     * @return valuewrapper on top of stack.
     * @throws EmptyStackException if stack is empty
     */
    @SuppressWarnings("UnusedReturnValue")
    public ValueWrapper pop(String keyName) {
        var current = map.get(keyName);
        if (current == null) throw new EmptyStackException();

        var next = current.next;
        map.put(keyName, next);
        return current.value;
    }

    /**
     * Peeks top element from stack for specified key.
     * That element is not removed from stack.
     * If stack is empty method throws {@link EmptyStackException}
     *
     * @param keyName key for wtack from which items are queried.
     * @return valuewrapper on top of stack.
     * @throws EmptyStackException if stack is empty
     */
    public ValueWrapper peek(String keyName) {
        var current = map.get(keyName);
        if (current == null) throw new EmptyStackException();

        return current.value;
    }

    /**
     * Returns if stack is empty or not.
     *
     * @param keyName key for which stack is in question.
     * @return <code>true</code> if empty else <code>false</code>
     */
    @SuppressWarnings("unused")
    public boolean isEmpty(String keyName) {
        return map.containsKey(keyName);
    }

    /**
     * Stack entry helper record.
     */
    private record MultistackEntry(ValueWrapper value,
                                   ObjectMultistack.MultistackEntry next) {
    }
}
