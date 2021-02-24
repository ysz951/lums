package com.zhou.lums;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.zhou.lums.model.User;
import com.zhou.lums.respository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
class LumsApplicationTests {

    @Autowired
    UserRepository userRepository;

	@Test
	void contextLoads() {
	}

	@Test
    public void testCreateProduct() throws Exception {
	    User user = new User();
	    user.setName("first");
	    user.setUsername("username1");
	    user.setEmail("test1@lums.com");
	    user.setPassword("password1");
	    user.setBlocked(false);
	    userRepository.save(user);
	    User db_user = userRepository.findByEmail("test1@lums.com")
	            .orElseThrow(() -> new Exception("User not found"));
	    assertEquals(user.getName(), db_user.getName());

	}

	@Test
    public void testInValidName() {
        User user = new User();
        user.setName("first1");
        user.setUsername("username2");
        user.setEmail("test2@lums.com");
        user.setPassword("password1");
        user.setBlocked(false);
        try {
            userRepository.save(user);
            fail("should not fail");
        } catch(Exception ex) {
            assertTrue(true);
        }

    }

	@Test
    public void duplicateEmail() {
        User user = new User();
        user.setName("second");
        user.setUsername("username2");
        user.setEmail("test1@lums.com");
        user.setPassword("password1");
        user.setBlocked(false);
        try {
            userRepository.save(user);
            fail("should not fail");
        } catch(Exception ex) {
            assertTrue(true);
        }

    }

	@Test
	public void invalidPassword() {
	    User user = new User();
        user.setName("third");
        user.setUsername("username3");
        user.setEmail("test3@lums.com");
        user.setPassword("ag");
        user.setBlocked(false);
        try {
            userRepository.save(user);
            fail("should not fail");
        } catch(Exception ex) {
            assertTrue(true);
        }
	}

}
