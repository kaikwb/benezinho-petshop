package br.com.fiap.petshop.domain.repository;

import br.com.fiap.petshop.domain.entity.Telefone;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class TelefoneRepository implements Repository<Telefone, Long> {

    private static final AtomicReference<TelefoneRepository> instance = new AtomicReference<>();
    private final EntityManager manager;

    private TelefoneRepository(EntityManager manager) {
        this.manager = manager;
    }

    public static TelefoneRepository build(EntityManager manager) {
        TelefoneRepository result = instance.get();
        if (Objects.isNull( result )) {
            TelefoneRepository repo = new TelefoneRepository( manager );
            if (instance.compareAndSet( null, repo )) {
                result = repo;
            } else {
                result = instance.get();
            }
        }
        return result;
    }


    @Override
    public List<Telefone> findAll() {
        String jpql = "FROM Telefone";

        return manager.createQuery(jpql, Telefone.class).getResultList();
    }

    @Override
    public Telefone findById(Long id) {
        return manager.find(Telefone.class, id);
    }

    @Override
    public List<Telefone> findByTexto(String texto) {
        String jpql = "SELECT t FROM Telefone t WHERE LOWER(t.numero) LIKE CONCAT('%',LOWER(:numero) ,'%')";

        return manager.createQuery(jpql, Telefone.class)
            .setParameter("numero", texto)
            .getResultList();
    }

    @Override
    public Telefone persist(Telefone telefone) {
        manager.getTransaction().begin();
        manager.persist(telefone);
        manager.getTransaction().commit();

        return telefone;
    }

    @Override
    public Telefone update(Telefone telefone) {
        manager.getTransaction().begin();
        manager.merge(telefone);
        manager.getTransaction().commit();

        return telefone;
    }

    @Override
    public boolean delete(Telefone telefone) {
        manager.getTransaction().begin();
        manager.remove(telefone);
        manager.getTransaction().commit();

        return true;
    }
}
