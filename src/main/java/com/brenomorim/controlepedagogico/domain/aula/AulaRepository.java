package com.brenomorim.controlepedagogico.domain.aula;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AulaRepository extends JpaRepository<Aula, Long> {

    @Query("SELECT a FROM Aula a WHERE a.data >= :dataInicial AND a.data <= :dataFinal")
    Page<Aula> getAulasNoIntervalo(LocalDateTime dataInicial, LocalDateTime dataFinal, Pageable paginacao);

    @Query("SELECT a FROM Aula a WHERE a.data >= :dataInicial AND a.data <= :dataFinal AND a.professor.id=:professorId")
    Page<Aula> getAulasNoIntervaloPorProfessor(LocalDateTime dataInicial, LocalDateTime dataFinal, Long professorId, Pageable paginacao);

    @Query("SELECT a FROM Aula a WHERE a.data >= :dataInicial AND a.data <= :dataFinal AND a.aluno.id=:alunoId")
    Page<Aula> getAulasNoIntervaloPorAluno(LocalDateTime dataInicial, LocalDateTime dataFinal, Long alunoId, Pageable paginacao);

    @Query("SELECT a FROM Aula a WHERE a.data >= :dataInicial AND a.data <= :dataFinal AND a.aluno.id=:alunoId AND a.professor.id=:professorId")
    Page<Aula> getAulasNoIntervaloPorAlunoEProfessor(LocalDateTime dataInicial, LocalDateTime dataFinal, Long alunoId, Long professorId, Pageable paginacao);

    @Query("SELECT a FROM Aula a WHERE a.data >= :dataInicial AND a.data <= :dataFinal AND a.aluno.id = :alunoId")
    List<Aula> getAulasDoAlunoPorDataEHorario(LocalDateTime dataInicial, LocalDateTime dataFinal, Long alunoId);

}
