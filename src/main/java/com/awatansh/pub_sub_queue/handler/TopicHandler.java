package com.awatansh.pub_sub_queue.handler;

import com.awatansh.pub_sub_queue.model.Topic;
import com.awatansh.pub_sub_queue.model.TopicSubscriber;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

public class TopicHandler {
    private final Topic topic;
    private final Map<String, SubscriberWorker> subscriberWorkers;

    public TopicHandler(@NonNull final Topic topic) {
        this.topic = topic;
        subscriberWorkers = new HashMap<>();
    }

    public void publish() {
        for (TopicSubscriber topicSubscriber:topic.getSubscribers()) {
            startSubsriberWorker(topicSubscriber);
        }
    }

    //Multiple workers for each subscriber
    // UNTIL THERE ARE MESSAGE TO PROCESS THE SUBSCRIBER WORKER WILL BE ACTIVE
    public void startSubsriberWorker(@NonNull final TopicSubscriber topicSubscriber) {
        final String subscriberId = topicSubscriber.getSubscriber().getId();
        if (!subscriberWorkers.containsKey(subscriberId)) {
            final SubscriberWorker subscriberWorker = new SubscriberWorker(topic, topicSubscriber);
            subscriberWorkers.put(subscriberId, subscriberWorker);
            new Thread(subscriberWorker).start();
        }
        final SubscriberWorker subscriberWorker = subscriberWorkers.get(subscriberId);
        subscriberWorker.wakeUpIfNeeded();
    }
}
