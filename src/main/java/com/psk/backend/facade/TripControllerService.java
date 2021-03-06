package com.psk.backend.facade;

import com.psk.backend.domain.common.EntityId;
import com.psk.backend.repository.TripRepository;
import com.psk.backend.domain.trip.value.*;
import com.psk.backend.repository.UserRepository;
import com.psk.backend.service.TripConfirmationService;
import com.psk.backend.service.TripManagementService;
import com.psk.backend.service.TripMergeService;
import io.atlassian.fugue.Try;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class TripControllerService {
    private final TripRepository repository;
    private final TripManagementService service;
    private final UserRepository userRepository;
    private final TripMergeService mergeService;
    private final TripConfirmationService confirmationService;

    public TripControllerService(TripRepository repository,
                                 TripManagementService service,
                                 UserRepository userRepository,
                                 TripMergeService mergeService,
                                 TripConfirmationService confirmationService) {
        this.repository = repository;
        this.service = service;
        this.userRepository = userRepository;
        this.mergeService = mergeService;
        this.confirmationService = confirmationService;
    }

    public Page<TripListView> list(Pageable page) {
        return repository.list(page);
    }

    public Try<EntityId> create(TripCreateForm form) {
        return service.create(form);
    }

    public Try<EntityId> update(String id, TripForm form) {
        return service.update(id, form);
    }

    public Try<TripView> get(String id) {
        return repository.get(id);
    }

    public Try<TripUserView> getUserView(String id, Authentication authentication) {
        return userRepository.findByEmail(authentication.getName())
                .flatMap(u -> repository.tripUserView(id, u.getId()));
    }

    public Try<EntityId> delete(String id) {
        return service.delete(id);
    }

    public Try<Page<TripListView>> match(String id, Pageable page) {
        return this.repository.match(id, page);
    }

    public Try<EntityId> merge(TripMergeForm form) {
        return mergeService.merge(form);
    }

    public Try<EntityId> confirm(String id, Authentication authentication) {
        return confirmationService.confirm(id, authentication);
    }

    public Try<EntityId> decline(String id, Authentication authentication) {
        return confirmationService.decline(id, authentication);
    }

    public Page<TripListView> listByUser(Pageable page, Authentication authentication) {
        return userRepository.findByEmail(authentication.getName())
                .map(u -> repository.listByUser(page, u.getId()))
                .getOrElse(Page::empty);
    }
}
