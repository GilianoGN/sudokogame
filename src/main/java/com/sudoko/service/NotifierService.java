package com.sudoko.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sudoko.service.EventEnum.CLEAR_SPACE;

public class NotifierService {

    private Map<EventEnum, List<EventListener>> listeners = new HashMap<>(){{
        put(CLEAR_SPACE, new ArrayList<>());
    }};

    public void subscribe(final EventEnum eventType, EventListener listener){
        List<EventListener> selectedListeners = listeners.get(eventType);
        selectedListeners.add(listener);
    }

    public void notify(final EventEnum eventType){
        listeners.get(eventType).forEach(l -> l.update(eventType));
    }
}