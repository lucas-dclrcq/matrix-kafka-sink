package me.ldclrcq.kafka.matrix.sink;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

@ApplicationScoped
public class IncomingMatrixMessagesProcessor {
    private static final Logger logger = Logger.getLogger(IncomingMatrixMessagesProcessor.class);

    private final MatrixAPI matrixAPI;

    public IncomingMatrixMessagesProcessor(@RestClient MatrixAPI matrixAPI) {
        this.matrixAPI = matrixAPI;
    }

    @Incoming("incoming_matrix_messages")
    public Uni<Void> process(ConsumerRecord<IncomingMatrixMessageKey, IncomingMatrixMessage> record) {
        MatrixMessage matrixMessage = MatrixMessage.fromIncoming(record.value());
        String transactionid = Long.toString(record.timestamp());

        return this.matrixAPI
                .sendMessage(record.key().getChannelId(), transactionid, matrixMessage)
                .onFailure().invoke(throwable -> logger.errorf(throwable, "Could not send matrix message to room %s", record.key()))
                .onFailure().recoverWithNull()
                .replaceWithVoid();
    }
}
