package com.wanderland.wanderlandserver.controller;

import com.wanderland.wanderlandserver.repositories.PhotoRepository;
import com.wanderland.wanderlandserver.repositories.RouteRepository;
import com.wanderland.wanderlandserver.services.PhotoHosterService;
import com.wanderland.wanderlandserver.services.PhotoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Irene Keller
 *
 * Currently only sets up a mockMvc instance which could then be used to test HTTP requests.
 */

@ExtendWith(MockitoExtension.class)
class PhotoControllerTest {

    @MockBean
    private PhotoRepository photoRepository;

    @MockBean
    private RouteRepository routeRepository;

    @MockBean
    private PhotoService photoService;

    @MockBean
    private PhotoHosterService photoHosterService;

    @InjectMocks
    private PhotoController photoController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(photoController).build();
    }

    /**
     * Test if the mockMVC object is created
     */
    @Test
    void shouldCreateMockMvc() {
        assertNotNull(mockMvc);
    }
    
}
