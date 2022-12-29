package dev.henriqueluiz.travelling.service;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import dev.henriqueluiz.travelling.model.Travel;
import dev.henriqueluiz.travelling.repository.TravelRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class TravelServiceImpl implements TravelService {
    private static final Logger LOG = LoggerFactory.getLogger(TravelServiceImpl.class);
    private final TravelRepo travelRepo;

    @Override
    public Travel saveTravel(Travel entity) {
        LOG.debug("Saving travel for user: '{}'", entity.getUser().getEmail());
        return travelRepo.save(entity);
    }

    @Override
    public Travel updateTravel(Long travelId, Travel entity) {
        if(Objects.isNull(entity.getUser())) {
            throw new IllegalStateException("User cannot be null");
        }
        LOG.debug("Updating travel for user: '{}'", entity.getUser().getEmail());
        Travel travel = travelRepo.findByUser(travelId, entity.getUser().getEmail())
                .orElseThrow(() -> {
                    LOG.error("Entity not found: 'id {}'", travelId);
                    return new EntityNotFoundException(String.format("Entity not found: 'id %s'", travelId));
                });
        travel.setDestination(entity.getDestination());
        travel.setDepartureDate(entity.getDepartureDate());
        travel.setReturnDate(entity.getReturnDate());
        travel.setBudget(entity.getBudget());
        return travel;
    }

    @Override
    public void deleteTravel(Long travelId, String email) {
        LOG.debug("Deleting travel for user: '{}'", email);
        Travel travel = travelRepo.findByUser(travelId, email)
                .orElseThrow(() -> {
                    LOG.error("Entity not found: 'id {}'", travelId);
                    return new EntityNotFoundException(String.format("Entity not found: 'id %s'", travelId));
                });

        travelRepo.delete(travel);
        LOG.debug("Travel has been deleted successfully: '{}'", email);
    }

    @Override
    public Travel getById(Long travelId, String email) {
        LOG.debug("Fetching travel for user: '{}'", email);
        return travelRepo.findByUser(travelId, email)
                .orElseThrow(() -> {
                    LOG.error("Entity not found: 'id {}'", travelId);
                    return new EntityNotFoundException(String.format("Entity not found: 'id %s'", travelId));
                });
    }

    @Override
    public Page<Travel> getAllByUser(String email, Pageable pageable) {
        LOG.debug("Fetching all travels for user: '{}'", email);
        return travelRepo.findAllByUser(email, pageable);
    }
}
