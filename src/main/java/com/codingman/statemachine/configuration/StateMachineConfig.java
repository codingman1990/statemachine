package com.codingman.statemachine.configuration;

import com.codingman.statemachine.event.HealthEvent;
import com.codingman.statemachine.state.HealthState;

import org.springframework.context.annotation.Bean;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<HealthState, HealthEvent> {
    @Override
    public void configure(StateMachineConfigurationConfigurer<HealthState, HealthEvent> config) throws Exception {
        config.withConfiguration()
                .autoStartup(true)
                .listener(listener());
    }

    @Bean
    public StateMachineListener<HealthState, HealthEvent> listener() {
        return new StateMachineListenerAdapter<HealthState, HealthEvent>() {
            @Override
            public void stateChanged(State<HealthState, HealthEvent> from, State<HealthState, HealthEvent> to) {
                System.out.println("from = " + from.getId() + ", to = " + to.getId());
            }
        };
    }
}
