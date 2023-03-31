package com.dus.back.team;

import com.dus.back.domain.Invite;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.sl.draw.geom.GuideIf;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Slf4j
public class MemoryInviteRepository implements InviteRepository{

    private Map<String, Invite> cacheStore = new ConcurrentHashMap<>();

    @Override
    public void save(Invite invite) {

        cacheStore.put(invite.getUniqueKey(), invite);

        log.info("초대장 등록 되었는지 체크: {}", cacheStore.get(invite.getUniqueKey()));
    }

    @Override
    public void remove(Invite invite) {
        cacheStore.remove(invite.getUniqueKey());
    }

    @Override
    public Optional<Invite> findByInviteeUserId(String inviteeUserId) {
        String findUniqueKey = null;
        for (String key : cacheStore.keySet()) {
            if (key.contains(inviteeUserId)) {
                findUniqueKey = key;

                return Optional.ofNullable(cacheStore.get(findUniqueKey));
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<Invite> findByAdminUserId(String adminUserId) {
        String findUniqueKey = null;
        for (String key : cacheStore.keySet()) {
            if (key.contains(adminUserId)) {
                findUniqueKey = key;

                return Optional.ofNullable(cacheStore.get(findUniqueKey));
            }
        }

        return Optional.empty();
    }
}
