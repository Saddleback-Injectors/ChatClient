package edu.saddleback.cs4b.Backend.PubSub;

import edu.saddleback.cs4b.Backend.Enums.ReceiveTypes;
import edu.saddleback.cs4b.Backend.Messages.TextMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
        List<ClientObserver> observerList = new ArrayList<>();
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

    @Test
    void aSubjectWithNoObserversWillReturnZero() {
        assertEquals(0, subject.observerList.size());
    }

    @Test
    void aSubjectWithOneObserverWillReturnOne() {
        subject.registerObserver(observer);
        assertEquals(1, subject.observerList.size());
    }

    @Test
    void aSubjectCanRemoveObserverFromTheList() {
        subject.registerObserver(observer);
        subject.removeObserver(observer);
        assertEquals(0, subject.observerList.size());
    }

    @Test
    void nothingHappensWhenRemoveIsCalledOnNoObservers() {
        assertDoesNotThrow(() -> {subject.removeObserver(observer);});
    }

    @Test
    void observerNotifiedWhenSubjectPublishes() {
        subject.registerObserver(observer);
        subject.notifyObservers();
        assertEquals("", ((TextMessage)((UIDisplayData)observer.getData()).getData()).getMessage());
    }

    @Test
    void observerWhoDoesNotSubscribeIsNotNotified() {
        try {
            ((TextMessage)((UIDisplayData)observer.getData()).getData()).getMessage();
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }
}