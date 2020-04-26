package com.codingman.statemachine.configuration;

import com.codingman.statemachine.event.HealthEvent;
import com.codingman.statemachine.state.HealthState;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

@Configuration
@EnableStateMachine
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<HealthState, HealthEvent> {
    @Override
    public void configure(StateMachineConfigurationConfigurer<HealthState, HealthEvent> config) throws Exception {
        config.withConfiguration()
                .autoStartup(true)
                .listener(listener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<HealthState, HealthEvent> states) throws Exception {
        states.withStates()
                .initial(HealthState.FULL)
                .states(EnumSet.allOf(HealthState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<HealthState, HealthEvent> transitions) throws Exception {
        transitions
                .withExternal()
                // 满血被攻击
                .source(HealthState.FULL).target(HealthState.HURT).event(HealthEvent.ATTACK)
                .and()
                .withExternal()
                // 满血被暴击打死
                .source(HealthState.FULL).target(HealthState.DEAD).event(HealthEvent.KILL)
                .and()
                .withExternal()
                // 残血被打死
                .source(HealthState.HURT).target(HealthState.DEAD).event(HealthEvent.KILL)
                .and()
                .withExternal()
                // 只有死了才能重生
                .source(HealthState.DEAD).target(HealthState.FULL).event(HealthEvent.REBORN)
                .and()
                .withExternal()
                // 受伤了可以加到满血
                .source(HealthState.HURT).target(HealthState.FULL).event(HealthEvent.HEAL)
                .and()
                .withExternal()
                // 满血也可以加血
                .source(HealthState.FULL).target(HealthState.FULL).event(HealthEvent.HEAL);
    }

    @Bean
    public StateMachineListener<HealthState, HealthEvent> listener() {
        return new StateMachineListenerAdapter<HealthState, HealthEvent>() {
            @Override
            public void stateChanged(State<HealthState, HealthEvent> from, State<HealthState, HealthEvent> to) {
                System.out.println("from = " + (from != null ? from.getId() : "null") + ", to = " + to.getId());
            }
        };
    }
}
