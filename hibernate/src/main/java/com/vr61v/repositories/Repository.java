package com.vr61v.repositories;

import com.vr61v.exceptions.RepositoryException;
import com.vr61v.utils.RepositorySessionManager;
import org.hibernate.Session;

import java.util.Optional;

/**
 * Abstract base repository class implementing core CRUD operations using Hibernate.
 * <p>
 * Provides foundational data access methods for entities of type {@code T} with identifier type {@code ID}.
 * Handles session management and translates persistence exceptions into {@link RepositoryException}.
 * <p>
 * Subclasses must implement entity-specific repositories by providing the entity class.
 *
 * @param <T>  the type of entity managed by this repository
 * @param <ID> the type of entity's identifier
 * @see RepositorySessionManager
 * @see RepositoryException
 */
public abstract class Repository<T, ID> {

    protected final RepositorySessionManager sessionManager;
    public final Class<T> clazz;

    /**
     * Constructs a repository instance for the specified entity class.
     *
     * @param sessionManager session manager for database connections, must not be null
     * @param clazz          entity class (e.g., {@code Ticket.class}), must not be null
     * @throws IllegalArgumentException if sessionManager or clazz is null
     */
    public Repository(RepositorySessionManager sessionManager, Class<T> clazz) {
        if (sessionManager == null || clazz == null) throw new IllegalArgumentException("SessionManager and clazz cannot be null");
        this.sessionManager = sessionManager;
        this.clazz = clazz;
    }

    /**
     * Persists an entity in the database.
     * <p>
     * Uses {@code session.persist()} for new entities.  Return false if remove operation
     * ended with an exception.
     *
     * @param entity the entity to persist, must not be null
     * @return true if persistence succeeded, false on failure
     * @throws IllegalArgumentException if entity is null
     */
    public boolean save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        }

        try (Session session = sessionManager.getSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Retrieves an entity by its identifier.
     * <p>
     * Uses {@code session.find()} for effective lookup. Returns empty optional for missing entities.
     *
     * @param id entity identifier, must not be null
     * @return {@link Optional} containing found entity or empty
     * @throws IllegalArgumentException if id is null
     * @throws RepositoryException if database error occurs
     */
    public Optional<T> findById(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id is null");
        }

        try (Session session = sessionManager.getSession()) {
            return Optional.ofNullable(session.find(clazz, id));
        } catch (Exception e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    /**
     * Updates an existing entity in the database.
     * <p>
     * Uses {@code session.merge()} to attach detached entities. Returns the updated entity.
     *
     * @param entity the entity with updated state, must not be null
     * @return entity instance after update
     * @throws IllegalArgumentException if entity is null
     * @throws RepositoryException if database error occurs
     */
    public T update(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        }

        try (Session session = sessionManager.getSession()) {
            session.beginTransaction();
            entity = session.merge(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new RepositoryException(e.getMessage());
        }

        return entity;
    }

    /**
     * Removes an entity from the database.
     * Uses {@code session.remove()} for remove entities. Return false if remove operation
     * ended with an exception.
     * @param entity the entity to remove, must not be null
     * @return true if deletion succeeded, false on failure
     * @throws IllegalArgumentException if entity is null
     */
    public boolean delete(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        }

        try (Session session = sessionManager.getSession()) {
            session.beginTransaction();
            session.remove(entity);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
