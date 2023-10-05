package br.com.fiap.petshop.domain.repository;

import br.com.fiap.petshop.domain.entity.animal.Animal;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class AnimalRepository implements Repository<Animal, Long> {

    private static final AtomicReference<AnimalRepository> instance = new AtomicReference<>();
    private final EntityManager manager;

    private AnimalRepository(EntityManager manager) {
        this.manager = manager;
    }

    public static AnimalRepository build(EntityManager manager) {
        AnimalRepository result = instance.get();
        if (Objects.isNull(result)) {
            AnimalRepository repo = new AnimalRepository(manager);
            if (instance.compareAndSet(null, repo)) {
                result = repo;
            } else {
                result = instance.get();
            }
        }
        return result;
    }


    @Override
    public List<Animal> findAll() {
        String jpql = "FROM Animal";

        return manager.createQuery(jpql, Animal.class).getResultList();
    }

    @Override
    public Animal findById(Long id) {
        return manager.find(Animal.class, id);
    }

    @Override
    public List<Animal> findByTexto(String texto) {
        String jpql = "SELECT a FROM Animal a WHERE LOWER(a.nome) LIKE CONCAT('%',LOWER(:nome) ,'%')";

        return manager.createQuery(jpql, Animal.class)
            .setParameter("nome", texto)
            .getResultList();
    }

    @Override
    public Animal persist(Animal animal) {
        manager.getTransaction().begin();
        manager.persist(animal);
        manager.getTransaction().commit();

        return animal;
    }

    @Override
    public Animal update(Animal animal) {
        manager.getTransaction().begin();
        manager.merge(animal);
        manager.getTransaction().commit();

        return animal;
    }

    @Override
    public boolean delete(Animal animal) {
        manager.getTransaction().begin();
        manager.remove(animal);
        manager.getTransaction().commit();

        return true;
    }
}
