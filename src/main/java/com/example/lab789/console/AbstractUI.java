package com.example.lab789.console;

import com.example.lab789.service.ServiceI;

public abstract class AbstractUI implements UI {
    ServiceI srv;

    public AbstractUI(ServiceI srv) {
        this.srv = srv;
    }
}