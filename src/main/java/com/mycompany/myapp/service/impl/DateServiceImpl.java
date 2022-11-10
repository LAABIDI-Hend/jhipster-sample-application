package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Date;
import com.mycompany.myapp.repository.DateRepository;
import com.mycompany.myapp.service.DateService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Date}.
 */
@Service
@Transactional
public class DateServiceImpl implements DateService {

    private final Logger log = LoggerFactory.getLogger(DateServiceImpl.class);

    private final DateRepository dateRepository;

    public DateServiceImpl(DateRepository dateRepository) {
        this.dateRepository = dateRepository;
    }

    @Override
    public Date save(Date date) {
        log.debug("Request to save Date : {}", date);
        return dateRepository.save(date);
    }

    @Override
    public Date update(Date date) {
        log.debug("Request to update Date : {}", date);
        return dateRepository.save(date);
    }

    @Override
    public Optional<Date> partialUpdate(Date date) {
        log.debug("Request to partially update Date : {}", date);

        return dateRepository
            .findById(date.getId())
            .map(existingDate -> {
                if (date.getDay() != null) {
                    existingDate.setDay(date.getDay());
                }
                if (date.getMonth() != null) {
                    existingDate.setMonth(date.getMonth());
                }
                if (date.getYear() != null) {
                    existingDate.setYear(date.getYear());
                }

                return existingDate;
            })
            .map(dateRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Date> findAll() {
        log.debug("Request to get all Dates");
        return dateRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Date> findOne(Long id) {
        log.debug("Request to get Date : {}", id);
        return dateRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Date : {}", id);
        dateRepository.deleteById(id);
    }
}
