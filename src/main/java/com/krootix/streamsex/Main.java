package com.krootix.streamsex;

import com.krootix.streamsex.creation.ObjectCreator;
import com.krootix.streamsex.stream.StreamExample;

public class Main {

    public static void main(String[] args) {

        ObjectCreator objectCreator = new ObjectCreator();
        StreamExample streamExample = new StreamExample(objectCreator);
        streamExample.run();

    }
}