package newanalytics.bugpattern;

import java.util.*;
import newanalytics.IssueFinder;
import newanalytics.IssueReport;
import scratch.ast.model.ASTNode;
import scratch.ast.model.ActorDefinition;
import scratch.ast.model.Program;
import scratch.ast.model.event.ReceptionOfMessage;
import scratch.ast.model.literals.StringLiteral;
import scratch.ast.model.statement.common.Broadcast;
import scratch.ast.model.statement.common.BroadcastAndWait;
import scratch.ast.visitor.ScratchVisitor;

public class MessageNeverReceived implements IssueFinder, ScratchVisitor {

    public static final String NAME = "never_receive_message";
    public static final String SHORT_NAME = "nvrrcv";

    private List<Pair> messageSent = new ArrayList<>();
    private List<Pair> messageReceived = new ArrayList<>();
    private ActorDefinition currentActor;

    @Override
    public IssueReport check(Program program) {
        program.accept(this);

        final LinkedHashSet<Pair> nonSyncedPairs = new LinkedHashSet<>();
        final LinkedHashSet<Pair> syncedPairs = new LinkedHashSet<>();

        for (Pair sent : messageSent) {
            boolean isReceived = false;
            for (Pair received : messageReceived) {
                if (sent.msgName.equals(received.msgName)) {
                    isReceived = true;
                    break;
                }
            }
            if (!isReceived) {
                nonSyncedPairs.add(sent);
            } else {
                syncedPairs.add(sent);
            }
        }

        final List<String> actorNames = new LinkedList<>();
        nonSyncedPairs.forEach(p -> actorNames.add(p.getActorName()));

        return new IssueReport(NAME, nonSyncedPairs.size(), actorNames, "");
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void visit(ActorDefinition actor) {
        currentActor = actor;
        if (!actor.getChildren().isEmpty()) {
            for (ASTNode child : actor.getChildren()) {
                child.accept(this);
            }
        }
    }

    @Override
    public void visit(Broadcast node) {
        if (node.getMessage().getMessage() instanceof StringLiteral) {
            final String actorName = currentActor.getIdent().getName();
            final String msgName = ((StringLiteral) node.getMessage().getMessage()).getText();
            messageSent.add(new Pair(actorName, msgName));
        }
    }

    @Override
    public void visit(BroadcastAndWait node) {
        if (node.getMessage().getMessage() instanceof StringLiteral) {
            final String actorName = currentActor.getIdent().getName();
            final String msgName = ((StringLiteral) node.getMessage().getMessage()).getText();
            messageSent.add(new Pair(actorName, msgName));
        }
    }


    @Override
    public void visit(ReceptionOfMessage node) {
        if (node.getMsg().getMessage() instanceof StringLiteral) {
            final String actorName = currentActor.getIdent().getName();
            final String msgName = ((StringLiteral) node.getMsg().getMessage()).getText();
            messageReceived.add(new Pair(actorName, msgName));
        }
    }

    /**
     * Helper class to map which messages are sent / received by which actor
     */
    private static class Pair {
        String msgName;
        private String actorName;

        public Pair(String actorName, String msgName) {
            this.setActorName(actorName);
            this.msgName = msgName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Pair pair = (Pair) o;

            if (!Objects.equals(getActorName(), pair.getActorName())) {
                return false;
            }
            return Objects.equals(msgName, pair.msgName);
        }

        @Override
        public int hashCode() {
            int result = getActorName() != null ? getActorName().hashCode() : 0;
            result = 31 * result + (msgName != null ? msgName.hashCode() : 0);
            return result;
        }

        String getActorName() {
            return actorName;
        }

        void setActorName(String actorName) {
            this.actorName = actorName;
        }
    }
}
