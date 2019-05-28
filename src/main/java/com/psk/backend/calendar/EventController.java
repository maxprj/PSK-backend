package com.psk.backend.calendar;

import com.psk.backend.calendar.value.EventForm;
import com.psk.backend.calendar.value.EventListView;
import com.psk.backend.common.CommonErrors;
import com.psk.backend.common.EntityId;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.ResponseEntity.unprocessableEntity;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private final EventControllerService service;

    public EventController(EventControllerService service) {
        this.service = service;
    }

    @CommonErrors
    @ApiOperation(value = "Get event list", response = EventListView.class)
    @GetMapping
    public List<EventListView> getAll(Authentication authentication) {
        return service.list(authentication);
    }

    @ApiOperation(value = "Create event", response = EntityId.class)
    @CommonErrors
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody EventForm form, Authentication authentication) {
        return service.create(form, authentication).fold(e -> unprocessableEntity().body(e), ResponseEntity::ok);
    }

//    @ApiOperation(value = "Update event", response = EntityId.class)
//    @CommonErrors
//    @PutMapping("/{id}")
//    public ResponseEntity<?> update(@PathVariable("id") String id, @Valid @RequestBody EventForm form, Authentication authentication) {
//        return service.update(id, form, authentication).fold(e -> unprocessableEntity().body(e), ResponseEntity::ok);
//    }

    @ApiOperation(value = "Delete event", response = EntityId.class)
    @CommonErrors
    @DeleteMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") String id, Authentication authentication) {
        return service.delete(id, authentication).fold(e -> unprocessableEntity().body(e), ResponseEntity::ok);
    }
}
