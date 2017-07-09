package bj.ats.hm.service.impl;

import bj.ats.hm.service.ReservationService;
import bj.ats.hm.domain.Reservation;
import bj.ats.hm.repository.ReservationRepository;
import bj.ats.hm.repository.search.ReservationSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Reservation.
 */
@Service
@Transactional
public class ReservationServiceImpl implements ReservationService{

    private final Logger log = LoggerFactory.getLogger(ReservationServiceImpl.class);

    private final ReservationRepository reservationRepository;

    private final ReservationSearchRepository reservationSearchRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository, ReservationSearchRepository reservationSearchRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationSearchRepository = reservationSearchRepository;
    }

    /**
     * Save a reservation.
     *
     * @param reservation the entity to save
     * @return the persisted entity
     */
    @Override
    public Reservation save(Reservation reservation) {
        log.debug("Request to save Reservation : {}", reservation);
        Reservation result = reservationRepository.save(reservation);
        reservationSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the reservations.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Reservation> findAll(Pageable pageable) {
        log.debug("Request to get all Reservations");
        return reservationRepository.findAll(pageable);
    }


    /**
     *  get all the reservations where Rooms is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Reservation> findAllWhereRoomsIsNull() {
        log.debug("Request to get all reservations where Rooms is null");
        return StreamSupport
            .stream(reservationRepository.findAll().spliterator(), false)
            .filter(reservation -> reservation.getRooms() == null)
            .collect(Collectors.toList());
    }

    /**
     *  Get one reservation by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Reservation findOne(Long id) {
        log.debug("Request to get Reservation : {}", id);
        return reservationRepository.findOne(id);
    }

    /**
     *  Delete the  reservation by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Reservation : {}", id);
        reservationRepository.delete(id);
        reservationSearchRepository.delete(id);
    }

    /**
     * Search for the reservation corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Reservation> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Reservations for query {}", query);
        Page<Reservation> result = reservationSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}