package io.leego.unique.client;

import io.leego.unique.client.service.UniqueService;
import io.leego.unique.common.Result;
import io.leego.unique.common.Segment;
import io.leego.unique.common.exception.ObtainErrorException;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * @author Yihleego
 */
public class SimpleUniqueClient extends AbstractUniqueClient {
    protected final UniqueService uniqueService;

    public SimpleUniqueClient(UniqueService uniqueService) {
        this.uniqueService = uniqueService;
    }

    @Override
    public Long next(String key) {
        Result<Long> result = uniqueService.next(key);
        if (!Result.isSuccessful(result)) {
            throw new ObtainErrorException(result.getMessage());
        }
        return result.getData();
    }

    @Override
    public <C extends Collection<Long>> C next(String key, int size, Supplier<C> collectionFactory) {
        if (size <= 0) {
            throw new IllegalArgumentException("The size cannot be negative or zero.");
        }
        Result<Segment> result = uniqueService.next(key, size);
        if (!Result.isSuccessful(result)) {
            throw new ObtainErrorException(result.getMessage());
        }
        return toCollection(result.getData(), collectionFactory);
    }

}
