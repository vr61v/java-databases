package com.vr61v.repositories;

import java.util.List;

/**
 * Generic interface for repository operations on objects of type T.
 * Provides basic CRUD (Create, Read, Update, Delete) operations
 * and bulk operations for collections of entities.
 *
 * @param <T> the type of entities this repository manages
 */
public interface Repository<T> {

    /**
     * Adds a single entity to the repository.
     *
     * @param t the entity to add
     * @return true if the entity was successfully added, false otherwise
     */
    boolean add(T t);

    /**
     * Adds all entities from the collection to the repository.
     *
     * @param t collection of entities to add
     * @return true if all entities were successfully added, false otherwise
     */
    boolean addAll(List<T> t);

    /**
     * Finds an entity by its unique identifier.
     *
     * @param id the unique identifier of the entity
     * @return the found entity or null if not found
     */
    T findById(String id);

    /**
     * Retrieves all entities from the repository.
     *
     * @return list of all entities in the repository
     */
    List<T> findAll();

    /**
     * Finds all entities with the specified identifiers.
     *
     * @param ids list of identifiers to search for
     * @return list of found entities (maybe smaller than input list if some entities weren't found)
     */
    List<T> findAllById(List<String> ids);

    /**
     * Retrieves a paginated list of entities from the repository.
     *
     * @param page the page number (0-based)
     * @param size the number of entities per page
     * @return list of entities for the requested page
     */
    List<T> findPage(int page, int size);

    /**
     * Updates an existing entity in the repository.
     *
     * @param t the entity with updated information
     * @return true if the entity was successfully updated, false otherwise
     */
    boolean update(T t);

    /**
     * Updates all entities from the collection in the repository.
     *
     * @param t collection of entities to update
     * @return true if all entities were successfully updated, false otherwise
     */
    boolean updateAll(List<T> t);

    /**
     * Deletes an entity with the specified identifier from the repository.
     *
     * @param id the unique identifier of the entity to delete
     * @return true if the entity was successfully deleted, false otherwise
     */
    boolean delete(String id);

    /**
     * Deletes all entities with the specified identifiers from the repository.
     *
     * @param ids list of identifiers of entities to delete
     * @return true if all entities were successfully deleted, false otherwise
     */
    boolean deleteAll(List<String> ids);

}
