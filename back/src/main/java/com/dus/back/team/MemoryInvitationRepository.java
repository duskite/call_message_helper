package com.dus.back.team;

import com.dus.back.domain.Invitation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Slf4j
public class MemoryInvitationRepository implements InvitationRepository {

    private Map<String, Invitation> cacheStore = new ConcurrentHashMap<>();

    @Override
    public void save(Invitation invite) {

        cacheStore.put(invite.getUniqueKey(), invite);

        log.info("초대장 등록 되었는지 체크: {}", cacheStore.get(invite.getUniqueKey()));
    }

    @Override
    public void remove(Invitation invite) {
        cacheStore.remove(invite.getUniqueKey());
    }

    @Override
    public List<Invitation> findAllByInviteeUserId(String inviteeUserId) {
        List<Invitation> list = new ArrayList<>();

        String findUniqueKey = null;
        for (String key : cacheStore.keySet()) {
            if (key.contains(inviteeUserId)) {
                findUniqueKey = key;

                list.add(cacheStore.get(findUniqueKey));
            }
        }

        return list;
    }
}
