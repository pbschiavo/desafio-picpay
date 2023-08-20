package br.com.demo.scheduled;

import br.com.demo.dao.NotificacaoDao;
import br.com.demo.model.NotificacaoModel;
import br.com.demo.service.TransferenciaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTask {

    private final TransferenciaService transferenciaService;
    private final NotificacaoDao notificacaoDao;

    @Scheduled(fixedRate = 120000) // Executa a cada 2 minutos (120000 milissegundos)
    public void executeTask() {
        List<NotificacaoModel> notificacao = notificacaoDao.listarNotificacoesPendentes();
        notificacao.forEach( n -> transferenciaService.enviaNotificacao(n.getRecebedor()));
        log.info("Tarefa agendada executada a cada 2 minutos.");
    }
}
