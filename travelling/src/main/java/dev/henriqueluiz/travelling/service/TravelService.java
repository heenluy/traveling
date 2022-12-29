package dev.henriqueluiz.travelling.service;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import dev.henriqueluiz.travelling.model.Travel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TravelService {
    Travel saveTravel(Travel entity);
    Travel updateTravel(Long travelId, Travel entity);
    void deleteTravel(Long travelId, String email);
    Travel getById(Long travelId, String email);
    Page<Travel> getAllByUser(String email, Pageable pageable);
}
