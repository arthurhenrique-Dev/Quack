package com.quack.quack_app.Application.UseCases.Services;

import com.quack.quack_app.Domain.Exceptions.ProcessingErrorException;
import org.slf4j.Logger;

import java.util.List;
import java.util.function.Supplier;

public class TryGetService {

    public static <T> T execute(
            Supplier<T> getMethod,
            Logger log
    ){
        try {
            return getMethod.get();
        } catch (Exception e) {
            var pe = new ProcessingErrorException();
            log.error(pe.getMessage(), e);
            throw pe;
        }
    }
}
