package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Date;
import com.mycompany.myapp.repository.DateRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DateResourceIT {

    private static final String DEFAULT_DAY = "AAAAAAAAAA";
    private static final String UPDATED_DAY = "BBBBBBBBBB";

    private static final String DEFAULT_MONTH = "AAAAAAAAAA";
    private static final String UPDATED_MONTH = "BBBBBBBBBB";

    private static final String DEFAULT_YEAR = "AAAAAAAAAA";
    private static final String UPDATED_YEAR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DateRepository dateRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDateMockMvc;

    private Date date;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Date createEntity(EntityManager em) {
        Date date = new Date().day(DEFAULT_DAY).month(DEFAULT_MONTH).year(DEFAULT_YEAR);
        return date;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Date createUpdatedEntity(EntityManager em) {
        Date date = new Date().day(UPDATED_DAY).month(UPDATED_MONTH).year(UPDATED_YEAR);
        return date;
    }

    @BeforeEach
    public void initTest() {
        date = createEntity(em);
    }

    @Test
    @Transactional
    void createDate() throws Exception {
        int databaseSizeBeforeCreate = dateRepository.findAll().size();
        // Create the Date
        restDateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(date)))
            .andExpect(status().isCreated());

        // Validate the Date in the database
        List<Date> dateList = dateRepository.findAll();
        assertThat(dateList).hasSize(databaseSizeBeforeCreate + 1);
        Date testDate = dateList.get(dateList.size() - 1);
        assertThat(testDate.getDay()).isEqualTo(DEFAULT_DAY);
        assertThat(testDate.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testDate.getYear()).isEqualTo(DEFAULT_YEAR);
    }

    @Test
    @Transactional
    void createDateWithExistingId() throws Exception {
        // Create the Date with an existing ID
        date.setId(1L);

        int databaseSizeBeforeCreate = dateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(date)))
            .andExpect(status().isBadRequest());

        // Validate the Date in the database
        List<Date> dateList = dateRepository.findAll();
        assertThat(dateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDates() throws Exception {
        // Initialize the database
        dateRepository.saveAndFlush(date);

        // Get all the dateList
        restDateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(date.getId().intValue())))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY)))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)));
    }

    @Test
    @Transactional
    void getDate() throws Exception {
        // Initialize the database
        dateRepository.saveAndFlush(date);

        // Get the date
        restDateMockMvc
            .perform(get(ENTITY_API_URL_ID, date.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(date.getId().intValue()))
            .andExpect(jsonPath("$.day").value(DEFAULT_DAY))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR));
    }

    @Test
    @Transactional
    void getNonExistingDate() throws Exception {
        // Get the date
        restDateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDate() throws Exception {
        // Initialize the database
        dateRepository.saveAndFlush(date);

        int databaseSizeBeforeUpdate = dateRepository.findAll().size();

        // Update the date
        Date updatedDate = dateRepository.findById(date.getId()).get();
        // Disconnect from session so that the updates on updatedDate are not directly saved in db
        em.detach(updatedDate);
        updatedDate.day(UPDATED_DAY).month(UPDATED_MONTH).year(UPDATED_YEAR);

        restDateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDate))
            )
            .andExpect(status().isOk());

        // Validate the Date in the database
        List<Date> dateList = dateRepository.findAll();
        assertThat(dateList).hasSize(databaseSizeBeforeUpdate);
        Date testDate = dateList.get(dateList.size() - 1);
        assertThat(testDate.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testDate.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testDate.getYear()).isEqualTo(UPDATED_YEAR);
    }

    @Test
    @Transactional
    void putNonExistingDate() throws Exception {
        int databaseSizeBeforeUpdate = dateRepository.findAll().size();
        date.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, date.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(date))
            )
            .andExpect(status().isBadRequest());

        // Validate the Date in the database
        List<Date> dateList = dateRepository.findAll();
        assertThat(dateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDate() throws Exception {
        int databaseSizeBeforeUpdate = dateRepository.findAll().size();
        date.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(date))
            )
            .andExpect(status().isBadRequest());

        // Validate the Date in the database
        List<Date> dateList = dateRepository.findAll();
        assertThat(dateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDate() throws Exception {
        int databaseSizeBeforeUpdate = dateRepository.findAll().size();
        date.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(date)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Date in the database
        List<Date> dateList = dateRepository.findAll();
        assertThat(dateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDateWithPatch() throws Exception {
        // Initialize the database
        dateRepository.saveAndFlush(date);

        int databaseSizeBeforeUpdate = dateRepository.findAll().size();

        // Update the date using partial update
        Date partialUpdatedDate = new Date();
        partialUpdatedDate.setId(date.getId());

        partialUpdatedDate.day(UPDATED_DAY).year(UPDATED_YEAR);

        restDateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDate))
            )
            .andExpect(status().isOk());

        // Validate the Date in the database
        List<Date> dateList = dateRepository.findAll();
        assertThat(dateList).hasSize(databaseSizeBeforeUpdate);
        Date testDate = dateList.get(dateList.size() - 1);
        assertThat(testDate.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testDate.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testDate.getYear()).isEqualTo(UPDATED_YEAR);
    }

    @Test
    @Transactional
    void fullUpdateDateWithPatch() throws Exception {
        // Initialize the database
        dateRepository.saveAndFlush(date);

        int databaseSizeBeforeUpdate = dateRepository.findAll().size();

        // Update the date using partial update
        Date partialUpdatedDate = new Date();
        partialUpdatedDate.setId(date.getId());

        partialUpdatedDate.day(UPDATED_DAY).month(UPDATED_MONTH).year(UPDATED_YEAR);

        restDateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDate))
            )
            .andExpect(status().isOk());

        // Validate the Date in the database
        List<Date> dateList = dateRepository.findAll();
        assertThat(dateList).hasSize(databaseSizeBeforeUpdate);
        Date testDate = dateList.get(dateList.size() - 1);
        assertThat(testDate.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testDate.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testDate.getYear()).isEqualTo(UPDATED_YEAR);
    }

    @Test
    @Transactional
    void patchNonExistingDate() throws Exception {
        int databaseSizeBeforeUpdate = dateRepository.findAll().size();
        date.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, date.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(date))
            )
            .andExpect(status().isBadRequest());

        // Validate the Date in the database
        List<Date> dateList = dateRepository.findAll();
        assertThat(dateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDate() throws Exception {
        int databaseSizeBeforeUpdate = dateRepository.findAll().size();
        date.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(date))
            )
            .andExpect(status().isBadRequest());

        // Validate the Date in the database
        List<Date> dateList = dateRepository.findAll();
        assertThat(dateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDate() throws Exception {
        int databaseSizeBeforeUpdate = dateRepository.findAll().size();
        date.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDateMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(date)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Date in the database
        List<Date> dateList = dateRepository.findAll();
        assertThat(dateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDate() throws Exception {
        // Initialize the database
        dateRepository.saveAndFlush(date);

        int databaseSizeBeforeDelete = dateRepository.findAll().size();

        // Delete the date
        restDateMockMvc
            .perform(delete(ENTITY_API_URL_ID, date.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Date> dateList = dateRepository.findAll();
        assertThat(dateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
