package bots.telegram.BarBot.mapper;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface BaseMapper<F, T> {
    T mapToDto(Optional<F> object);
    F mapToEntity(T object);
    List<T> mapToDtoList(List<F> objects);
    List<F> mapToEntityList(List<T> objects);
}
