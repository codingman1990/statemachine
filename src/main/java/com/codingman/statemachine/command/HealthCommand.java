package com.codingman.statemachine.command;

import com.codingman.statemachine.event.HealthEvent;
import com.codingman.statemachine.state.HealthState;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.statemachine.StateMachine;

import java.util.concurrent.atomic.AtomicLong;

@ShellComponent
public class HealthCommand {
    @Autowired
    private StateMachine<HealthState, HealthEvent> stateMachine;
    private AtomicLong health = new AtomicLong(1000);

    @ShellMethod("reborn")
    public String reborn() {
        boolean reborn = stateMachine.sendEvent(HealthEvent.REBORN);
        if (reborn) {
            health.set(1000);
            return "OK";
        }
        return "FAIL";
    }

    @ShellMethod("attack")
    public long attack(long damage) {
        long blood = health.accumulateAndGet(damage, (left, right) -> left - right < 0 ? 0 : left - right);
        if (blood == 0) {
            stateMachine.sendEvent(HealthEvent.KILL);
        } else {
            stateMachine.sendEvent(HealthEvent.ATTACK);
        }
        return blood;
    }

    @ShellMethod("heal")
    public long heal(long heal) {
        long blood = health.accumulateAndGet(heal, (left, right) -> {
            if (left <= 0) {
                return left;
            } else if (left + right > 1000L) {
                return 1000L;
            } else {
                return left + right;
            }
        });
        stateMachine.sendEvent(HealthEvent.HEAL);
        return blood;
    }

    @ShellMethod("get")
    public long get() {
        return health.get();
    }
}
