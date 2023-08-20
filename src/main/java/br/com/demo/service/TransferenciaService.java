package br.com.demo.service;

import br.com.demo.dao.NotificacaoDao;
import br.com.demo.dao.TransferenciaDao;
import br.com.demo.dao.UsuarioDao;
import br.com.demo.exception.BadRequestException;
import br.com.demo.model.TransferenciaModel;
import br.com.demo.model.UsuarioModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferenciaService {

    private final TransferenciaDao transferenciaDao;
    private final NotificacaoDao notificacaoDao;
    private final UsuarioDao usuarioDao;

    public void transferir(TransferenciaModel transferencia) {
        UsuarioModel usuario = usuarioDao.buscarUsuarioPorId(transferencia.getPagador());
        transferenciaDao.validarTransferencia();
        transferenciaDao.validarTipoUsuario(usuario);
        transferenciaDao.validarSaldo(transferencia, usuario);
        transferenciaDao.registrarTransacao(transferencia);
        transferenciaDao.atualizaSaldoPagador(transferencia);
        transferenciaDao.atualizaSaldoRecebedor(transferencia);
        notificacaoDao.registrarNotificacao(transferencia, usuario);
        enviaNotificacao(transferencia.getRecebedor());
    }


    public void enviaNotificacao(Integer recebedor) {
        try {
            CompletableFuture.runAsync(() -> {
                HttpClient httpClient = HttpClient.newHttpClient();

                // Crie uma requisição GET para a URL da API
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://o4d9z.mocklab.io/notify"))
                        .build();

                // Envie a requisição de forma assíncrona
                CompletableFuture<HttpResponse<String>> responseFuture = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

                responseFuture.thenAccept(response -> {
                    if (response != null) {
                        if (response.statusCode() == 200) {
                            notificacaoDao.atualizaStatusNotificacao(recebedor);
                            log.info("Requisição bem-sucedida! Mensagem: " + response.body());
                        } else {
                            log.info("Requisição não autorizada ou falha. Código de status: " + response.statusCode());
                        }
                    } else {
                        log.info("A requisição excedeu o tempo limite.");
                    }
                });
            });

        } catch (Exception e) {
            log.error("Erro ao enviar notificação: {}", e);
            throw new BadRequestException("Erro ao enviar notificação.");
        }
    }
}
