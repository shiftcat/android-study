package com.example.android.board;

import com.example.android.models.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class BoardRepository {


    private EntityManager em;


    public BoardRepository(EntityManager em)
    {
        this.em = em;
    }


    public Board save(Board board)
    {
        em.persist(board);
        return board;
    }


    public void mearge(Board board)
    {
        em.merge(board);
    }


    public List<Board> findAllBy(Pageable pageable)
    {
        String query = "SELECT b FROM Board b LEFT JOIN FETCH b.thumbnailImage ORDER BY b.id DESC";

        return em.createQuery(query)
                .setFirstResult((int)pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }


    public List<Board> findBySubjectLike(String subject, Pageable pageable)
    {
        String query = "SELECT b FROM Board b LEFT JOIN FETCH b.thumbnailImage WHERE b.subject LIKE :subject ORDER BY b.id DESC";

        return em.createQuery(query)
                .setParameter("subject", subject)
                .setFirstResult((int)pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }



    public Optional<Board> findOne(Long id)
    {
        Board board = em.find(Board.class, id);
        return Optional.ofNullable(board);
    }



    public void delete(Board board)
    {
        em.remove(board);
    }


}
