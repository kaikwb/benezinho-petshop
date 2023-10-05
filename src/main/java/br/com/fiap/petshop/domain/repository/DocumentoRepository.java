package br.com.fiap.petshop.domain.repository;

import br.com.fiap.petshop.domain.entity.Documento;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class DocumentoRepository implements Repository<Documento, Long> {

    private static final AtomicReference<DocumentoRepository> instance = new AtomicReference<>();
    private final EntityManager manager;

    private DocumentoRepository(EntityManager manager) {
        this.manager = manager;
    }

    public static DocumentoRepository build(EntityManager manager) {
        DocumentoRepository result = instance.get();
        if (Objects.isNull( result )) {
            DocumentoRepository repo = new DocumentoRepository( manager );
            if (instance.compareAndSet( null, repo )) {
                result = repo;
            } else {
                result = instance.get();
            }
        }
        return result;
    }


    @Override
    public List<Documento> findAll() {
        String jpql = "FROM Documento";

        return manager.createQuery(jpql, Documento.class).getResultList();
    }

    @Override
    public Documento findById(Long id) {
        return manager.find(Documento.class, id);
    }

    @Override
    public List<Documento> findByTexto(String texto) {
        String jpql = "SELECT d FROM Documento d WHERE LOWER(d.numero) LIKE CONCAT('%',LOWER(:numero) ,'%')";

        return manager.createQuery(jpql, Documento.class)
            .setParameter("numero", texto)
            .getResultList();
    }

    @Override
    public Documento persist(Documento documento) {
        manager.getTransaction().begin();
        manager.persist(documento);
        manager.getTransaction().commit();

        return documento;
    }

    @Override
    public Documento update(Documento documento) {
        manager.getTransaction().begin();
        manager.merge(documento);
        manager.getTransaction().commit();

        return documento;
    }

    @Override
    public boolean delete(Documento documento) {
        manager.getTransaction().begin();
        manager.remove(documento);
        manager.getTransaction().commit();

        return true;
    }
}
