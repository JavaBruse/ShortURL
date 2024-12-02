package org.MIFI.GRUD;

import org.MIFI.entity.Entity;

public interface DAO {
    Entity findById(Long id);
    boolean saveOrUpdate(Entity entity);
    boolean deleteById(Long id);
}
