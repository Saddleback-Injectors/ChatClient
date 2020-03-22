package edu.saddleback.cs4b.Backend.PubSub;

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
}