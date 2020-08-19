package io.leego.unique.client;

import io.leego.unique.common.Result;
import io.leego.unique.common.Segment;
import io.leego.unique.common.exception.ObtainErrorException;
import io.leego.unique.common.service.UniqueService;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * Please use {@link CachedUniqueClient} if the servers are clustered.
 * @author Yihleego
 */
public class SimpleUniqueClient extends AbstractUniqueClient {

    public SimpleUniqueClient(UniqueService uniqueService) {
        super(uniqueService);
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
            return collectionFactory.get();
        }
        Result<Segment> result = uniqueService.next(key, size);
        if (!Result.isSuccessful(result)) {
            throw new ObtainErrorException(result.getMessage());
        }
        return toCollection(result.getData(), collectionFactory);
    }

}
