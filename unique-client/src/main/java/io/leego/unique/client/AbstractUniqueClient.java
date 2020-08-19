package io.leego.unique.client;

import io.leego.unique.common.Result;
import io.leego.unique.common.Segment;
import io.leego.unique.common.Validation;
import io.leego.unique.common.exception.IllegalValueException;
import io.leego.unique.common.exception.ObtainErrorException;
import io.leego.unique.common.service.UniqueService;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author Yihleego
 */
public abstract class AbstractUniqueClient implements UniqueClient {
    protected final UniqueService uniqueService;

    public AbstractUniqueClient(UniqueService uniqueService) {
        Objects.requireNonNull(uniqueService);
        this.uniqueService = uniqueService;
    }

    @Override
    public abstract Long next(String key);

    @Override
    public LinkedList<Long> next(String key, int size) {
        return next(key, size, LinkedList::new);
    }

    @Override
    public abstract <C extends Collection<Long>> C next(String key, int size, Supplier<C> collectionFactory);

    @Override
    public Validation validateKeys(Set<String> keys) {
        if (keys == null || keys.isEmpty()) {
            return Validation.buildSuccess();
        }
        Result<Set<String>> result = uniqueService.contains(keys);
        if (!Result.isSuccessful(result)) {
            throw new ObtainErrorException(result.getMessage());
        }
        Set<String> presentKeys = result.getData();
        Set<String> absentKeys = new HashSet<>();
        for (String key : keys) {
            if (!presentKeys.contains(key)) {
                absentKeys.add(key);
            }
        }
        if (absentKeys.isEmpty()) {
            return Validation.buildSuccess();
        } else {
            return Validation.buildFailure(absentKeys);
        }
    }

    protected <C extends Collection<Long>> C toCollection(final long begin, final long end, final int increment, Supplier<C> collectionFactory) {
        long difference = end - begin;
        if (difference < 0 || difference % increment != 0) {
            throw new IllegalValueException("Illegal values: begin=" + begin + ", end=" + end + ", increment=" + increment);
        }
        C c = collectionFactory.get();
        for (long i = begin; i <= end; i += increment) {
            c.add(i);
        }
        return c;
    }

    protected <C extends Collection<Long>> C toCollection(Segment segment, Supplier<C> collectionFactory) {
        return toCollection(segment.getBegin(), segment.getEnd(), segment.getIncrement(), collectionFactory);
    }

}
