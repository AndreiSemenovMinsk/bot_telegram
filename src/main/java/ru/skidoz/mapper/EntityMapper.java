package ru.skidoz.mapper;

import java.util.List;
import java.util.Set;

public abstract class EntityMapper<D, E> {

    public abstract E toEntity(D dto);

    public abstract D toDto(E entity);

    public abstract List<D> toDto(List<E> entityList);

    public abstract Set<D> toDto(Set<E> entityList);
}
