package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.Movie;
import com.devsuperior.dsmovie.entities.Score;
import com.devsuperior.dsmovie.entities.User;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class ScoreService {

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Transactional
    public void saveScore(ScoreDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElse(null);
        if (Objects.isNull(user)) {
            user = new User();
            user.setEmail(dto.getEmail());
            user = userRepository.saveAndFlush(user);
        }

        Movie movie = movieRepository.findById(dto.getMovieId()).orElse(null);
        if (Objects.nonNull(movie)) {
            Score score = new Score();
            score.setMovie(movie);
            score.setUser(user);
            score.setValue(dto.getScore());
            scoreRepository.saveAndFlush(score);

            double sum = 0.0;
            for (Score s : movie.getScores()) {
                sum = sum + s.getValue();
            }
            double avg = sum / movie.getScores().size();

            movie.setScore(avg);
            movie.setCount(movie.getScores().size());
            movieRepository.save(movie);
        }
    }
}
