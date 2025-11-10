package com.educamais.app.dtos;

import java.util.List;

import com.educamais.app.enums.TipoQuestao;
import com.educamais.app.model.Questao;

public record QuestaoAlunoDTO(
    Long id,
    String enunciado,
    String imagemUrl,
    String disciplina,
    TipoQuestao tipo,
    double pontuacao,
    List<String> alternativas
) {

    public QuestaoAlunoDTO(Questao questao){
        this(questao.getId(), questao.getEnunciado(), questao.getImagemUrl(), questao.getDisciplina(), questao.getTipo(), questao.getPontuacao(), questao.getAlternativas());
    }

}
