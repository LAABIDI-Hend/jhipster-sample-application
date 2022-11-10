package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Date}.
 */
public interface DateService {
    /**
     * Save a date.
     *
     * @param date the entity to save.
     * @return the persisted entity.
     */
    Date save(Date date);

    /**
     * Updates a date.
     *
     * @param date the entity to update.
     * @return the persisted entity.
     */
    Date update(Date date);

    /**
     * Partially updates a date.
     *
     * @param date the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Date> partialUpdate(Date date);

    /**
     * Get all the dates.
     *
     * @return the list of entities.
     */
    List<Date> findAll();

    /**
     * Get the "id" date.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Date> findOne(Long id);

    /**
     * Delete the "id" date.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
