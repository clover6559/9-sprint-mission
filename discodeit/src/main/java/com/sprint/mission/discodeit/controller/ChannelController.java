package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.dto.user.UserFind;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;
    private final UserService userService;


    @RequestMapping(
            path = "/create/public",
            method = RequestMethod.POST
    )
    public ResponseEntity<Channel> createPublic(
            @RequestBody CreatePublicRequest request
    ) {
        UserFind user = userService.find(request.userId());
        CreatePublic createPublic = new CreatePublic(request.channelName(), request.description(), user);
        Channel channel = channelService.create(createPublic);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(channel);
    }

    @RequestMapping(
            path = "/create/private",
            method = RequestMethod.POST
    )
    public ResponseEntity<Channel> createPrivate(
            @RequestBody CreatePrivateRequest request
    ) {
        UserFind user = userService.find(request.userId());
        CreatePrivate createPrivate = new CreatePrivate(user);
        Channel channel = channelService.create(createPrivate);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(channel);
    }

    @RequestMapping(
            path = "/{channelId}",
            method = RequestMethod.PATCH
    )
    public ResponseEntity<Void> update(
            @PathVariable UUID channelId,
            @RequestBody ChannelUpdate update
    ) {
        ChannelUpdate channelUpdate = new ChannelUpdate(channelId, update.channelUpdateInfo());
        channelService.update(channelUpdate);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(
            path = "/{channelId}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity<Void> delete(
            @PathVariable UUID channelId
    ) {
        channelService.delete(channelId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(
            path = "/{userId}/channel",
            method = RequestMethod.GET
    )
    public ResponseEntity<List<ChannelResponse>> findAll(
            @PathVariable UUID userId
    ) {
        List<ChannelResponse> channelResponse = channelService.findAllByUserId(userId);
        return ResponseEntity.ok(channelResponse);

    }
}
