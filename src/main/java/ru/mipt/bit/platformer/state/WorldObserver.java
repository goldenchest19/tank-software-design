package ru.mipt.bit.platformer.state;

import ru.mipt.bit.platformer.model.BaseModel;

public interface WorldObserver {
    void onObjectAdded(BaseModel model);

    void onObjectRemoved(BaseModel model);
}
