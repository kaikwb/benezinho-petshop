package br.com.fiap.petshop.domain.repository;

import br.com.fiap.petshop.domain.entity.servico.Servico;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class ServicoRepository implements Repository<Servico, Long>{

    private static final AtomicReference<ServicoRepository> instance = new AtomicReference<>();
    private final EntityManager manager;

    private ServicoRepository(EntityManager manager) {
        this.manager = manager;
    }

    public static ServicoRepository build(EntityManager manager) {
        ServicoRepository result = instance.get();
        if (Objects.isNull( result )) {
            ServicoRepository repo = new ServicoRepository( manager );
            if (instance.compareAndSet( null, repo )) {
                result = repo;
            } else {
                result = instance.get();
            }
        }
        return result;
    }


    @Override
    public List<Servico> findAll() {
        String jpql = "FROM Servico";

        return manager.createQuery(jpql, Servico.class).getResultList();
    }

    @Override
    public Servico findById(Long id) {
        return manager.find(Servico.class, id);
    }

    @Override
    public List<Servico> findByTexto(String texto) {
        String jpql = "SELECT s FROM Servico s WHERE LOWER(s.TP_SERVICO) LIKE CONCAT('%',LOWER(:servico) ,'%')";

        return manager.createQuery(jpql, Servico.class)
            .setParameter("servico", texto)
            .getResultList();
    }

    @Override
    public Servico persist(Servico servico) {
        manager.getTransaction().begin();
        manager.persist(servico);
        manager.getTransaction().commit();

        return servico;
    }

    @Override
    public Servico update(Servico servico) {
        manager.getTransaction().begin();
        manager.merge(servico);
        manager.getTransaction().commit();

        return servico;
    }

    @Override
    public boolean delete(Servico servico) {
        manager.getTransaction().begin();
        manager.remove(servico);
        manager.getTransaction().commit();

        return true;
    }
}
