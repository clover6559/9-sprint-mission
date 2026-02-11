package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binarycontent")
@RequiredArgsConstructor
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @RequestMapping(
            path = "/find",
            method = RequestMethod.GET
    )
    public ResponseEntity<List<BinaryContent>> findAll(
            @RequestParam ("ids") List<UUID> uuidList
            ){
                if (uuidList == null || uuidList.isEmpty()) {
                    return ResponseEntity.badRequest().build();
                }
        List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(uuidList);
        return ResponseEntity.ok(binaryContents);
    }

    @RequestMapping(
            path = "/{id}",
            method = RequestMethod.GET
    )
    public ResponseEntity<BinaryContent> find(
            @PathVariable UUID id
    ){
        BinaryContent binaryContent = binaryContentService.find(id);
        return ResponseEntity.ok(binaryContent);
    }

}
