package com.vr61v.repositories;

import com.vr61v.exceptions.RepositoryException;
import com.vr61v.filters.Filter;

import java.util.List;
import java.util.Optional;

/**
 * Generic interface for repository operations on objects of type T.
 * Provides basic CRUD (Create, Read, Update, Delete) operations
 * and bulk operations for collections of entities.
 * <p>
 * Implementations of this interface should handle all database-specific
 * operations and translate any SQL exceptions into {@link RepositoryException}.
 *
 * @param <T> the type of entities this repository manages
 * @see RepositoryException
 */
public interface Repository<T> {

    /**
     * Adds a single entity to the repository.
     * <p>
     * Implementation should validate the entity before insertion and handle
     * any database constraints violations by throwing RepositoryException.
     *
     * @param t the entity to add, must not be null
     * @return true if the entity was successfully added, false if insertion failed
     * @throws RepositoryException if there's an error during database operation
     */
    boolean add(T t);

    /**
     * Adds all entities from the collection to the repository in a batch operation.
     * <p>
     * The operation should be atomic - either all entities are added or none.
     *
     * @param t collection of entities to add, must not be null or empty
     * @return true if all entities were successfully added, false otherwise
     * @throws RepositoryException if there's an error during database operation
     */
    boolean addAll(List<T> t);

    /**
     * Finds an entity by its unique identifier.
     *
     * @param id the unique identifier of the entity, must not be null or empty
     * @return an {@link Optional} containing the found entity or empty if not found
     * @throws RepositoryException if there's an error during database operation
     */
    Optional<T> findById(String id);

    /**
     * Retrieves all entities from the repository.
     * <p>
     * For large datasets, consider using {@link #findPage(int, int)} instead.
     *
     * @return list of all entities in the repository, empty list if none found
     * @throws RepositoryException if there's an error during database operation
     */
    List<T> findAll();

    /**
     * Retrieves all entities matching the specified filter conditions.
     *
     * @param filter the filter criteria to apply, must not be null
     * @return list of matching entities, empty list if none found
     * @throws RepositoryException if there's an error during database operation
     *                            or if filter is invalid
     */
    List<T> findAll(Filter filter);

    /**
     * Finds all entities with the specified identifiers.
     * <p>
     * The returned list may be smaller than the input list if some entities weren't found.
     *
     * @param ids list of identifiers to search for, must not be null or empty
     * @return list of found entities, empty list if none found
     * @throws RepositoryException if there's an error during database operation
     */
    List<T> findAllById(List<String> ids);

    /**
     * Retrieves a paginated list of entities from the repository.
     * <p>
     * The pagination is 0-based. Results are ordered consistently for stable pagination.
     *
     * @param page the page number (0-based)
     * @param size the number of entities per page
     * @return list of entities for the requested page, empty list if page is empty
     * @throws RepositoryException if there's an error during database operation
     *                            or if pagination parameters are invalid
     */
    List<T> findPage(int page, int size);

    /**
     * Updates an existing entity in the repository.
     * <p>
     * The implementation should verify the entity exists before updating.
     *
     * @param t the entity with updated information, must not be null
     * @return true if the entity was successfully updated, false if entity not found
     * @throws RepositoryException if there's an error during database operation
     */
    boolean update(T t);

    /**
     * Updates all entities from the collection in the repository.
     * <p>
     * The operation should be atomic - either all entities are updated or none.
     *
     * @param t collection of entities to update, must not be null or empty
     * @return true if all entities were successfully updated, false if any update failed
     * @throws RepositoryException if there's an error during database operation
     */
    boolean updateAll(List<T> t);

    /**
     * Deletes an entity with the specified identifier from the repository.
     *
     * @param id the unique identifier of the entity to delete, must not be null or empty
     * @return true if the entity was successfully deleted, false if entity not found
     * @throws RepositoryException if there's an error during database operation
     */
    boolean delete(String id);

    /**
     * Deletes all entities with the specified identifiers from the repository.
     * <p>
     * The operation should be atomic - either all entities are deleted or none.
     *
     * @param ids list of identifiers of entities to delete, must not be null or empty
     * @return true if all entities were successfully deleted, false if any deletion failed
     * @throws RepositoryException if there's an error during database operation
     */
    boolean deleteAll(List<String> ids);

}
