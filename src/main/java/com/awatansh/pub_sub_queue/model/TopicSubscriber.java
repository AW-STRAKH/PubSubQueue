package com.awatansh.pub_sub_queue.model;

import com.awatansh.pub_sub_queue.public_interface.ISubscriber;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@AllArgsConstructor
public class TopicSubscriber {
    //ATOMIC INTEGER IS USED TO HANDLE THE CASE FOR MULTI THREADING
    private final AtomicInteger offset;
    private final ISubscriber subscriber;

    public TopicSubscriber(@NonNull final ISubscriber subscriber) {
        this.subscriber = subscriber;
        this.offset = new AtomicInteger(0);
    }
}
