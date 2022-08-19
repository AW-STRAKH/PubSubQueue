package com.awatansh.pub_sub_queue.public_interface;

import com.awatansh.pub_sub_queue.handler.TopicHandler;
import com.awatansh.pub_sub_queue.model.Message;
import com.awatansh.pub_sub_queue.model.Topic;
import com.awatansh.pub_sub_queue.model.TopicSubscriber;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
//apis built
public class Queue {
    private final Map<String, TopicHandler> topicProcessors;

    public Queue() {
        this.topicProcessors = new HashMap<>();
    }

    //api creates a topic
    public Topic createTopic(@NonNull final String topicName) {
        final Topic topic = new Topic(topicName, UUID.randomUUID().toString());
        TopicHandler topicHandler = new TopicHandler(topic);
        topicProcessors.put(topic.getTopicId(), topicHandler);
        System.out.println("Created topic: " + topic.getTopicName());
        return topic;
    }

    //sbscribe a topic
    public void subscribe(@NonNull final ISubscriber subscriber, @NonNull final Topic topic) {
        topic.addSubscriber(new TopicSubscriber(subscriber));
        System.out.println(subscriber.getId() + " subscribed to topic: " + topic.getTopicName());
    }

    //publish in a topic TO ALL THE SUBSCRIBERS
    public void publish(@NonNull final Topic topic, @NonNull final Message message) {
        topic.addMessage(message);
        System.out.println(message.getMsg() + " published to topic: " + topic.getTopicName());
        //NEW THREAD SO AS TO NOT BLOCK A MAIN CALL
        new Thread(() -> topicProcessors.get(topic.getTopicId()).publish()).start();
    }

    //resetting the offset of api
    public void resetOffset(@NonNull final Topic topic, @NonNull final ISubscriber subscriber, @NonNull final Integer newOffset) {
        for (TopicSubscriber topicSubscriber : topic.getSubscribers()) {
            if (topicSubscriber.getSubscriber().equals(subscriber)) {
                topicSubscriber.getOffset().set(newOffset);
                System.out.println(topicSubscriber.getSubscriber().getId() + " offset reset to: " + newOffset);
                new Thread(() -> topicProcessors.get(topic.getTopicId()).startSubsriberWorker(topicSubscriber)).start();
                break;
            }
        }
    }
}
