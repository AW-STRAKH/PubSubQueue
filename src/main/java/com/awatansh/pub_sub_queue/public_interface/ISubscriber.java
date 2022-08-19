package com.awatansh.pub_sub_queue.public_interface;

import com.awatansh.pub_sub_queue.model.Message;

//how a consumer looks like
public interface ISubscriber {

    String getId();
    //api to consume a message
    void consume(Message message) throws InterruptedException;
}
