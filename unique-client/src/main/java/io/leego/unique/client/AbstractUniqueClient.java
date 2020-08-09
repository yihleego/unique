package io.leego.unique.client;

import io.leego.unique.common.Segment;
import io.leego.unique.common.exception.IllegalValueException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Supplier;

/**
 * @author Yihleego
 */
public abstract class AbstractUniqueClient implements UniqueClient {

    @Override
    public abstract Long next(String key);

    @Override
    public LinkedList<Long> next(String key, int size) {
        return next(key, size, LinkedList::new);
    }

    @Override
    public abstract <C extends Collection<Long>> C next(String key, int size, Supplier<C> collectionFactory);

    protected <C extends Collection<Long>> C toCollection(final long begin, final long end, final int increment, Supplier<C> collectionFactory) {
        validate(begin, end, increment);
        C c = collectionFactory.get();
        for (long i = begin; i <= end; i += increment) {
            c.add(i);
        }
        return c;
    }

    protected <C extends Collection<Long>> C toCollection(Segment segment, Supplier<C> collectionFactory) {
        return toCollection(segment.getBegin(), segment.getEnd(), segment.getIncrement(), collectionFactory);
    }

    protected void validate(final long begin, final long end, final int increment) {
        long difference = end - begin;
        if (difference >= 0 && difference % increment == 0) {
            return;
        }
        throw new IllegalValueException("Illegal values: begin=" + begin + ", end=" + end + ", increment=" + increment);
    }

}
