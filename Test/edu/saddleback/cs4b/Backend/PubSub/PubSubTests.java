package edu.saddleback.cs4b.Backend.PubSub;

import edu.saddleback.cs4b.Backend.Enums.ReceiveTypes;
import edu.saddleback.cs4b.Backend.Messages.TextMessage;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PubSubTests {
    private static class Observer implements ClientObserver {
        private Receivable data;
        @Override
        public void update(Receivable data) {
            this.data = data;
        }
        public Receivable getData() { return data; }
    }

    private static class Subject implements ClientSubject {
        List<ClientObserver> observerList;
        @Override
        public void registerObserver(ClientObserver o) {
            observerList.add(o);
        }

        @Override
        public void removeObserver(ClientObserver o) {
            observerList.remove(o);
        }

        @Override
        public void notifyObservers() {
            for (ClientObserver o : observerList) {
                o.update(new UIDisplayData(ReceiveTypes.TEXT_AREA, new TextMessage("", "", ""), ""));
            }
        }
    }

    Observer observer;
    Subject subject;

    @BeforeEach
    void init() {
        observer = new Observer();
        subject = new Subject();
    }
}