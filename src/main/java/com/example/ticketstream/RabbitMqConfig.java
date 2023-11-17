package com.example.ticketstream;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
	
    @Bean
    Queue movieGetByTitleQueue() {
        return new Queue("movieGetByTitle.rpc.requests");
    }

    @Bean
    DirectExchange movieGetByTitleExchange() {
        return new DirectExchange("movieGetByTitle.rpc");
    }

    @Bean
    Binding movieGetByTitleBinding(DirectExchange movieGetByTitleExchange, Queue movieGetByTitleQueue) {
        return BindingBuilder.bind(movieGetByTitleQueue)
            .to(movieGetByTitleExchange)
            .with("rpc");
    }
    
    @Bean
    Queue theaterGetByNameQueue() {
        return new Queue("theaterGetByName.rpc.requests");
    }

    @Bean
    DirectExchange theaterGetByNameExchange() {
        return new DirectExchange("theaterGetByName.rpc");
    }

    @Bean
    Binding theaterGetByNameBinding(DirectExchange theaterGetByNameExchange, Queue theaterGetByNameQueue) {
        return BindingBuilder.bind(theaterGetByNameQueue)
            .to(theaterGetByNameExchange)
            .with("rpc");
    }
    
    @Bean
    Queue screeningGetMoviesPlayingQueue() {
        return new Queue("screeningGetMoviesPlaying.rpc.requests");
    }

    @Bean
    DirectExchange screeningGetMoviesPlayingExchange() {
        return new DirectExchange("screeningGetMoviesPlaying.rpc");
    }

    @Bean
    Binding screeningGetMoviesPlayingBinding(DirectExchange screeningGetMoviesPlayingExchange, Queue screeningGetMoviesPlayingQueue) {
        return BindingBuilder.bind(screeningGetMoviesPlayingQueue)
            .to(screeningGetMoviesPlayingExchange)
            .with("rpc");
    }
    
    @Bean
    Queue screeningGetTheatersPlayingMovieQueue() {
        return new Queue("screeningGetTheatersPlayingMovie.rpc.requests");
    }

    @Bean
    DirectExchange screeningGetTheatersPlayingMovieExchange() {
        return new DirectExchange("screeningGetTheatersPlayingMovie.rpc");
    }

    @Bean
    Binding screeningGetTheatersPlayingMovieBinding(DirectExchange screeningGetTheatersPlayingMovieExchange, Queue screeningGetTheatersPlayingMovieQueue) {
        return BindingBuilder.bind(screeningGetTheatersPlayingMovieQueue)
            .to(screeningGetTheatersPlayingMovieExchange)
            .with("rpc");
    }
    
    @Bean
    Queue screeningGetScreeningsPlayingMovieInTheaterQueue() {
        return new Queue("screeningGetScreeningsPlayingMovieInTheater.rpc.requests");
    }

    @Bean
    DirectExchange screeningGetScreeningsPlayingMovieInTheaterExchange() {
        return new DirectExchange("screeningGetScreeningsPlayingMovieInTheater.rpc");
    }

    @Bean
    Binding screeningGetScreeningsPlayingMovieInTheaterBinding(DirectExchange screeningGetScreeningsPlayingMovieInTheaterExchange, Queue screeningGetScreeningsPlayingMovieInTheaterQueue) {
        return BindingBuilder.bind(screeningGetScreeningsPlayingMovieInTheaterQueue)
            .to(screeningGetScreeningsPlayingMovieInTheaterExchange)
            .with("rpc");
    }
    
    @Bean
    Queue screeningBuyTicketsQueue() {
        return new Queue("screeningBuyTickets.rpc.requests");
    }

    @Bean
    DirectExchange screeningBuyTicketsExchange() {
        return new DirectExchange("screeningBuyTickets.rpc");
    }

    @Bean
    Binding screeningBuyTicketsBinding(DirectExchange screeningBuyTicketsExchange, Queue screeningBuyTicketsQueue) {
        return BindingBuilder.bind(screeningBuyTicketsQueue)
            .to(screeningBuyTicketsExchange)
            .with("rpc");
    }
    
}
