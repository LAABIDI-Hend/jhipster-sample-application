package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Date;
import com.mycompany.myapp.repository.DateRepository;
import com.mycompany.myapp.service.DateService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Date}.
 */
@RestController
@RequestMapping("/api")
public class DateResource {

    private final Logger log = LoggerFactory.getLogger(DateResource.class);

    private static final String ENTITY_NAME = "date";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DateService dateService;

    private final DateRepository dateRepository;

    public DateResource(DateService dateService, DateRepository dateRepository) {
        this.dateService = dateService;
        this.dateRepository = dateRepository;
    }

    /**
     * {@code POST  /dates} : Create a new date.
     *
     * @param date the date to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new date, or with status {@code 400 (Bad Request)} if the date has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dates")
    public ResponseEntity<Date> createDate(@RequestBody Date date) throws URISyntaxException {
        log.debug("REST request to save Date : {}", date);
        if (date.getId() != null) {
            throw new BadRequestAlertException("A new date cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Date result = dateService.save(date);
        return ResponseEntity
            .created(new URI("/api/dates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dates/:id} : Updates an existing date.
     *
     * @param id the id of the date to save.
     * @param date the date to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated date,
     * or with status {@code 400 (Bad Request)} if the date is not valid,
     * or with status {@code 500 (Internal Server Error)} if the date couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dates/{id}")
    public ResponseEntity<Date> updateDate(@PathVariable(value = "id", required = false) final Long id, @RequestBody Date date)
        throws URISyntaxException {
        log.debug("REST request to update Date : {}, {}", id, date);
        if (date.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, date.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Date result = dateService.update(date);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, date.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /dates/:id} : Partial updates given fields of an existing date, field will ignore if it is null
     *
     * @param id the id of the date to save.
     * @param date the date to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated date,
     * or with status {@code 400 (Bad Request)} if the date is not valid,
     * or with status {@code 404 (Not Found)} if the date is not found,
     * or with status {@code 500 (Internal Server Error)} if the date couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dates/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Date> partialUpdateDate(@PathVariable(value = "id", required = false) final Long id, @RequestBody Date date)
        throws URISyntaxException {
        log.debug("REST request to partial update Date partially : {}, {}", id, date);
        if (date.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, date.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Date> result = dateService.partialUpdate(date);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, date.getId().toString())
        );
    }

    /**
     * {@code GET  /dates} : get all the dates.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dates in body.
     */
    @GetMapping("/dates")
    public List<Date> getAllDates() {
        log.debug("REST request to get all Dates");
        return dateService.findAll();
    }

    /**
     * {@code GET  /dates/:id} : get the "id" date.
     *
     * @param id the id of the date to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the date, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dates/{id}")
    public ResponseEntity<Date> getDate(@PathVariable Long id) {
        log.debug("REST request to get Date : {}", id);
        Optional<Date> date = dateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(date);
    }

    /**
     * {@code DELETE  /dates/:id} : delete the "id" date.
     *
     * @param id the id of the date to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dates/{id}")
    public ResponseEntity<Void> deleteDate(@PathVariable Long id) {
        log.debug("REST request to delete Date : {}", id);
        dateService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
