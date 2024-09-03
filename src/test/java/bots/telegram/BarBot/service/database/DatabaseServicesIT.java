package bots.telegram.BarBot.service.database;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class DatabaseServicesIT {

    @Autowired
    UserService userService;
    @Autowired
    UserChatService userChatService;

    @Test
    void shouldFindNullUserDtoById() {
        var user = userService.findById(0L);
        assertNull(user);
    }

    @Test
    void shouldFindNotNullUserDtoById() {
        var user = userService.findById(1L);
        assertNotNull(user);
    }

    @Test
    void shouldFindUserDataDtoById() {
        var userGroupDto = userChatService.findById(1L, -4117494362L);
        assertNotNull(userGroupDto);
    }
}